/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.di.Injector
import com.radixpro.enigma.libbe.domain.*

/**
 * Endpoints for astronomy related calculations.
 */
class AstronApi {

    /**
     * Calculate a base chart.
     * @param jdUt: julian day number for UT.
     * @param celPoints: the celestial points to calculate.
     * @param houseSystem: the housesystem to use.
     * @param location: the geographical location.
     * @return the calculated BaseChart.
     */
    fun calcBaseChart(jdUt: Double,
                      celPoints: List<CelPoints>,
                      houseSystem: HouseSystems,
                      location: Location): BaseChartResponse {
        val baseChartHandler = Injector.injectBaseChartHandler()
        return baseChartHandler.calcBaseChartPositions(jdUt, celPoints, houseSystem, location)
    }

    /**
     * Calculate true Epsilon (Obliquity). True means corrected for nuation.
     * @param jdUt: julian day number for UT.
     * @return the calculated value for Epsilon and an error text if an error occurred.
     */
    fun calcEpsilon(jdUt: Double): EpsilonResponse {
        val epsilonHandler = Injector.injectEpsilonHandler()
        return epsilonHandler.calcTrueEpsilon(jdUt)
    }

    /**
     * Calculate the Julian day number for UT.
     * @param dateTimeParts: the date and time.
     * @return the calculated value for the Julian day number and an error text if an error occurred.
     */
    fun calcJdUt(dateTimeParts: DateTimeParts): JdResponse {
        val dateTimeHandler = Injector.injectDateTimeHandler()
        return dateTimeHandler.calcJdUt(dateTimeParts)
    }

    /**
     * Checks the validty of a date. Takes the calendar (Gregorian or Julina) into account.
     * @param year: the year (astronomical, so BCE 1 is entered as 0 and BCE 2 is entered as -1 etc.
     */
    fun isValidDate(year:Int, month: Int, day: Int, gregorian: Boolean): Boolean {
        val dateTimeHandler = Injector.injectDateTimeHandler()
        return dateTimeHandler.isValidDate(year, month, day, gregorian)
    }

}