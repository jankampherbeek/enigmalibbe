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
import com.radixpro.enigma.libbe.domain.PersistedEvent
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class EventDao {

    /**
     * Writes all events to a file and overwrites any previous data.
     */
    fun writeAll(fileAndPath: String, events: List<PersistedEvent>) {
        val writer: Writer = FileWriter(fileAndPath)
        val beanToCsv: StatefulBeanToCsv<PersistedEvent> = StatefulBeanToCsvBuilder<PersistedEvent>(writer).build()
        beanToCsv.write(events)
        writer.close()
    }

    /**
     * Reads all events from a file.
     */
    fun readAll(fileAndPath: String): MutableList<PersistedEvent> {
        return CsvToBeanBuilder<PersistedEvent>(FileReader(fileAndPath))
            .withType(PersistedEvent::class.java).build().parse()
    }

    /**
     * Returns all events that have a specific id.
     * The result is a list but should contain not more than one event as id is unique.
     * If no event is found the list will be empty.
     */
    fun readForId(fileAndPath: String, id: Int): List<PersistedEvent> {
        val allEvents = readAll(fileAndPath)
        val searchResult = mutableListOf<PersistedEvent>()
        for (event: PersistedEvent in allEvents) {
            if (event.id == id) searchResult.add(event)
        }
        return searchResult
    }

    /**
     * Returns all events that have a specific chartId.
     * If no event is found the list will be empty.
     */
    fun readForChartId(fileAndPath: String, chartId: Int): List<PersistedEvent> {
        val allEvents = readAll(fileAndPath)
        val searchResult = mutableListOf<PersistedEvent>()
        for (event: PersistedEvent in allEvents) {
            if (event.chartId == chartId) searchResult.add(event)
        }
        return searchResult
    }

    /**
     * Adds an event to a given file without deleting previous data.
     */
    fun add(fileAndPath: String, event: PersistedEvent) {
        val allEvents = readAll(fileAndPath)
        val nextId = findNextId(allEvents)
        event.id = nextId
        allEvents.add(event)
        writeAll(fileAndPath, allEvents)
    }

    /**
     * Updates a specific event that has the same id as event2Update.
     */
    fun update(fileAndPath: String, event2Update: PersistedEvent) {
        val id = event2Update.id
        val allEvents = readAll(fileAndPath)
        var newEvent: MutableList<PersistedEvent> = mutableListOf()
        for (event: PersistedEvent in allEvents) {
            if (event.id != id) newEvent.add(event)
        }
        newEvent.add(event2Update)
        writeAll(fileAndPath, newEvent)
    }


    /**
     * Deletes an event that has the same id as event2Delete.
     */
    fun delete(fileAndPath: String, event2Delete: PersistedEvent) {
        val id = event2Delete.id
        val allEvents = readAll(fileAndPath)
        var newEvents: MutableList<PersistedEvent> = mutableListOf()
        for (event: PersistedEvent in allEvents) {
            if (event.id != id) newEvents.add(event)
        }
        writeAll(fileAndPath, newEvents)
    }

    /**
     * Calculates the next available id, comparable to a sequence in a RDBMS.
     */
    private fun findNextId(allEvents: List<PersistedEvent>): Int {
        var nextId = 1
        for (chart: PersistedEvent in allEvents) {
            if (chart.id >= nextId) nextId = chart.id+1
        }
        return nextId
    }


}