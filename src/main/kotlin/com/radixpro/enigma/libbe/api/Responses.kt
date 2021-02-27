/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.BaseChartPositions

data class EpsilonResponse(val position: Double, val errorTxt: String)

data class JdResponse(val jdNumber: Double, val errorTxt: String)

data class BaseChartResponse(val baseChartPositions: BaseChartPositions, val errorTxt: String)