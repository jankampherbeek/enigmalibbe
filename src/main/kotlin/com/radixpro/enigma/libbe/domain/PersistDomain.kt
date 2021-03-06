/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

data class PersistedChart(
    val id: Int,
    val name: String,
    val description: String,
    val rating: Ratings,
    val chartType: ChartTypes,
    val jdUt: Double,
    val geoLat: Double,
    val geoLon: Double,
    val input: String
)

data class PersistedEvent(
    val id: Int,
    val chartId: Int,
    val description: String,
    val jdUt: Double,
    val geoLat: Double,
    val geoLon: Double,
    val input: String
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


data class PersistedConfig(
    val id: Int,
    val name: String,
    val description: String,
    val ayanamsha: Ayanamshas,
    val houseSystem: HouseSystems,
    val observerPos: ObserverPos,
    val celPoints: List<PersistedCelPoint>,
    val aspects: List<PersistedAspect>
)

