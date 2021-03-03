/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.*
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldHaveLength
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
        val request = BaseChartRequest(jdUt, celPoints, HouseSystems.PLACIDUS, location)
        val response = api.calcBaseChart(request)
        response.comments shouldHaveLength 0
        response.errors shouldBe false
        val celPositions = response.result.celPositions
        celPositions[0].celPoint shouldBe CelPoints.SUN
        celPositions[0].eclCoord.position shouldBe (309.1181602720 plusOrMinus margin)  //309.11816027204895
        celPositions[0].eclSpeed.position shouldBe (1.0153031475 plusOrMinus margin)
    }

    @Test
    fun `IT for calculating Epsilon should return correct result`() {
        val jdUt = 2434406.817713       // 1953-1-29 UT 7:37
        val expected = 23.4470723027
        val response = api.calcEpsilon(EpsilonRequest(jdUt))
        response.comments shouldHaveLength 0
        response.errors shouldBe false
        response.result shouldBe (expected plusOrMinus margin)
    }

    @Test
    fun `IT for calculating Julian Day Nr should give correct result`() {
        val request = JdUtRequest(DateTimeParts(1953,1,29,7,37,0,0.0, true))
        val expected = 2434406.8173611113
        val response = api.calcJdUt(request)
        response.comments shouldHaveLength 0
        response.errors shouldBe false
        response.result shouldBe (expected plusOrMinus margin)

    }

    @Test
    fun `IT for validating a correct date should return true`() {
        val request = ValidDateRequest(1953, 1, 29, true)
        api.isValidDate(request) shouldBe true
    }

    @Test
    fun `IT for validating an incorrect date should return false`() {
        val request = ValidDateRequest(1953, 2, 29, true)
        api.isValidDate(request) shouldBe false
    }

    @Test
    fun `IT for calculating a TimeSeries of positions should return the correct results`() {
        val startJd = 2434404.8173611113
        val location = Location(52.216666666667, 6.54)
        val celPoints = listOf(CelPoints.SUN, CelPoints.MOON, CelPoints.MERCURY)
        val observerPos = ObserverPos.GEOCENTRIC
        val coordinates = Coordinates.ECLIPTICAL
        val interval = 1.0
        val repeats = 10
        val request = TimeSeriesRequest(celPoints, observerPos, coordinates, startJd, location, interval, repeats)
        val timeSeriesResult = api.calcTimeSeries(request)
        timeSeriesResult.errors shouldBe false
        timeSeriesResult.comments shouldHaveLength 0
        timeSeriesResult.result[0].timePositions.size shouldBe 10
        // positions for 2 days after start
        val timeSeries = timeSeriesResult.result
        timeSeries[0].celPoint shouldBe CelPoints.SUN
        timeSeries[0].timePositions[2].first shouldBe (2434406.81736111113 plusOrMinus margin)
        timeSeries[0].timePositions[2].second shouldBe (309.1181602720 plusOrMinus margin)

    }


}