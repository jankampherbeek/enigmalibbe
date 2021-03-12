/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.domain.ChartEvent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

internal class EventDaoTest {

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testEvents.csv"
    private val persEvent1 = ChartEvent(1, 1, "descr1", 2444406.5, 10.0,
        20.0, "Source: xxx, Location: yyy, Coordinates: 10.00.00 N/20.00.00 E, Date: ..... G, Time: 12:00:00 UT")
    private val persEvent2 = ChartEvent(2, 1, "descr2", 2444506.5, 14.0,
        -24.0, "Source: aaa, Location: bbb, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT")
    private val persEvent3 = ChartEvent(3, 11, "descr3", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT")
    private val persEventNew = ChartEvent(4, 11, "descr-new", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT")
    private val persEventUpdate = ChartEvent(2, 11, "descr-updated", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT")

    private val dao = EventDao()

    @Test
    fun `Writing and reading a set of events should result in the originally saved events`() {
        initializeCsvFile()
        val results = dao.readAll(fileAndPath) as List<ChartEvent>
        results.size shouldBe 3
        for (event: ChartEvent in results) {
            if (event.id == 2) event.description shouldBe "descr2"
        }
    }

    @Test
    fun `Reading an event for a specific id should return the correct event`() {
        initializeCsvFile()
        val result = dao.readForId(fileAndPath, 3) as List<ChartEvent>
        result.size shouldBe 1
        result[0].id shouldBe 3
        result[0].description shouldBe "descr3"
    }

    @Test
    fun readForChartId() {
        initializeCsvFile()
        val result = dao.readForChartId(fileAndPath, 1)
        result.size shouldBe 2
        result[0].chartId shouldBe 1
    }

    @Test
    fun `Adding an event to an existing file should give the correct result`() {
        initializeCsvFile()
        dao.add(fileAndPath, persEventNew)
        val result1 = dao.readAll(fileAndPath) as List<ChartEvent>
        result1.size shouldBe 4
        val result2 = dao.readForId(fileAndPath,4) as List<ChartEvent>
        result2[0].description shouldBe "descr-new"
    }

    @Test
    fun `Updating an event should give the expected results`() {
        initializeCsvFile()
        dao.update(fileAndPath, persEventUpdate)
        dao.readAll(fileAndPath).size shouldBe 3
        val result = dao.readForId(fileAndPath, 2) as List<ChartEvent>
        result[0].description shouldBe "descr-updated"
    }

    @Test
    fun `Deleting an event should give the expected results`() {
        initializeCsvFile()
        dao.delete(fileAndPath, persEvent2)
        dao.readAll(fileAndPath).size shouldBe 2
        dao.readForId(fileAndPath, 2).size shouldBe 0
    }

    private fun initializeCsvFile() {
        val allEvents = listOf(persEvent1, persEvent2, persEvent3)
        dao.writeAll(fileAndPath, allEvents)
    }





}