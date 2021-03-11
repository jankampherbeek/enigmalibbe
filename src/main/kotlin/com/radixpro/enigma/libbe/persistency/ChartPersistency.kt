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
import com.radixpro.enigma.libbe.domain.ChartData
import java.io.File
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
    fun writeAll(fileAndPath: String, chartData: List<ChartData>) {
        val writer: Writer = FileWriter(fileAndPath)
        val beanToCsv: StatefulBeanToCsv<ChartData> = StatefulBeanToCsvBuilder<ChartData>(writer).build()
        beanToCsv.write(chartData)
        writer.close()
    }

    /**
     * Reads all charts from a file.
     */
    fun readAll(fileAndPath: String): MutableList<ChartData> {
        return if (File(fileAndPath).exists()) CsvToBeanBuilder<ChartData>(FileReader(fileAndPath))
            .withType(ChartData::class.java).build().parse()
        else mutableListOf()
    }

    /**
     * Returns all charts that have a specific id.
     * The result is a list but should contain not more than one chart as id is unique.
     * If no chart is found the list will be empty.
     */
    fun readForId(fileAndPath: String, id: Int): List<ChartData> {
        val allCharts = readAll(fileAndPath)
        val searchResult = mutableListOf<ChartData>()
        for (chartData: ChartData in allCharts) {
            if (chartData.id == id) searchResult.add(chartData)
        }
        return searchResult
    }

    /**
     * Returns charts that have a name that contains a given searchstring.
     * The searchstring can be at the start, the end or in between.
     * If no chart is found the result is an empty list.
     */
    fun searchForName(fileAndPath: String, name: String): List<ChartData> {
        val allCharts = readAll(fileAndPath)
        val searchResult = mutableListOf<ChartData>()
        for (chartData: ChartData in allCharts) {
            if (chartData.name.toLowerCase().contains(name.toLowerCase())) searchResult.add(chartData)
        }
        return searchResult
    }

    /**
     * Adds a chart to a given file without deleting previous data.
     */
    fun add(fileAndPath: String, chartData: ChartData) {
        val allCharts = readAll(fileAndPath)
        val nextId = findNextId(allCharts)
        chartData.id = nextId
        allCharts.add(chartData)
        writeAll(fileAndPath, allCharts)
    }

    /**
     * Updates a specific chart that has the same id as chart2Update.
     */
    fun update(fileAndPath: String, chartData2Update: ChartData) {
        val id = chartData2Update.id
        val allCharts = readAll(fileAndPath)
        val newChartData: MutableList<ChartData> = mutableListOf()
        for (chartData: ChartData in allCharts) {
            if (chartData.id != id) newChartData.add(chartData)
        }
        newChartData.add(chartData2Update)
        writeAll(fileAndPath, newChartData)
    }

    /**
     * Deletes a chart that has the same id as chart2Delete.
     */
    fun delete(fileAndPath: String, chartData2Delete: ChartData) {
        val id = chartData2Delete.id
        val allCharts = readAll(fileAndPath)
        val newChartData: MutableList<ChartData> = mutableListOf()
        for (chartData: ChartData in allCharts) {
            if (chartData.id != id) newChartData.add(chartData)
        }
        writeAll(fileAndPath, newChartData)
    }

    /**
     * Calculates the next available id, comparable to a sequence in a RDBMS.
     */
    private fun findNextId(allChartData: List<ChartData>): Int {
        var nextId = 1
        for (chartData: ChartData in allChartData) {
            if (chartData.id >= nextId) nextId = chartData.id+1
        }
        return nextId
    }
}

