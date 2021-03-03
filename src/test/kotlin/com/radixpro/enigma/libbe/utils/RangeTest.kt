/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.utils

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RangeTest {

    private val margin = 0.00000001

    @Test
    fun `Converting position to a range, using defaults, should give correct results`() {
        val inputValue = 400.0
        Range.checkValue(inputValue) shouldBe (40.0 plusOrMinus margin)
    }

    @Test
    fun `Converting position to a range, using min and max values, should give correct results`() {
        val inputValue = 50.0
        val minValue = 5.0
        val maxValue = 25.0
        Range.checkValue(inputValue, minValue, maxValue) shouldBe (10.0 plusOrMinus margin)
    }

    @Test
    fun `Converting position to a range, with min and max in wrong sequence, should give correct results`() {
        val inputValue = 50.0
        val minValue = 25.0
        val maxValue = 5.0
        Range.checkValue(inputValue, minValue, maxValue) shouldBe (10.0 plusOrMinus margin)
    }

    @Test
    fun `Converting position, equal to maxValue, to a range should give minValue as result`() {
        val inputValue = 25.0
        val minValue = 5.0
        val maxValue = 25.0
        Range.checkValue(inputValue, minValue, maxValue) shouldBe (5.0 plusOrMinus margin)
    }


}