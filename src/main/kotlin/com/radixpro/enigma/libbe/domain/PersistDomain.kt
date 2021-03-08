/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

/**
 * Members in data classes for persistency need default values.
 * This forces Kotlin to add an empty constructor which is required by OpenCSV.
 */

data class PersistedChart(
    var id: Int = 0,
    val name: String = "",
    val description: String  = "",
    val rating: Ratings = Ratings.UNKNOWN,
    val chartType: ChartTypes = ChartTypes.UNKNOWN,
    val jdUt: Double = 0.0,
    val geoLat: Double = 0.0,
    val geoLon: Double = 0.0,
    val input: String = ""
)

data class PersistedEvent(
    val id: Int = 0,
    val chartId: Int = 0,
    val description: String = "",
    val jdUt: Double = 0.0,
    val geoLat: Double = 0.0,
    val geoLon: Double = 0.0,
    val input: String= ""
)

data class PersistedConfig(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val ayanamsha: Ayanamshas = Ayanamshas.NONE,
    val houseSystem: HouseSystems = HouseSystems.NO_HOUSES,
    val observerPos: ObserverPos = ObserverPos.GEOCENTRIC,
    val celPoints: List<PersistedCelPoint> = mutableListOf(),
    val aspects: List<PersistedAspect> = mutableListOf()
)

data class PersistedCelPoint(
    val celPoint: CelPoints,
    val showInDrawing: Boolean,
    val glyph: String
)

data class PersistedAspect(
    val aspect: Aspects,
    val showInDrawing: Boolean,
    val showInOut: Boolean,
    val glyph: String,
    val secondGlyph: String
)