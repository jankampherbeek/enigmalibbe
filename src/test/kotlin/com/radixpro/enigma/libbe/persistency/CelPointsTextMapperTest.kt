/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.di.Injector
import com.radixpro.enigma.libbe.domain.CelPoints
import com.radixpro.enigma.libbe.domain.PersistedCelPoint
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CelPointsTextMapperTest {

    private val celPointText = "SUN;true;a|MOON;true;b|MERCURY;true;c"
    private val persCelPoints = listOf(PersistedCelPoint(CelPoints.SUN, true, "a"),
        PersistedCelPoint(CelPoints.MOON, true, "b"),
        PersistedCelPoint(CelPoints.MERCURY, true, "c"))
    private val mapper = Injector.injectCelPointsTextMapper()

    @Test
    fun `Converting a list of PersistedCelpoints to a text for csv should give the correct result`() {
        mapper.createText(persCelPoints) shouldBe celPointText
    }

    @Test
    fun `Converting a csv text to a list of PersistedCelpoints should gieve the correct result`() {
        mapper.createCelPoints(celPointText) shouldBe persCelPoints
    }
}