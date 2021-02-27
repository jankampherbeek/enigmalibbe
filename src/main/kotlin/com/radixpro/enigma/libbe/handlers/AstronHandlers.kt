/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.handlers

import com.radixpro.enigma.libbe.api.BaseChartResponse
import com.radixpro.enigma.libbe.api.EpsilonResponse
import com.radixpro.enigma.libbe.api.JdResponse
import com.radixpro.enigma.libbe.astron.*
import com.radixpro.enigma.libbe.domain.*
import swisseph.SweConst

class BaseChartHandler(private val celPointCalculator: CelPointCalculator,
                       private val housesCalculator: HousesCalculator,
                       private val epsilon: Epsilon) {

    private val eclFlags = 2 or 256 // Use Swiss Eph and speed
    private val equFlags = eclFlags or SweConst.SEFLG_EQUATORIAL
    private var errorTxt = ""

    fun calcBaseChartPositions(jdUt: Double,
                               celPoints: List<CelPoints>,
                               houseSystem: HouseSystems,
                               location: Location): BaseChartResponse {
        errorTxt = ""     // reset to handle subsequent calls
        val calculatedCelPoints: MutableList<BasePosCelPoint> = ArrayList()
        for (celPoint in celPoints) {
            calculatedCelPoints.add(constructBasePosCelPoint(jdUt, celPoint, location))
        }
        val housesCalcResult = housesCalculator.calcPositionsForHouses(jdUt, eclFlags, location,
            houseSystem.seId.toInt(), houseSystem.nrOfCusps)
        val ascEcl = CoordinateSet(housesCalcResult.first[0], 0.0)
        val mcEcl = CoordinateSet(housesCalcResult.first[1], 0.0)
        val obliquity = epsilon.calcTrueEpsilon(jdUt).first
        val asc = constructBaseHousePoint(ascEcl, obliquity)
        val mc = constructBaseHousePoint(mcEcl, obliquity)
        val allCusps: MutableList<BaseHousePoint> = ArrayList()
        for (cusp in housesCalcResult.second) {
            val cuspEcl = CoordinateSet(cusp, 0.0)
            allCusps.add(constructBaseHousePoint(cuspEcl, obliquity))
        }
        val result = BaseChartPositions(calculatedCelPoints, BaseHouseSystem(asc, mc, allCusps))
        return BaseChartResponse(result, errorTxt)
    }

    private fun constructBasePosCelPoint(jdUt: Double, celPoint: CelPoints, location: Location): BasePosCelPoint {
        val eclCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.seId, eclFlags, location)
        val eclPos = CoordinateSet(eclCalcResult.first[0], eclCalcResult.first[1])
        val eclSpeed = CoordinateSet(eclCalcResult.first[3], eclCalcResult.first[4])
        if (eclCalcResult.second.isNotEmpty()) errorTxt+= eclCalcResult.second
        val equCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.id, equFlags, location)
        val equPos = CoordinateSet(equCalcResult.first[0], equCalcResult.first[1])
        val equSpeed = CoordinateSet(equCalcResult.first[3], equCalcResult.first[4])
        if (equCalcResult.second.isNotEmpty()) errorTxt+= equCalcResult.second
        return BasePosCelPoint(celPoint, eclPos, equPos, eclSpeed, equSpeed)
    }

    private fun constructBaseHousePoint(cuspEcl: CoordinateSet, obliquity: Double): BaseHousePoint {
        val cuspEquCoord = CoordinateConversions.eclipticToEquatorial(doubleArrayOf(cuspEcl.position, 0.0), obliquity)
        val cuspEqu = CoordinateSet(cuspEquCoord[0], cuspEquCoord[1])
        return BaseHousePoint(cuspEcl, cuspEqu)
    }

}

class EpsilonHandler(private val epsilon: Epsilon) {

    fun calcTrueEpsilon(jdUt: Double): EpsilonResponse {
        val result = epsilon.calcTrueEpsilon(jdUt)
        return EpsilonResponse(result.first, result.second)
    }
}

class DateTimeHandler(private val julianDayNr: JulianDayNr) {

    fun calcJdUt(dateTimeParts: DateTimeParts): JdResponse {
        var calculatedJd: Double = 0.0
        var errorTxt = ""
        try {
            calculatedJd = julianDayNr.calculateJdUt(dateTimeParts)
        } catch (e: Exception) {
            errorTxt = "Error for jd: " + e.message
        }
        return JdResponse(calculatedJd, errorTxt)
    }

    fun isValidDate(year: Int, month: Int, day: Int, gregorian: Boolean): Boolean {
        return julianDayNr.isValidDate(year, month, day, gregorian)
    }
}


