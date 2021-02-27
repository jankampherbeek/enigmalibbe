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
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

/**
 * No mocking or stubbing. These tests are actually integration tests.
 */
internal class AstronApiTest {

    private val margin = 0.00000001
    private lateinit var api: AstronApi

    @BeforeEach
    fun setUp() {
        api = AstronApi()
    }

    @Test
    fun `IT for calculating a BaseChart should return correct positions`() {
        val jdUt = 2434406.8173611113
        val location = Location(52.216666666667, 6.54)
        val celPoints = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY)
        val houseSystem = HouseSystems.PLACIDUS
        val result = api.calcBaseChart(jdUt, celPoints, houseSystem, location)
        val errorTxt = result.errorTxt
        errorTxt shouldHaveLength 0
        val celPositions = result.baseChartPositions.celPositions
        celPositions[0].celPoint shouldBe CelPoints.SUN
        celPositions[0].eclCoord.position shouldBe (309.1181602720 plusOrMinus margin)  //309.11816027204895
        celPositions[0].eclSpeed.position shouldBe (1.0153031475 plusOrMinus margin)
    }

    @Test
    fun `IT for calculating Epsilon should return correct result`() {
        val jdUt = 2434406.817713       // 1953-1-29 UT 7:37
        val expected = 23.4470723027
        val result = api.calcEpsilon(jdUt)
        result.position shouldBe (expected plusOrMinus margin)
        result.errorTxt shouldHaveLength 0
    }

    @Test
    fun `IT for calculating Julian Day Nr should give correct result`() {
        val dateTimeParts = DateTimeParts(1953,1,29,7,37,0,0.0, true)
        val expected = 2434406.8173611113
        val result = api.calcJdUt(dateTimeParts)
        result.jdNumber shouldBe (expected plusOrMinus margin)
        result.errorTxt shouldHaveLength 0
    }

    @Test
    fun `IT for validating a correct date should return true`() {
        api.isValidDate(1953, 1, 29, true) shouldBe true
    }

    @Test
    fun `IT for validating an incorrect date should return false`() {
        api.isValidDate(1953, 2, 29, true) shouldBe false
    }



}