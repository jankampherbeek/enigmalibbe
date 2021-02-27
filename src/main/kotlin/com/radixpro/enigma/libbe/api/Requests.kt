/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.CelPoints
import com.radixpro.enigma.libbe.domain.DateTimeParts
import com.radixpro.enigma.libbe.domain.HouseSystems
import com.radixpro.enigma.libbe.domain.Location

interface Request


data class EpsilonRequest(val jdUt: Double): Request

data class JdUtRequest(val dateTimeParts: DateTimeParts): Request

data class ValidDateRequest(val year:Int, val month: Int, val day: Int, val gregorian: Boolean): Request

data class BaseChartRequest(val jdUt: Double, val celPoints: List<CelPoints>, val houseSystem: HouseSystems,
                            val location: Location): Request
