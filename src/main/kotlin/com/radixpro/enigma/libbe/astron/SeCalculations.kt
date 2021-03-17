/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import com.radixpro.enigma.libbe.domain.DateTimeParts
import com.radixpro.enigma.libbe.domain.Location
import com.radixpro.enigma.libbe.domain.SeFlags
import com.radixpro.enigma.libbe.math.Trigonometry.asin
import com.radixpro.enigma.libbe.math.Trigonometry.atan2
import com.radixpro.enigma.libbe.math.Trigonometry.cos
import com.radixpro.enigma.libbe.math.Trigonometry.sin
import com.radixpro.enigma.libbe.math.Trigonometry.tan
import com.radixpro.enigma.libbe.utils.Range
import swisseph.SweDate
import swisseph.SwissEph
import swisseph.SwissLib

private const val MINUTES_PER_HOUR = 60.0
private const val SECONDS_PER_HOUR = 3600.0

/**
 * Obliquity of the earth axis (Epsilon).
 */
class Epsilon(private val swissEph: SwissEph) {

    /**
     * Calculate the value of true Epsilon (obliquity of the earth axis, corrected for nutation).
     * Returns a Pair with the value for epsilon and an error message.
     */
    fun calcTrueEpsilon(jdUt: Double): Pair<Double, String> {
        val seId = -1
        val results = DoubleArray(6)
        val flags = 2   // use Swiss Ephemeris, default. Other flags are not applicable or not relevant.
        val errorMsg = StringBuffer()
        swissEph.swe_calc_ut(jdUt, seId, flags, results, errorMsg)
        return Pair(results[0], errorMsg.toString())
    }
}

/**
 * Calculations for the Julian Day Number.
 */
class JulianDayNr {

    /**
     * Calculate Julian Day for UT.
     */
    fun calculateJdUt(dtParts: DateTimeParts): Double {
        val time =
            ((dtParts.hour - dtParts.offsetUt) + dtParts.minute / MINUTES_PER_HOUR + dtParts.second / SECONDS_PER_HOUR)
        val sweDate = SweDate(dtParts.year, dtParts.month, dtParts.day, time, dtParts.gregorian)
        return sweDate.julDay
    }

    /**
     * Checks if a date is valid.
     */
    fun isValidDate(
        year: Int,
        month: Int,
        day: Int,
        gregorian: Boolean
    ): Boolean {
        val sweDate1 = SweDate(year, month, day, 0.0, gregorian)
        val calculatedJulDay = sweDate1.julDay
        val sweDate2 = SweDate(calculatedJulDay, gregorian)
        return sweDate1.year == sweDate2.year && sweDate1.month == sweDate2.month && sweDate1.day == sweDate2.day
    }
}

class CelPointCalculator(private val swissEph: SwissEph) {

    /**
     * Calculate ecliptical or equatorial positions for a body
     *
     * @param jdUt  Julian Day based on Ephemeris Time.
     * @param id    indicates the body.
     * @param flags combined settings for the SE.
     * @param location optional location, defaults to lat 0.0 and lon 0.0, use real value for topocentric positions.
     * @return calculated positions. Array contains ecliptical/equatorial positions: from 0..5: Longitude, latitude,
     * distance in AU, speed long, speed lat, speed dist. For equatorial positions from 0..5: right ascension,
     * declination, distance in AU, speed RA, speed decl, speed dist.
     */
    fun calcMainPositionsForCelPoint(
        jdUt: Double,
        id: Int,
        flags: Int,
        location: Location = Location(0.0, 0.0)
    ): Pair<DoubleArray, String> {
        val allPositions = DoubleArray(6)
        val errorMsg = StringBuffer()     // StringBuilder not possible because Java Port to the SE uses a StringBuffer.
        if (flags == (flags or SeFlags.TOPOCENTRIC.seValue.toInt())) {
            swissEph.swe_set_topo(location.geoLon, location.geoLat, 0.0)
        }
        swissEph.swe_calc_ut(jdUt, id, flags, allPositions, errorMsg)
        return Pair(allPositions, errorMsg.toString())
    }

    /**
     * Calculate horizontal positions for any point
     *
     * @param jdUt     Julian day based on Ephemeris Time
     * @param eclCoord ecliptical coördinates: index 0 = longitude, 1 = latitude, 2 = distance
     * @param location geographic latitude and longitude
     * @param flags    combined settings for the SE
     * @return calculated positions (Azimuth and Altitude)
     */
    fun getHorizontalPosition(jdUt: Double,
                              eclCoord: DoubleArray,
                              location: Location,
                              flags: Int): DoubleArray {
        require(3 == eclCoord.size)
        val geoPos = doubleArrayOf(location.geoLon, location.geoLat, 0.0)
        val eclPos = doubleArrayOf(eclCoord[0], eclCoord[1], eclCoord[2])
        val atPress = 0.0
        val atTemp = 0.0
        val azAlt = DoubleArray(3)
        swissEph.swe_azalt(jdUt, flags, geoPos, atPress, atTemp, eclPos, azAlt)
        return azAlt
    }

}

class HousesCalculator(private val swissEph: SwissEph) {

    /**
     * Calculate positions for houses
     *
     * @param jdUt      Julian Day based on Universal Time
     * @param flags     combined settings for the SE
     * @param location  geographic latitude and longitude
     * @param system    the housesystem to use
     * @param nrOfCusps number of cusps for the current housesystem
     * @return calculated positions in a Pair with 2 DoubleArrays. The first array contains respectively asc, mc, armc,
     * vertex, eastpoint, co-ascendant (Koch), co-ascendant (Munkasey) and polar ascendant (Munkasey). The second
     * array contains the cusps starting with index 1.
     */
    fun calcPositionsForHouses(jdUt: Double,
                               flags: Int,
                               location: Location,
                               system: Int,
                               nrOfCusps: Int): Pair<DoubleArray, DoubleArray> {
        val cusps = DoubleArray(nrOfCusps + 1)
        val ascMc = DoubleArray(10)
        val tempCusps = DoubleArray(100)
        swissEph.swe_houses(jdUt, flags, location.geoLat, location.geoLon, system, tempCusps, ascMc)
        if (nrOfCusps >= 0) System.arraycopy(tempCusps, 1, cusps, 1, nrOfCusps)
        return Pair(ascMc, cusps)
    }

}

object CoordinateConversions {
    /**
     * Convert ecliptic coordinates to equatorial coordinates.
     *
     * @param eclipticValues Array with Longitude and Latitude in degrees.
     * @param obliquity      Obliquity (Epsilon) in degrees.
     * @return Array with right ascension and declination in degrees.
     */
    fun eclipticToEquatorial(
        eclipticValues: DoubleArray,
        obliquity: Double
    ): DoubleArray {
        val fullEclValues = doubleArrayOf(eclipticValues[0], eclipticValues[1], 1.0)
        val equatorialValues = DoubleArray(3)
        SwissLib().swe_cotrans(fullEclValues, equatorialValues, -obliquity) // obliquity must be negative !
        return equatorialValues
    }

    /**
     * Converts right ascension to longitude in the correct quadrant.<br></br>
     * Uses formula: tan L = tan RA / cos E<br></br>
     * RA = right ascension, L = longitude, E = epsilon.
     *
     * @param ra  The right ascension in decimal degrees. If rightAsc is not in range 0 - &lt; 360 this will be corrected.
     * @param eps The value for epsilon or obliquity, defining the angle between ecliptic and equator.
     * @return longitude in in decimal degrees, possible values 0 - &lt; 360.
     */
    fun rightAscToLongitude(ra: Double, eps: Double): Double {
        val workRA = Range.checkValue(ra)
        var lon = atan2(tan(workRA), cos(eps))
        lon = Range.checkValue(lon)
        if (workRA < 180 && lon >= 180) lon -= 180.0
        if (workRA >= 180 && lon < 180) lon += 180.0
        return lon
    }

    /**
     * Calculates declination for an ecliptic point without latitude.<br></br>
     * Uses the formula: sin declination = sin lon . sin eps).
     *
     * @param lon Ecliptical longitude in degrees. If longitude is not in range 0 - &lt; 360 this will be corrected.
     * @param eps The value for epsilon or obliquity, defining the angle between ecliptic and equator.
     * @return The declination in degrees.
     */
    fun longitudeToDeclination(lon: Double, eps: Double): Double {
        val workLongitude = Range.checkValue(lon)
        return asin(sin(workLongitude) * sin(eps))
    }
}



