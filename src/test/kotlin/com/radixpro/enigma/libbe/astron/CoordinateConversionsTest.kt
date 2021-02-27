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

internal class CoordinateConversionsTest {

    private val margin = 0.00000001

    @Test
    fun `Converting longitude and latitude to ra and declination should give the correct results`() {
        val lon = 356.009444444      // 26 00 34 PI  lat 0 24 46   ra 356 10 26  decl -1 12 28
        val lat = 0.412777778
        val eclValues = doubleArrayOf(lon, lat)
        val epsilon = 23.4470723027
        val expectedRA = 356.1740708048
        val expectedDecl = -1.2079232912
        val result = CoordinateConversions.eclipticToEquatorial(eclValues, epsilon)
        result[0] shouldBe (expectedRA plusOrMinus margin)
        result[1] shouldBe (expectedDecl plusOrMinus margin)
    }

    @Test
    fun `Converting ra to longitude should give the correct result`() {
        // ra 249 24 35   lon 10 58 58 SA   decl -22 05 51
        val ra = 249.409722222
        val epsilon = 23.4470723027
        val expectedLon = 250.9829314011
        val result = CoordinateConversions.rightAscToLongitude(ra, epsilon)
        result shouldBe (expectedLon plusOrMinus margin)
    }

    @Test
    fun `Converting longitude using zero latitude to declination should give the correct result`() {
        val lon = 250.9829314011
        val epsilon = 23.4470723027
        val expectedDecl = -22.0975652509
        val result = CoordinateConversions.longitudeToDeclination(lon, epsilon)
        result shouldBe (expectedDecl plusOrMinus margin)
    }
}