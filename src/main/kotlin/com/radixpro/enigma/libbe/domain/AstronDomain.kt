/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

/**
 * Elements of date and time.
 */
data class DateTimeParts(val year: Int,
                         val month: Int,
                         val day: Int,
                         val hour: Int,
                         val minute: Int,
                         val second: Int,
                         val offsetUt: Double,
                         val gregorian: Boolean)

/**
 * Coordinates for location.
 */
data class Location(val geoLat: Double,
                    val geoLon: Double)

data class CoordinateSet(val position: Double,
                         val deviation: Double)

/**
 * Single position for a basechart.
 */
data class BasePosCelPoint(val celPoint: CelPoints,
                           val eclCoord: CoordinateSet,
                           val equCoord: CoordinateSet,
                           val eclSpeed: CoordinateSet,
                           val equSpeed: CoordinateSet)

/**
 * Single cusp for a basechart.
 */
data class BaseHousePoint(val eclCoord: CoordinateSet,
                          val equCoord: CoordinateSet)

/**
 * Positions of houses, asc and mc for a basechart.
 */
data class BaseHouseSystem(val asc: BaseHousePoint,
                           val mc: BaseHousePoint,
                           val cusps: List<BaseHousePoint>)

/**
 * All calculated positions for a basechart.
 */
data class BaseChartPositions(val celPositions: List<BasePosCelPoint>,
                              val houseSystem: BaseHouseSystem
)

/**
 * Single position for a simplechart.
 */
data class SimplePosCelPoint(val celPoint: CelPoints, val coords: CoordinateSet, val speeds: CoordinateSet)

/**
 * Positions of houses, asc and mc for a simplechart.
 */
data class SimpleHouseSystem(val asc: Double,
                           val mc: Double,
                           val cusps: List<Double>)

/**
 * All calculated positions for a smplechart.
 */
data class SimpleChartPositions(val celPositions: List<SimplePosCelPoint>,
                              val houseSystem: SimpleHouseSystem)


/**
 * Timeseries for a single celestial point. TimePositions contains pairs of <Julian Day, Position>
 */
data class TimeSeriesValues(val celPoint: CelPoints, val timePositions: List<Pair<Double, Double>>)

