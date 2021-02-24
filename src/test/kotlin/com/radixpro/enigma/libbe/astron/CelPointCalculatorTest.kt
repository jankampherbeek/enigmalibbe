/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import swisseph.SwissEph

private const val margin = 0.00000001

internal class CelPointCalculatorTest {

    private lateinit var calculator: CelPointCalculator

    @BeforeEach
    fun setUp() {
        calculator = CelPointCalculator(SwissEph())
    }

    @Test
    fun `Calculating the longitudional positions and speeds for the Sun should give the correct results`() {
        val jdUt = 2434406.817713       // 1953-1-29 UT 7:37
        val flags = 2L or 256L // Use Swiss Eph and speed
        val id = 0
        val expectedLon = 309.11851554
        val expectedLat = 0.00003117191
        val expectedRadv = 0.9850163594
        val expectedLonSpeed = 1.0153008804
        val expectedLatSpeed = -0.0000496490
        val expectedRadvSpeed = 0.0001299518
        val results = calculator.calcMainPositionsForCelPoint(jdUt, id, flags.toInt())
        results.first[0] shouldBe (expectedLon plusOrMinus margin)
        results.first[1] shouldBe (expectedLat plusOrMinus margin)
        results.first[2] shouldBe (expectedRadv plusOrMinus margin)
        results.first[3] shouldBe (expectedLonSpeed plusOrMinus margin)
        results.first[4] shouldBe (expectedLatSpeed plusOrMinus margin)
        results.first[5] shouldBe (expectedRadvSpeed plusOrMinus margin)
        results.second shouldContain "Moshier"
    }
}