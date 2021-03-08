/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.opencsv.bean.CsvToBeanBuilder
import com.opencsv.bean.StatefulBeanToCsv
import com.opencsv.bean.StatefulBeanToCsvBuilder
import com.radixpro.enigma.libbe.domain.PersistedChart
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

/**
 * Dao for charts.
 */
class ChartDao{

    /**
     * Writes all charts to a file and overwrites any previous data.
     */
    fun writeAll(fileAndPath: String, charts: List<PersistedChart>) {
        val writer: Writer = FileWriter(fileAndPath)
        val beanToCsv: StatefulBeanToCsv<PersistedChart> = StatefulBeanToCsvBuilder<PersistedChart>(writer).build()
        beanToCsv.write(charts)
        writer.close()
    }

    /**
     * Reads all charts from a file.
     */
    fun readAll(fileAndPath: String): MutableList<PersistedChart> {
        return CsvToBeanBuilder<PersistedChart>(FileReader(fileAndPath))
            .withType(PersistedChart::class.java).build().parse()
    }

    /**
     * Returns all charts that have a specific id.
     * The result is a list but should contain not more than one chart as id is unique.
     * If no chart is found the list will be empty.
     */
    fun readForId(fileAndPath: String, id: Int): List<PersistedChart> {
        val allCharts = readAll(fileAndPath)
        val searchResult = mutableListOf<PersistedChart>()
        for (chart: PersistedChart in allCharts) {
            if (chart.id == id) searchResult.add(chart)
        }
        return searchResult
    }

    /**
     * Returns charts that have a name that contains a given searchstring.
     * The searchstring can be at the start, the end or in between.
     * If no chart is found the result is an empty list.
     */
    fun searchForName(fileAndPath: String, name: String): List<PersistedChart> {
        val allCharts = readAll(fileAndPath)
        val searchResult = mutableListOf<PersistedChart>()
        for (chart: PersistedChart in allCharts) {
            if (chart.name.toLowerCase().contains(name.toLowerCase())) searchResult.add(chart)
        }
        return searchResult
    }

    /**
     * Adds a chart to a given file without deleting previous data.
     */
    fun add(fileAndPath: String, chart: PersistedChart) {
        val allCharts = readAll(fileAndPath)
        val nextId = findNextId(allCharts)
        chart.id = nextId
        allCharts.add(chart)
        writeAll(fileAndPath, allCharts)
    }

    /**
     * Updates a specific chart that has the same id as chart2Update.
     */
    fun update(fileAndPath: String, chart2Update: PersistedChart) {
        val id = chart2Update.id
        val allCharts = readAll(fileAndPath)
        val newCharts: MutableList<PersistedChart> = mutableListOf()
        for (chart: PersistedChart in allCharts) {
            if (chart.id != id) newCharts.add(chart)
        }
        newCharts.add(chart2Update)
        writeAll(fileAndPath, newCharts)
    }

    /**
     * Deletes a chart that has the same id as chart2Delete.
     */
    fun delete(fileAndPath: String, chart2Delete: PersistedChart) {
        val id = chart2Delete.id
        val allCharts = readAll(fileAndPath)
        var newCharts: MutableList<PersistedChart> = mutableListOf()
        for (chart: PersistedChart in allCharts) {
            if (chart.id != id) newCharts.add(chart)
        }
        writeAll(fileAndPath, newCharts)
    }

    /**
     * Calculates the next available id, comparable to a sequence in a RDBMS.
     */
    private fun findNextId(allCharts: List<PersistedChart>): Int {
        var nextId = 1
        for (chart: PersistedChart in allCharts) {
            if (chart.id >= nextId) nextId = chart.id+1
        }
        return nextId
    }
}

