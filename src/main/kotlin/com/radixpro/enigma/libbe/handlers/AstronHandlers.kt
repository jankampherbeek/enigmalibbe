/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.handlers

import com.radixpro.enigma.libbe.api.*
import com.radixpro.enigma.libbe.astron.*
import com.radixpro.enigma.libbe.domain.*
import swisseph.SweConst
import kotlin.Double.Companion.NaN

class BaseChartHandler(
    private val celPointCalculator: CelPointCalculator,
    private val housesCalculator: HousesCalculator,
    private val epsilon: Epsilon
) {

    private val eclFlags = 2 or 256 // Use Swiss Eph and speed
    private val equFlags = eclFlags or SweConst.SEFLG_EQUATORIAL
    private var comments = ""
    private var errors = false

    fun calcBaseChartPositions(request: Request): Response {
        comments = ""     // reset to handle subsequent calls
        errors = false
        val actRequest = request as BaseChartRequest
        val calculatedCelPoints: MutableList<BasePosCelPoint> = ArrayList()
        for (celPoint in actRequest.celPoints) {
            calculatedCelPoints.add(constructBasePosCelPoint(actRequest.jdUt, celPoint, actRequest.location))
        }
        val housesCalcResult = housesCalculator.calcPositionsForHouses(
            actRequest.jdUt, eclFlags, actRequest.location,
            actRequest.houseSystem.seId.toInt(), actRequest.houseSystem.nrOfCusps
        )
        val ascEcl = CoordinateSet(housesCalcResult.first[0], 0.0)
        val mcEcl = CoordinateSet(housesCalcResult.first[1], 0.0)
        val obliquity = epsilon.calcTrueEpsilon(actRequest.jdUt).first
        val asc = constructBaseHousePoint(ascEcl, obliquity)
        val mc = constructBaseHousePoint(mcEcl, obliquity)
        val allCusps: MutableList<BasePosHousePoint> = ArrayList()
        for (cusp in housesCalcResult.second) {
            val cuspEcl = CoordinateSet(cusp, 0.0)
            allCusps.add(constructBaseHousePoint(cuspEcl, obliquity))
        }
        val result = BaseChartPositions(calculatedCelPoints, BaseHousePositions(asc, mc, allCusps))
        return BaseChartResponse(result, errors, comments)
    }

    private fun constructBasePosCelPoint(jdUt: Double, celPoint: CelPoints, location: Location): BasePosCelPoint {
        val eclCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.seId, eclFlags, location)
        val eclPos = CoordinateSet(eclCalcResult.first[0], eclCalcResult.first[1])
        val eclSpeed = CoordinateSet(eclCalcResult.first[3], eclCalcResult.first[4])
        if (eclCalcResult.second.isNotEmpty()) {
            comments += eclCalcResult.second
            errors = true
        }
        val equCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.id, equFlags, location)
        val equPos = CoordinateSet(equCalcResult.first[0], equCalcResult.first[1])
        val equSpeed = CoordinateSet(equCalcResult.first[3], equCalcResult.first[4])
        if (equCalcResult.second.isNotEmpty()) {
            comments += equCalcResult.second
            errors = true
        }
        return BasePosCelPoint(celPoint, eclPos, equPos, eclSpeed, equSpeed)
    }

    private fun constructBaseHousePoint(cuspEcl: CoordinateSet, obliquity: Double): BasePosHousePoint {
        val cuspEquCoord = CoordinateConversions.eclipticToEquatorial(doubleArrayOf(cuspEcl.position, 0.0), obliquity)
        val cuspEqu = CoordinateSet(cuspEquCoord[0], cuspEquCoord[1])
        return BasePosHousePoint(cuspEcl, cuspEqu)
    }

}

class EpsilonHandler(private val epsilon: Epsilon) {

    fun calcTrueEpsilon(request: Request): Response {
        var result = 0.0
        var comments = ""
        var errors = false
        val actRequest = request as EpsilonRequest
        try {
            val pairedResult = epsilon.calcTrueEpsilon(actRequest.jdUt)
            result = pairedResult.first
            if (result.equals(NaN)) throw Exception("Julian day out of range.")
            comments += pairedResult.second
        } catch (e: Exception) {
            errors = true
            comments += "Error calculating epsilon: " + e.message
        }
        return SingleDoubleResponse(result, errors, comments)
    }
}

class DateTimeHandler(private val julianDayNr: JulianDayNr) {

    fun calcJdUt(request: Request): SingleDoubleResponse {
        var calculatedJd: Double
        var comments = ""
        var errors = false
        val actRequest = request as JdUtRequest
        try {
            if (!isValidDate(
                    ValidDateRequest(
                        actRequest.dateTimeParts.year, actRequest.dateTimeParts.month,
                        actRequest.dateTimeParts.day, actRequest.dateTimeParts.gregorian
                    )
                )
            ) throw Exception("Invalid date")
            calculatedJd = julianDayNr.calculateJdUt(actRequest.dateTimeParts)
        } catch (e: Exception) {
            errors = true
            comments = "Error for jd: " + e.message
            calculatedJd = NaN
        }
        return SingleDoubleResponse(calculatedJd, errors, comments)
    }

    fun isValidDate(request: Request): Boolean {
        val actRequest = request as ValidDateRequest
        return julianDayNr.isValidDate(actRequest.year, actRequest.month, actRequest.day, actRequest.gregorian)
    }
}


class TimeSeriesHandler(private val celPointCalculator: CelPointCalculator) {

    fun calcSimpleTimeSeries(request: Request): TimeSeriesResponse {
        val actRequest = request as TimeSeriesRequest
        var actJd = actRequest.startJd
        val flags = FlagConstructor.defineFlags(actRequest.observerPos, actRequest.coordinates)
        var comments = ""
        val positionsForAllPoints = mutableListOf<TimeSeriesValues>()
        for (celPoint in actRequest.celPoints) {
            val positionsForPoint = mutableListOf<Pair<Double, Double>>()
            for (i in 0 until actRequest.repeats) {
                val result =
                    celPointCalculator.calcMainPositionsForCelPoint(actJd, celPoint.seId, flags, actRequest.location)
                comments += result.second
                positionsForPoint.add(Pair(actJd, result.first[0]))
                actJd++
            }
            positionsForAllPoints.add(TimeSeriesValues(celPoint, positionsForPoint))
        }
        val errors = comments.isNotEmpty()
        return TimeSeriesResponse(positionsForAllPoints, errors, comments)
    }


}

