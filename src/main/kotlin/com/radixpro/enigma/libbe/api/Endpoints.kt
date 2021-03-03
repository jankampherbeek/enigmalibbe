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
     * Calculate a base chart.
     * @return the calculated BaseChart.
     */
    fun calcBaseChart(request: BaseChartRequest): BaseChartResponse {
        val baseChartHandler = Injector.injectBaseChartHandler()
        return baseChartHandler.calcBaseChartPositions(request) as BaseChartResponse
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