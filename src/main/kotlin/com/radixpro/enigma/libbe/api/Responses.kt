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

interface ReadResponse {
    val result: List<Persistable>
    val errors: Boolean
    val comments: String
}

/**
 * Response for a single position.
 */
data class SingleDoubleResponse(
    override val result: Double,
    override val errors: Boolean,
    override val comments: String
) : Response

/**
 * Response for a calculated Base Chart.
 */
data class ChartResponse(
    override val result: ChartPositions,
    override val errors: Boolean,
    override val comments: String
) : Response

/**
 * Response for a Time Series of positions.
 */
data class TimeSeriesResponse(
    override val result: List<TimeSeriesValues>,
    override val errors: Boolean,
    override val comments: String
) : Response

/**
 * Generic response for writing/changing data.
 */
data class WriteResponse(
    override val result: Int,
    override val errors: Boolean,
    override val comments: String
) : Response

/**
 * Response for readng one or more charts.
 */
data class ChartReadResponse(
    override val result: List<ChartData>,
    override val errors: Boolean,
    override val comments: String
) : ReadResponse

/**
 * Response for reading one or more events.
 */
data class EventReadResponse(
    override val result: List<ChartEvent>,
    override val errors: Boolean,
    override val comments: String
) : ReadResponse

/**
 * Response for reading one or more configurations.
 */
data class ConfigReadResponse(
    override val result: List<Config>,
    override val errors: Boolean,
    override val comments: String
) : ReadResponse

