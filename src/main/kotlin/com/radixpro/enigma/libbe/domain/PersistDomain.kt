/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

import com.opencsv.bean.CsvBindByName

/**
 * Members in data classes for persistency need default values.
 * This forces Kotlin to add an empty constructor which is required by OpenCSV.
 */

interface Persistable {
    var id: Int
}

/**
 * Persistable data for a chart.
 */
data class ChartData(
    override var id: Int = 0,
    val name: String = "",
    val description: String  = "",
    val rating: Ratings = Ratings.UNKNOWN,
    val chartType: ChartTypes = ChartTypes.UNKNOWN,
    val jdUt: Double = 0.0,
    val geoLat: Double = 0.0,
    val geoLon: Double = 0.0,
    val input: String = ""
): Persistable

/**
 * Persistable data for an event.
 */
data class ChartEvent(
    override var id: Int = 0,
    val chartId: Int = 0,
    val description: String = "",
    val jdUt: Double = 0.0,
    val geoLat: Double = 0.0,
    val geoLon: Double = 0.0,
    val input: String= ""
): Persistable

/**
 * Persistable data for a configuration.
 */
data class PersistedConfig(
    @CsvBindByName override var id: Int = 0,
    @CsvBindByName val name: String = "",
    @CsvBindByName val description: String = "",
    @CsvBindByName val ayanamsha: Ayanamshas = Ayanamshas.NONE,
    @CsvBindByName val houseSystem: HouseSystems = HouseSystems.NO_HOUSES,
    @CsvBindByName val observerPos: ObserverPos = ObserverPos.GEOCENTRIC,
    @CsvBindByName val celPointsText: String = "",
    @CsvBindByName val aspectsText: String = ""
): Persistable

/**
 * A configuration that is created from PersistedConfig.
 */
data class Config(
    override var id: Int,
    val name: String,
    val description: String,
    val ayanamsha: Ayanamshas,
    val houseSystem: HouseSystems,
    val observerPos: ObserverPos,
    val celPoints: List<PersistedCelPoint>,
    val aspects: List<PersistedAspect>
): Persistable

/**
 * Persistable celestial point.
 */
data class PersistedCelPoint(
    val celPoint: CelPoints,
    val showInDrawing: Boolean,
    val glyph: String
)

/**
 * Persistable aspect.
 */
data class PersistedAspect(
    val aspect: Aspects,
    val showInDrawing: Boolean,
    val glyph: String
)
