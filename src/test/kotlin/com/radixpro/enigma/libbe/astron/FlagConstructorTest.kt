/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import com.radixpro.enigma.libbe.domain.Coordinates
import com.radixpro.enigma.libbe.domain.ObserverPos
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FlagConstructorTest {

    @Test
    fun `Flagconstructor should return the expected flags for default values`() {
        val observerPos = ObserverPos.GEOCENTRIC
        val coordinates = Coordinates.ECLIPTICAL
        FlagConstructor.defineFlags(observerPos, coordinates) shouldBe (2L or 256L).toInt()
    }

    @Test
    fun `Flagconstructor should return the expected flags for equatorial coordinates`() {
        val observerPos = ObserverPos.GEOCENTRIC
        val coordinates = Coordinates.EQUATORIAL
        FlagConstructor.defineFlags(observerPos, coordinates) shouldBe (2L or 256L or 2048L).toInt()
    }

    @Test
    fun `Flagconstructor should return the expected flags for topocentric observerpos`() {
        val observerPos = ObserverPos.TOPOCENTRIC
        val coordinates = Coordinates.ECLIPTICAL
        FlagConstructor.defineFlags(observerPos, coordinates) shouldBe (2L or 256L or 32*1024L).toInt()
    }

}