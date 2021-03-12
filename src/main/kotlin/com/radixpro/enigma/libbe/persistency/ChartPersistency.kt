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
import com.radixpro.enigma.libbe.domain.Persistable
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

/**
 * Dao for charts.
 */
class ChartDao: Dao() {

    /**
     * Writes all charts to a file and overwrites any previous data.
     */
    override fun writeAll(fileAndPath: String, items: List<Persistable>) {
        val writer: Writer = FileWriter(fileAndPath)
        val beanToCsv: StatefulBeanToCsv<ChartData> = StatefulBeanToCsvBuilder<ChartData>(writer).build()
        beanToCsv.write(items as List<ChartData>)
        writer.close()
    }

    /**
     * Reads all charts from a file.
     */
    override fun readAll(fileAndPath: String): MutableList<Persistable> {
        return if (File(fileAndPath).exists()) CsvToBeanBuilder<ChartData>(FileReader(fileAndPath))
                .withType(ChartData::class.java).build().parse() as MutableList<Persistable>
        else mutableListOf()
    }

    /**
     * Returns charts that have a name that contains a given searchstring.
     * The searchstring can be at the start, the end or in between.
     * If no chart is found the result is an empty list.
     */
    fun searchForName(fileAndPath: String, name: String): List<ChartData> {
        val allCharts = readAll(fileAndPath) as List<ChartData>
        val searchResult = mutableListOf<ChartData>()
        for (chartData: ChartData in allCharts) {
            if (chartData.name.toLowerCase().contains(name.toLowerCase())) searchResult.add(chartData)
        }
        return searchResult
    }

    override fun defineItemType(item: Persistable): Persistable {
        return item as ChartData
    }

}

