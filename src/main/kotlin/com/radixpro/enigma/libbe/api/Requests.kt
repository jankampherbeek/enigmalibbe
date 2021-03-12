/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.*

interface Request

interface ReadRequest{
    val action: ReadActions
    val fileAndPath: String
}

interface WriteRequest{
    val action: WriteActions
    val fileAndPath: String
    val items: List<Persistable>
}


data class EpsilonRequest(val jdUt: Double): Request

data class JdUtRequest(val dateTimeParts: DateTimeParts): Request

data class ValidDateRequest(val year:Int, val month: Int, val day: Int, val gregorian: Boolean): Request

data class BaseChartRequest(val jdUt: Double, val celPoints: List<CelPoints>, val houseSystem: HouseSystems,
                            val location: Location): Request

data class TimeSeriesRequest(val celPoints: List<CelPoints>, val observerPos: ObserverPos, val coordinates: Coordinates,
                             val startJd: Double, val location: Location = Location(0.0, 0.0),
                             val interval: Double = 1.0, val repeats: Int = 100): Request

data class ChartWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                             override val items: List<ChartData>): WriteRequest

data class ChartReadRequest(override val action: ReadActions, override val fileAndPath: String, val searchId: Int = 0,
                            val searchPartOfName: String = ""): ReadRequest

data class EventWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                             override val items: List<ChartEvent>): WriteRequest

data class EventReadRequest(override val action: ReadActions, override val fileAndPath: String,
                            val searchId: Int = 0): ReadRequest

data class ConfigWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                              override val items: List<Config>): WriteRequest

data class ConfigReadRequest(override val action: ReadActions, override val fileAndPath: String,
                             val searchId: Int = 0): ReadRequest