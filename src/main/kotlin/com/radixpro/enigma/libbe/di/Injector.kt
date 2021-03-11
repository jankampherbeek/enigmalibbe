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
import com.radixpro.enigma.libbe.handlers.*
import com.radixpro.enigma.libbe.persistency.*
import swisseph.SwissEph

/**
 * Handles dependency injection.
 */
object Injector {

    fun injectAspectsTextMapper(): AspectsTextMapper {
        return AspectsTextMapper()
    }

    fun injectBaseChartHandler(): BaseChartHandler {
        return BaseChartHandler(injectCelPointCalculator(), injectHousesCalculator(), injectEpsilon())
    }

    private fun injectCelPointCalculator(): CelPointCalculator {
        return CelPointCalculator(injectSwissEph())
    }

    fun injectCelPointsTextMapper(): CelPointsTextMapper {
        return CelPointsTextMapper()
    }

    fun injectChartDao(): ChartDao {
        return ChartDao()
    }

    fun injectChartPersistencyHandler(): ChartPersistencyHandler {
        return ChartPersistencyHandler(injectChartDao())
    }

    fun injectConfigDao(): ConfigDao {
        return ConfigDao(injectConfigMapper())
    }

    private fun injectConfigMapper(): ConfigMapper {
        return ConfigMapper(injectCelPointsTextMapper(), injectAspectsTextMapper())
    }

    fun injectConfigPersistencyHandler(): ConfigPersistencyHandler {
        return ConfigPersistencyHandler(injectConfigDao())
    }

    fun injectDateTimeHandler(): DateTimeHandler {
        return DateTimeHandler(injectJulianDayNr())
    }

    private fun injectEpsilon(): Epsilon {
        return Epsilon(injectSwissEph())
    }

    fun injectEpsilonHandler(): EpsilonHandler {
        return EpsilonHandler(injectEpsilon())
    }

    fun injectEventDao(): EventDao {
        return EventDao()
    }

    fun injectEventPersistencyHandler(): EventPersistencyHandler {
        return EventPersistencyHandler(injectEventDao())
    }

    private fun injectHousesCalculator(): HousesCalculator {
        return HousesCalculator(injectSwissEph())
    }

    private fun injectJulianDayNr(): JulianDayNr{
        return JulianDayNr()
    }

    private fun injectSwissEph(): SwissEph {
        val path = "./se"
        return SwissEph(path)
    }

    fun injectTimeseriesHandler(): TimeSeriesHandler {
        return TimeSeriesHandler(injectCelPointCalculator())
    }

}

