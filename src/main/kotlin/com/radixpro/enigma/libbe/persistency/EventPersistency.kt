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
import com.radixpro.enigma.libbe.domain.ChartEvent
import com.radixpro.enigma.libbe.domain.Persistable
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class EventDao: Dao(){

    /**
     * Writes all events to a file and overwrites any previous data.
     */
    override fun writeAll(fileAndPath: String, events: List<Persistable>) {
        val writer: Writer = FileWriter(fileAndPath)
        val beanToCsv: StatefulBeanToCsv<ChartEvent> = StatefulBeanToCsvBuilder<ChartEvent>(writer).build()
        beanToCsv.write(events as List<ChartEvent>)
        writer.close()
    }

    /**
     * Reads all events from a file.
     */
    override fun readAll(fileAndPath: String): MutableList<Persistable> {
        return if (File(fileAndPath).exists()) CsvToBeanBuilder<ChartEvent>(FileReader(fileAndPath))
            .withType(ChartEvent::class.java).build().parse() as MutableList<Persistable>
        else mutableListOf()
    }

    /**
     * Returns all events that have a specific chartId.
     * If no event is found the list will be empty.
     */
    fun readForChartId(fileAndPath: String, chartId: Int): List<ChartEvent> {
        val allEvents = readAll(fileAndPath) as List<ChartEvent>
        val searchResult = mutableListOf<ChartEvent>()
        for (event: ChartEvent in allEvents) {
            if (event.chartId == chartId) searchResult.add(event)
        }
        return searchResult
    }


    override fun defineItemType(item: Persistable): Persistable {
        return item as ChartEvent
    }




}