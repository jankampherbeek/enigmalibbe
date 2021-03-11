/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.*

interface Response {
    val result: Any
    val errors: Boolean
    val comments: String
}

/**
 * Response for a single position.
 */
data class SingleDoubleResponse(override val result: Double,
                           override val errors: Boolean,
                           override val comments: String): Response

/**
 * Response for a calculated Base Chart.
 */
data class BaseChartResponse(override val result: BaseChartPositions,
                             override val errors: Boolean,
                             override val comments: String): Response

/**
 * Response for a Time Series of positions.
 */
data class TimeSeriesResponse(override val result: List<TimeSeriesValues>,
                              override val errors: Boolean,
                              override val comments: String): Response

data class WriteResponse(override val result: Int,
                         override val errors: Boolean,
                         override val comments: String): Response

data class ChartReadResponse(override val result: List<ChartData>,
                             override val errors: Boolean,
                             override val comments: String): Response

data class EventReadResponse(override val result: List<ChartEvent>,
                             override val errors: Boolean,
                             override val comments: String): Response

data class ConfigReadResponse(override val result: List<Config>,
                              override val errors: Boolean,
                              override val comments: String): Response


