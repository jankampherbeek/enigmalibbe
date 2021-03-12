/*
 * Jan Kampherbeek, (c) 2021.
 * EnigmaLibBe is open source.
 * Please check the file copyright.txt in the root of the source for further details.
 */
package com.radixpro.enigma.libbe.math

import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class TrigonometryTest {

    private val delta = 0.00000001

    @Test
    fun `Sine of positive value should return correct result`() {
        Trigonometry.sin(12.0) shouldBe (0.207911690817759 plusOrMinus delta)
    }

    @Test
    fun `Sine of negative value should return correct result`() {
        Trigonometry.sin(-12.0) shouldBe (-0.207911690817759 plusOrMinus delta)
    }

    @Test
    fun `Sine for zero should return zero`() {
        Trigonometry.sin(0.0) shouldBe (0.0 plusOrMinus delta)
    }

    @Test
    fun `Sine for 90 degrees should return 1`() {
        Trigonometry.sin(90.0) shouldBe (1.0 plusOrMinus delta)
    }

    @Test
    fun `Cosine for a positive value should return correct result`() {
        Trigonometry.cos(16.0) shouldBe (0.96126169593831886 plusOrMinus delta)
    }


    @Test
    fun `Cosine for a negative value should return correct result`() {
        Trigonometry.cos(-16.0) shouldBe (0.96126169593831886 plusOrMinus delta)
    }

    @Test
    fun `Cosine for zero should return 1`() {
        Trigonometry.cos(0.0) shouldBe (1.0 plusOrMinus delta)
    }

    @Test
    fun `Cosine for 90 should return zero`() {
        Trigonometry.cos(90.0) shouldBe (0.0 plusOrMinus delta)
    }

    @Test
    fun `Tangent for positive value should return correct result`() {
        Trigonometry.tan(5.0) shouldBe (0.08748866352592 plusOrMinus delta)
    }

    @Test
    fun `Tangent for negative value should return correct result`() {
        Trigonometry.tan(-5.0) shouldBe (-0.08748866352592 plusOrMinus delta)
    }

    @Test
    fun `Tangent for zero should return zero`() {
        Trigonometry.tan(0.0) shouldBe (0.0 plusOrMinus delta)
    }

    @Test
    fun `Arcsine for a positive value should return correct result`() {
        Trigonometry.asin(0.207911690817759) shouldBe (12.0 plusOrMinus delta)
    }

    @Test
    fun `Arcsine for a negative value should return correct result`() {
        Trigonometry.asin(-0.207911690817759) shouldBe (-12.0 plusOrMinus delta)
    }

    @Test
    fun `Arcsine for zero should return zero`() {
        Trigonometry.asin(0.0) shouldBe (0.0 plusOrMinus delta)
    }

    @Test
    fun `Arcsine for 1 should return 90`() {
        Trigonometry.asin(1.0) shouldBe (90.0 plusOrMinus delta)
    }

    @Test
    fun `Arccosine for a positive value should return correct result`() {
        Trigonometry.acos(0.96126169593831886) shouldBe (16.0 plusOrMinus delta)
    }

    @Test
    fun `Arccosine for a negative value should return correct result`() {
        Trigonometry.acos(-0.96126169593831886) shouldBe (164.0 plusOrMinus delta)
    }

    @Test
    fun `Arccosine for zero should return 90`() {
        Trigonometry.acos(0.0) shouldBe (90.0 plusOrMinus delta)
    }

    @Test
    fun `Arccosine for 90 should return NaN`() {
        Trigonometry.acos(90.0) shouldBe Double.NaN
    }

    @Test
    fun `Arctangent for a positive value should return correct result`() {
        Trigonometry.atan(0.08748866352592) shouldBe (5.0 plusOrMinus delta)
    }

    @Test
    fun `Arctangent for a negative value should return correct result`() {
        Trigonometry.atan(-0.08748866352592) shouldBe (-5.0 plusOrMinus delta)
    }

    @Test
    fun `Arctangent for zero should return zero`() {
        Trigonometry.atan(0.0) shouldBe (0.0 plusOrMinus delta)
    }

    @Test
    fun `Atan2 should return currect result`() {
        Trigonometry.atan2(5.0, 2.0) shouldBe (Trigonometry.atan(2.5) plusOrMinus delta)
    }

}
