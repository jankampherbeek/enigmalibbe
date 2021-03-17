/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

// ------------ Celestial Points ----------------------------------

interface PosCelPoint {
    val celPoint: CelPoints
    val eclCoord: CoordinateSet
}

/**
 * Single position for a simplechart.
 */
data class SimplePosCelPoint(
    override val celPoint: CelPoints,
    override val eclCoord: CoordinateSet,
    val speeds: CoordinateSet
) : PosCelPoint


/**
 * Single position for a basechart.
 */
data class BasePosCelPoint(
    override val celPoint: CelPoints,
    override val eclCoord: CoordinateSet,
    val equCoord: CoordinateSet,
    val eclSpeed: CoordinateSet,
    val equSpeed: CoordinateSet
) : PosCelPoint

/**
 * Full position for a basechart.
 */
data class FullPosCelPoint(
    override val celPoint: CelPoints,
    override val eclCoord: CoordinateSet,
    val equCoord: CoordinateSet,
    val eclSpeed: CoordinateSet,
    val equSpeed: CoordinateSet,
    val horCoord: CoordinateSet) : PosCelPoint

// ------------ House Points ----------------------------------

interface PosHousePoint {
    val eclCoord: CoordinateSet
}

interface HousePositions {
    val asc: PosHousePoint
    val mc: PosHousePoint
    val cusps: List<PosHousePoint>
}

/**
 * Single cusp for a simplechart.
 */
data class SimplePosHousePoint(override val eclCoord: CoordinateSet) : PosHousePoint

/**
 * Single cusp for a basechart.
 */
data class BasePosHousePoint(
    override val eclCoord: CoordinateSet,
    val equCoord: CoordinateSet
) : PosHousePoint

/**
 * Single cusp for a fullchart.
 */
data class FullPosHousePoint(override val eclCoord: CoordinateSet,
                             val equCoord: CoordinateSet,
                             val horCoord: CoordinateSet): PosHousePoint

/**
 * Positions of houses, asc and mc for a simplechart.
 */
data class SimpleHousePositions(
    override val asc: SimplePosHousePoint,
    override val mc: SimplePosHousePoint,
    override val cusps: List<SimplePosHousePoint>
) : HousePositions

/**
 * Positions of houses, asc and mc for a basechart.
 */
data class BaseHousePositions(
    override val asc: BasePosHousePoint,
    override val mc: BasePosHousePoint,
    override val cusps: List<BasePosHousePoint>
) : HousePositions

/**
 * Positions of houses, asc, mc, vertex and eastpoint for a fullchart.
 */
data class FullHousePositions(
    override val asc: FullPosHousePoint,
    override val mc: FullPosHousePoint,
    override val cusps: List<FullPosHousePoint>
) : HousePositions


// ------------ Charts ----------------------------------

interface ChartPositions {
    val celPositions: List<PosCelPoint>
    val housePositions: HousePositions
}

/**
 * All calculated positions for a simplechart.
 */
data class SimpleChartPositions(
    override val celPositions: List<SimplePosCelPoint>,
    override val housePositions: SimpleHousePositions
) : ChartPositions

/**
 * All calculated positions for a basechart.
 */
data class BaseChartPositions(
    override val celPositions: List<BasePosCelPoint>,
    override val housePositions: BaseHousePositions
) : ChartPositions

/**
 * All calculated positions for a fullchart.
 */
data class FullChartPositions(
    override val celPositions: List<FullPosCelPoint>,
    override val housePositions: FullHousePositions
) : ChartPositions

// ------------ Time series -------------------------------------
/**
 * Timeseries for a single celestial point. TimePositions contains pairs of <Julian Day, Position>
 */
data class TimeSeriesValues(val celPoint: CelPoints, val timePositions: List<Pair<Double, Double>>)


// ------------ Time and location -------------------------------

/**
 * Elements of date and time.
 */
data class DateTimeParts(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val offsetUt: Double,
    val gregorian: Boolean
)

/**
 * Coordinates for location.
 */
data class Location(
    val geoLat: Double,
    val geoLon: Double
)

data class CoordinateSet(
    val position: Double,
    val deviation: Double
)




