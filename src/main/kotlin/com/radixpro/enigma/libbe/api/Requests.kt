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
    val searchId: Int
}

interface WriteRequest{
    val action: WriteActions
    val fileAndPath: String
    val items: List<Persistable>
}

/**
 * Request for the calculation of the obliquityn of the earth-axis (Epsilon). Required only Julian day for UT.
 */
data class EpsilonRequest(val jdUt: Double): Request

/**
 * Request for the calculation of a Julian Day Number for UT.
 */
data class JdUtRequest(val dateTimeParts: DateTimeParts): Request

/**
 * Request to check the validness of a date
 */
data class ValidDateRequest(val year:Int, val month: Int, val day: Int, val gregorian: Boolean): Request

/**
 * Request to construct a formatted text for a date and time, given a Julian Day Number.
 */
data class DateTimeTxtRequest(val jd: Double, val gregorian: Boolean): Request

/**
 * Request for the calculation of a chart.
 */
data class ChartRequest(val chartRequestType: ChartRequestTypes,val jdUt: Double, val celPoints: List<CelPoints>,
                        val houseSystem: HouseSystems, val location: Location): Request

/**
 * Request for the calculation of time series for specific celestial points.
 * @param celPoints: the celestial points that you want to calculate in the time series.
 * @param observerPos: the position of the observer (geocentric, topocentric, heliocentric).
 * @param coordinateTypes: the type of coordinates (ecliptical, equatorial, horizontal).
 * @param startJd: Julian day number for the UT of the startmoment.
 * @param location: geographical location, default a liatitude of 0.0 and a longitude of 0.0
 *                  (only relevant for topocentric positions).
 * @param interval: number of days between each calculation, this can be a fraction. Default 1.
 * @param repeats: the number of repeats for the calculation. Default 100.
 */
data class TimeSeriesRequest(val celPoints: List<CelPoints>, val observerPos: ObserverPos,
                             val coordinateTypes: CoordinateTypes, val startJd: Double,
                             val location: Location = Location(0.0, 0.0),
                             val interval: Double = 1.0, val repeats: Int = 100): Request

/**
 * Request to change the data for a chart.
 * @param action the action to perform (WriteAll, Add, Update, Delete).
 *        If data for a chart does not exist, update will add the data.
 * @param fileAndPath location and name of the file that contains the data.
 * @param items a list with the data to handle.
 */
data class ChartWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                             override val items: List<ChartData>): WriteRequest

/**
 * Request to read the data for one or more charts.
 * @param action the action to perform (ReadAll, ReadForId, SearchForName).
 *        ReadForChartId is only supported for events.
 * @param fileAndPath location and name of the file that contains the data.
 * @param searchId the id to search for, only used if action is ReadForId. Default 0.
 * @param searchPartOfName the string to search for in the name of the chartowner, only used if action is SearchForName.
 *        Default empty string.
 */
data class ChartReadRequest(override val action: ReadActions, override val fileAndPath: String, override val searchId: Int = 0,
                            val searchPartOfName: String = ""): ReadRequest

/**
 * Request to change the data for an event.
 * @param action the action to perform (WriteAll, Add, Update, Delete).
 *        If data for an event does not exist, update will add the data.
 * @param fileAndPath location and name of the file that contains the data.
 * @param items a list with the data to handle.
 */
data class EventWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                             override val items: List<ChartEvent>): WriteRequest

/**
 * Request to read the data for one or more events.
 * @param action the action to perform (ReadAll, ReadForId, ReadForChartId, SearchForName).
 * @param fileAndPath location and name of the file that contains the data.
 * @param searchId the id to search for, only used if action is ReadForId.
 *        If action is ReadForId the searchId is used to find the event.
 *        If action is ReadForChartId the searchId is used to find events that belong to the chart with the searchId.
 *        Default 0.
 */
data class EventReadRequest(override val action: ReadActions, override val fileAndPath: String,
                            override val searchId: Int = 0): ReadRequest

/**
 * Request to change the data for a configuration (config).
 * @param action the action to perform (WriteAll, Add, Update, Delete).
 *        If data for a config does not exist, update will add the data.
 * @param fileAndPath location and name of the file that contains the data.
 * @param items a list with the data to handle.
 */
data class ConfigWriteRequest(override val action: WriteActions, override val fileAndPath: String,
                              override val items: List<Config>): WriteRequest

/**
 * Request to read the data for one or more configurations (configs).
 * @param action the action to perform (ReadAll, ReadForId).
 *        ReadForChartId and SearchForName are not used for reading configs.
 * @param fileAndPath location and name of the file that contains the data.
 * @param searchId the id to search for, only used if action is ReadForId. Default 0.
 */
data class ConfigReadRequest(override val action: ReadActions, override val fileAndPath: String,
                             override val searchId: Int = 0): ReadRequest