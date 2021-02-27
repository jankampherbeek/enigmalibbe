/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.di

import com.radixpro.enigma.libbe.astron.*
import com.radixpro.enigma.libbe.handlers.BaseChartHandler
import com.radixpro.enigma.libbe.handlers.DateTimeHandler
import com.radixpro.enigma.libbe.handlers.EpsilonHandler
import swisseph.SwissEph

/**
 * Handles dependency injection.
 */
object Injector {

    fun injectSwissEph(): SwissEph {
        val path = "./se"
        return SwissEph(path)
    }

    fun injectEpsilon(): Epsilon {
        return Epsilon(injectSwissEph())
    }

    fun injectJulianDayNr(): JulianDayNr{
        return JulianDayNr()
    }

    fun injectCelPointCalculator(): CelPointCalculator {
        return CelPointCalculator(injectSwissEph())
    }

    fun injectHousesCalculator(): HousesCalculator {
        return HousesCalculator(injectSwissEph())
    }

    fun injectBaseChartHandler(): BaseChartHandler {
        return BaseChartHandler(injectCelPointCalculator(), injectHousesCalculator(), injectEpsilon())
    }

    fun injectEpsilonHandler(): EpsilonHandler {
        return EpsilonHandler(injectEpsilon())
    }

    fun injectDateTimeHandler(): DateTimeHandler {
        return DateTimeHandler(injectJulianDayNr())
    }

}

