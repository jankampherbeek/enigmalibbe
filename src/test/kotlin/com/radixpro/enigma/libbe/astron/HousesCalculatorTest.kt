/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import com.radixpro.enigma.libbe.domain.Location
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import swisseph.SwissEph

private const val margin = 0.00000001

internal class HousesCalculatorTest {

    private lateinit var housesCalculator: HousesCalculator

    @BeforeEach
    fun setUp() {
        housesCalculator = HousesCalculator(SwissEph())
    }

    @Test
    fun getPositionsForHouses() {
        val jdUt = 2434406.817713       // 1953-1-29 UT 7:37
        val flags = 2L or 256L // Use Swiss Eph
        val location = Location(52.21666667 ,6.9, )
        val nrOfCusps = 12
        val houseSystem = 'p'
        val expected = 314.7241118009
        val results = housesCalculator.calcPositionsForHouses(jdUt, flags.toInt(), location, houseSystem.toInt(), nrOfCusps)
        results.first[0] shouldBe (expected plusOrMinus margin)
        results.second[1] shouldBe (expected plusOrMinus margin)
    }
}