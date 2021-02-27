/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import com.radixpro.enigma.libbe.domain.DateTimeParts
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val margin = 0.00000001

internal class JulianDayNrTest {

    private lateinit var jdnr: JulianDayNr

    @BeforeEach
    fun startUp() {
        jdnr = JulianDayNr()
    }

    @Test
    fun `calculation of Julian Day nr for a given date and time should return the correct result`() {
        val dateTimeParts = DateTimeParts(1953,1,29,7,30,0,0.0, true)
        val expected = 2434406.8125
        jdnr.calculateJdUt(dateTimeParts) shouldBe (expected plusOrMinus margin)
    }

    @Test
    fun `Validation for a valid date should return true`() {
        jdnr.isValidDate(1953,1,29, true) shouldBe true
    }

    @Test
    fun `Validation for an erroneous data should return false`() {
        jdnr.isValidDate(1953, 2, 29, true) shouldBe false
    }
}