/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.di

import com.radixpro.enigma.libbe.astron.CelPointCalculator
import com.radixpro.enigma.libbe.astron.Epsilon
import com.radixpro.enigma.libbe.astron.HousesCalculator
import com.radixpro.enigma.libbe.astron.JulianDayNr
import com.radixpro.enigma.libbe.handlers.BaseChartHandler
import com.radixpro.enigma.libbe.handlers.DateTimeHandler
import com.radixpro.enigma.libbe.handlers.EpsilonHandler
import com.radixpro.enigma.libbe.handlers.TimeSeriesHandler
import swisseph.SwissEph

/**
 * Handles dependency injection.
 */
object Injector {

    private fun injectSwissEph(): SwissEph {
        val path = "./se"
        return SwissEph(path)
    }

    private fun injectEpsilon(): Epsilon {
        return Epsilon(injectSwissEph())
    }

    private fun injectJulianDayNr(): JulianDayNr{
        return JulianDayNr()
    }

    private fun injectCelPointCalculator(): CelPointCalculator {
        return CelPointCalculator(injectSwissEph())
    }

    private fun injectHousesCalculator(): HousesCalculator {
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

    fun injectTimeseriesHandler(): TimeSeriesHandler {
        return TimeSeriesHandler(injectCelPointCalculator())
    }

}

