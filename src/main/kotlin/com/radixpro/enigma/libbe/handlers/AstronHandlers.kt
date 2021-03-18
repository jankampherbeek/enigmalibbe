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

abstract class ChartHandler(private val celPointCalculator: CelPointCalculator,
                            private val housesCalculator: HousesCalculator,
                            private val epsilon: Epsilon
){
    protected val eclFlags = 2 or 256 // Use Swiss Eph and speed
    private val equFlags = eclFlags or SweConst.SEFLG_EQUATORIAL
    private var comments = ""
    private var errors = false
    protected var jdUt = 0.0
    protected var obliquity = 0.0
    protected var location = Location(0.0,  0.0)

    fun calcChartPositions(request: Request): Response {
        comments = ""     // reset to handle subsequent calls
        errors = false
        val actRequest = request as ChartRequest
        jdUt = actRequest.jdUt
        location = actRequest.location
        val obliquityResult = epsilon.calcTrueEpsilon(jdUt)
        obliquity = obliquityResult.first
        comments+= obliquityResult.second
        val calculatedCelPoints: MutableList<PosCelPoint> = ArrayList()
        for (celPoint in actRequest.celPoints) {
            calculatedCelPoints.add(definePosCelPoint(celPoint))
        }
        val housesCalcResult = housesCalculator.calcPositionsForHouses(
            jdUt, eclFlags, actRequest.location,
            actRequest.houseSystem.seId.toInt(), actRequest.houseSystem.nrOfCusps
        )
        val ascEcl = CoordinateSet(housesCalcResult.first[0], 0.0)
        val mcEcl = CoordinateSet(housesCalcResult.first[1], 0.0)
        val asc = constructPosHousePoint(ascEcl)
        val mc = constructPosHousePoint(mcEcl)
        val allCusps: MutableList<PosHousePoint> = ArrayList()
        for (cusp in housesCalcResult.second) {
            val cuspEcl = CoordinateSet(cusp, 0.0)
            allCusps.add(constructPosHousePoint(cuspEcl))
        }
        val result = constructResult(calculatedCelPoints, asc, mc, allCusps)
        return ChartResponse(result, errors, comments)
    }

    protected fun defineEclPosCelPoint(celPoint: CelPoints): Triple<CoordinateSet, CoordinateSet, Double> {
        val eclCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.seId, eclFlags, location)
        val eclPos = CoordinateSet(eclCalcResult.first[0], eclCalcResult.first[1])
        val distance = eclCalcResult.first[2]
        val eclSpeed = CoordinateSet(eclCalcResult.first[3], eclCalcResult.first[4])
        if (eclCalcResult.second.isNotEmpty()) {
            comments += eclCalcResult.second
            errors = true
        }
        return Triple(eclPos, eclSpeed, distance)
    }

    protected fun defineEquPosCelPoint(celPoint: CelPoints): Pair<CoordinateSet, CoordinateSet> {
        val equCalcResult = celPointCalculator.calcMainPositionsForCelPoint(jdUt, celPoint.seId, equFlags, location)
        val equPos = CoordinateSet(equCalcResult.first[0], equCalcResult.first[1])
        val equSpeed = CoordinateSet(equCalcResult.first[3], equCalcResult.first[4])
        if (equCalcResult.second.isNotEmpty()) {
            comments += equCalcResult.second
            errors = true
        }
        return Pair(equPos, equSpeed)
    }

    protected abstract fun constructPosHousePoint(cuspEcl: CoordinateSet): PosHousePoint

    protected abstract fun constructResult(calculatedCelPoints: List<PosCelPoint>, asc: PosHousePoint, mc: PosHousePoint,
                                               allCusps: List<PosHousePoint>) : ChartPositions

    protected abstract fun definePosCelPoint(celPoint: CelPoints): PosCelPoint
}

class SimpleChartHandler(
    private val celPointCalculator: CelPointCalculator,
    private val housesCalculator: HousesCalculator,
    private val epsilon: Epsilon
): ChartHandler(celPointCalculator, housesCalculator, epsilon) {

    override fun definePosCelPoint(celPoint: CelPoints): PosCelPoint {
        val eclResult = defineEclPosCelPoint(celPoint)
        return SimplePosCelPoint(celPoint, eclResult.first, eclResult.second)
    }

    override fun constructPosHousePoint(cuspEcl: CoordinateSet): PosHousePoint {
        return SimplePosHousePoint(cuspEcl)
    }

    override fun constructResult(calculatedCelPoints: List<PosCelPoint>, asc: PosHousePoint, mc: PosHousePoint,
                                 allCusps: List<PosHousePoint>) : ChartPositions{
        return SimpleChartPositions(calculatedCelPoints as List<SimplePosCelPoint>,
            SimpleHousePositions(asc as SimplePosHousePoint, mc as SimplePosHousePoint,
                allCusps as List<SimplePosHousePoint>)
        )
    }
}


class BaseChartHandler(
    private val celPointCalculator: CelPointCalculator,
    private val housesCalculator: HousesCalculator,
    private val epsilon: Epsilon
): ChartHandler(celPointCalculator, housesCalculator, epsilon) {

    override fun definePosCelPoint(celPoint: CelPoints): PosCelPoint {
        val eclResult = defineEclPosCelPoint(celPoint)
        val equResult = defineEquPosCelPoint(celPoint)
        return BasePosCelPoint(celPoint, eclResult.first, equResult.first, eclResult.second, equResult.second)
    }

    override fun constructPosHousePoint(cuspEcl: CoordinateSet): BasePosHousePoint {
        val cuspEquCoord = CoordinateConversions.eclipticToEquatorial(doubleArrayOf(cuspEcl.position, 0.0), obliquity)
        val cuspEqu = CoordinateSet(cuspEquCoord[0], cuspEquCoord[1])
        return BasePosHousePoint(cuspEcl, cuspEqu)
    }

    override fun constructResult(calculatedCelPoints: List<PosCelPoint>, asc: PosHousePoint, mc: PosHousePoint,
                                 allCusps: List<PosHousePoint>) : ChartPositions{
        return BaseChartPositions(calculatedCelPoints as List<BasePosCelPoint>,
            BaseHousePositions(asc as BasePosHousePoint, mc as BasePosHousePoint,
                allCusps as List<BasePosHousePoint>)
        )
    }
}

class FullChartHandler(
    private val celPointCalculator: CelPointCalculator,
    private val housesCalculator: HousesCalculator,
    private val epsilon: Epsilon
): ChartHandler(celPointCalculator, housesCalculator, epsilon) {


    override fun definePosCelPoint(celPoint: CelPoints): FullPosCelPoint {
        val eclResult = defineEclPosCelPoint(celPoint)
        val equResult = defineEquPosCelPoint(celPoint)
        val horResult = defineHorPosCelPoint(eclResult.first.position, eclResult.first.deviation)
        return FullPosCelPoint(celPoint, eclResult.first, equResult.first, eclResult.second, equResult.second,
            horResult)
    }

    override fun constructPosHousePoint(cuspEcl: CoordinateSet): FullPosHousePoint {
        val cuspEquCoord = CoordinateConversions.eclipticToEquatorial(doubleArrayOf(cuspEcl.position, 0.0), obliquity)
        val cuspEqu = CoordinateSet(cuspEquCoord[0], cuspEquCoord[1])
        val eclCoord = arrayOf(cuspEcl.position, cuspEcl.deviation, 1.0).toDoubleArray()
        val horResult =  celPointCalculator.getHorizontalPosition(jdUt, eclCoord, location, eclFlags)
        val horCoord = CoordinateSet(horResult[0], horResult[1])
        return FullPosHousePoint(cuspEcl, cuspEqu, horCoord)
    }

    override fun constructResult(calculatedCelPoints: List<PosCelPoint>, asc: PosHousePoint, mc: PosHousePoint,
                                 allCusps: List<PosHousePoint>) : ChartPositions{
        return FullChartPositions(calculatedCelPoints as List<FullPosCelPoint>,
            createFullHousePositions(asc, mc, allCusps))
    }

    private fun defineHorPosCelPoint(lon: Double, lat: Double): CoordinateSet {
        val eclCoord = arrayOf(lon, lat, 1.0).toDoubleArray()
        val horCalcResult = celPointCalculator.getHorizontalPosition(jdUt, eclCoord, location, eclFlags)
        return CoordinateSet(horCalcResult[0], horCalcResult[1])
    }

    private fun createFullHousePositions(asc: PosHousePoint, mc: PosHousePoint,
                                         allCusps: List<PosHousePoint>): FullHousePositions {
        return FullHousePositions(asc as FullPosHousePoint, mc as FullPosHousePoint,
            allCusps as List<FullPosHousePoint>)
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

