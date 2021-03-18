/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.di.Injector

/**
 * Endpoints for astronomy related calculations.
 */
class AstronApi {

    /**
     * Calculate a simple chart.
     * @return the calculated SimpleChart.
     */
    fun calcSimpleChart(request: ChartRequest): ChartResponse {
        val simpleChartHandler = Injector.injectSimpleChartHandler()
        return simpleChartHandler.calcChartPositions(request) as ChartResponse
    }

    /**
     * Calculate a base chart.
     * @return the calculated BaseChart.
     */
    fun calcBaseChart(request: ChartRequest): ChartResponse {
        val baseChartHandler = Injector.injectBaseChartHandler()
        return baseChartHandler.calcChartPositions(request) as ChartResponse
    }

    /**
     * Calculate a full chart.
     * @return the calculated FullChart.
     */
    fun calcFullChart(request: ChartRequest): ChartResponse {
        val fullChartHandler = Injector.injectFullChartHandler()
        return fullChartHandler.calcChartPositions(request) as ChartResponse
    }

    /**
     * Calculate true Epsilon (Obliquity). True means corrected for nuation.
     * @return the calculated value for Epsilon and an error text if an error occurred.
     */
    fun calcEpsilon(request: EpsilonRequest): SingleDoubleResponse {
        val epsilonHandler = Injector.injectEpsilonHandler()
        return epsilonHandler.calcTrueEpsilon(request) as SingleDoubleResponse
    }

    /**
     * Calculate the Julian day number for UT.
     * @return the calculated value for the Julian day number and an error text if an error occurred.
     */
    fun calcJdUt(request: JdUtRequest): SingleDoubleResponse {
        val dateTimeHandler = Injector.injectDateTimeHandler()
        return dateTimeHandler.calcJdUt(request)
    }

    /**
     * Checks the validity of a date. Takes the calendar (Gregorian or Julina) into account.
     */
    fun isValidDate(request: ValidDateRequest): Boolean {
        val dateTimeHandler = Injector.injectDateTimeHandler()
        return dateTimeHandler.isValidDate(request)
    }

    /**
     * Calculates timeseries for positions.
     */
    fun calcTimeSeries(request: TimeSeriesRequest): TimeSeriesResponse {
        val timeSeriesHandler = Injector.injectTimeseriesHandler()
        return timeSeriesHandler.calcSimpleTimeSeries(request)
    }

}

/**
 * Endpoint for persistency of Chart data.
 */
class ChartPersistencyApi {

    /**
     * Reads ChartData, the request defines how the data is retrieved.
     * @return a response that gives the reslt of the reading, a boolean indicating any errors and a text with possible errors.
     */
    fun read(request: ChartReadRequest): ChartReadResponse {
        val handler = Injector.injectChartPersistencyHandler()
        return handler.read(request) as ChartReadResponse
    }

    /**
     * Changes data, the request defines how the data is changed.
     * Updating a chart that does not exist results in adding the chart.
     * Deleting a chart that does not exist results in no action and no errors.
     * @return a response that indicates the number of changes, a boolean indicating any errors and a text with possible errors.
     */
    fun write(request: ChartWriteRequest): WriteResponse {
        val handler = Injector.injectChartPersistencyHandler()
        return handler.write(request)
    }
}


/**
 * Endpoint for persistency of Events.
 */
class EventPersistencyApi {

    /**
     * Reads Events, the request defines how the data is retrieved.
     * @return a response that gives the result of the reading, a boolean indicating any errors and a text with possible errors.
     */
    fun read(request: EventReadRequest): EventReadResponse {
        val handler = Injector.injectEventPersistencyHandler()
        return handler.read(request) as EventReadResponse
    }

    /**
     * Changes data for events, the request defines how the data is changed.
     * Updating an event that does not exist results in adding the event.
     * Deleting an event that does not exist results in no action and no errors.
     * @return a response that indicates the number of changes, a boolean indicating any errors and a text with possible errors.
     */
    fun write(request: EventWriteRequest): WriteResponse {
        val handler = Injector.injectEventPersistencyHandler()
        return handler.write(request)
    }


}



/**
 * Endpoint for persistency of Configs.
 */
class ConfigPersistencyApi {

    /**
     * Reads Configs, the request defines how the data is retrieved.
     * @return a response that gives the result of the reading, a boolean indicating any errors and a text with possible errors.
     */
    fun read(request: ConfigReadRequest): ConfigReadResponse {
        val handler = Injector.injectConfigPersistencyHandler()
        return handler.read(request) as ConfigReadResponse
    }

    /**
     * Changes data for configs, the request defines how the data is changed.
     * Updating a config that does not exist results in adding the config.
     * Deleting a config that does not exist results in no action and no errors.
     * @return a response that indicates the number of changes, a boolean indicating any errors and a text with possible errors.
     */
    fun write(request: ConfigWriteRequest): WriteResponse {
        val handler = Injector.injectConfigPersistencyHandler()
        return handler.write(request)
    }


}