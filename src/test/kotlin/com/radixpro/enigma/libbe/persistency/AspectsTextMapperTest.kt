/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.di.Injector
import com.radixpro.enigma.libbe.domain.Aspects
import com.radixpro.enigma.libbe.domain.PersistedAspect
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AspectsTextMapperTest {

    private val aspectText = "CONJUNCTION;true;B|OPPOSITION;true;C|TRIANGLE;true;D"
    private val persAspects = listOf(PersistedAspect(Aspects.CONJUNCTION, true, "B"),
        PersistedAspect(Aspects.OPPOSITION, true, "C"),
        PersistedAspect(Aspects.TRIANGLE, true, "D"))
    private val mapper = Injector.injectAspectsTextMapper()

    @Test
    fun `Converting a list of PersistedAspects to a text for csv should give the correct result`() {
        mapper.createText(persAspects) shouldBe aspectText
    }

    @Test
    fun `Converting a csv text to a list of PersistedAspects should gieve the correct result`() {
        mapper.createAspects(aspectText) shouldBe persAspects
    }
}