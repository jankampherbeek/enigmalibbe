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
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import swisseph.SwissEph

private const val margin = 0.00000001

internal class EpsilonTest {

    @Test
    fun `calculation of true obliquity shoud return correct result`() {
        val swissEph = SwissEph()
        val epsilon = Epsilon(swissEph)
        val jdUt = 2434406.817713       // 1953-1-29 UT 7:37
        val expected = 23.4470723027
        epsilon.calcTrueEpsilon(jdUt).first shouldBe(expected plusOrMinus margin)
        epsilon.calcTrueEpsilon(jdUt).second shouldBe("")
    }

}