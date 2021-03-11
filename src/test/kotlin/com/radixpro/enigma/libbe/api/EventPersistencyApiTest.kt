/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.ChartEvent
import com.radixpro.enigma.libbe.domain.ReadActions
import com.radixpro.enigma.libbe.domain.WriteActions
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class EventPersistencyApiTest {

    private lateinit var api: EventPersistencyApi

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testEvents.csv"
    private val persEvent1 = ChartEvent(
        1, 1, "descr1", 2444406.5, 10.0,
        20.0, "Source: xxx, Location: yyy, Coordinates: 10.00.00 N/20.00.00 E, Date: ..... G, Time: 12:00:00 UT"
    )
    private val persEvent2 = ChartEvent(
        2, 1, "descr2", 2444506.5, 14.0,
        -24.0, "Source: aaa, Location: bbb, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT"
    )
    private val persEvent3 = ChartEvent(
        3, 11, "descr3", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT"
    )
    private val persEventNew = ChartEvent(
        4, 11, "descr-new", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT"
    )
    private val persEventUpdate = ChartEvent(
        2, 11, "descr-updated", 2444606.5, 14.0,
        -24.0, "Source: ccc, Location: ddd, Coordinates: 14.00.00 N/24.00.00 W, Date: ..... G, Time: 12:00:00 UT"
    )

    @BeforeEach
    fun setUp() {
        api = EventPersistencyApi()
    }

    @Test
    fun `Writing events and then reading it should result in the same event`() {
        val events = listOf(persEvent1)
        val writeRequest = EventWriteRequest(WriteActions.WRITEALL, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result[0] shouldBe persEvent1
    }

    @Test
    fun `Adding an event to a non existing file should create the file and add the event to that file`() {
        val file = File(fileAndPath)
        if (file.exists()) file.delete()
        val events = listOf(persEvent1)
        val writeRequest = EventWriteRequest(WriteActions.ADD, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result[0] shouldBe persEvent1
    }

    @Test
    fun `Updating an instance of event should be handled correctly`() {
        val events = listOf(persEvent1, persEvent2, persEvent3)
        var writeRequest = EventWriteRequest(WriteActions.WRITEALL, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        val updateEvent = listOf(persEventUpdate)
        writeRequest = EventWriteRequest(WriteActions.UPDATE, fileAndPath, updateEvent)
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READFORID, fileAndPath, 2)
        val event = api.read(readRequest)
        event.errors shouldBe false
        event.result[0] shouldBe persEventUpdate
    }

    @Test
    fun `Trying to update an instance of event that does not exist should add the event`() {
        val events = listOf(persEvent1, persEvent2, persEvent3)
        var writeRequest = EventWriteRequest(WriteActions.WRITEALL, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        val updateEvent = listOf(persEventNew)
        writeRequest = EventWriteRequest(WriteActions.UPDATE, fileAndPath, updateEvent)
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 4
    }

    @Test
    fun `Deleting an instance of event should be handled correctly`() {
        val events = listOf(persEvent1, persEvent2, persEvent3)
        var writeRequest = EventWriteRequest(WriteActions.WRITEALL, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        writeRequest = EventWriteRequest(WriteActions.DELETE, fileAndPath, listOf(persEvent3))
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READFORID, fileAndPath, 3)
        api.read(readRequest).result.size shouldBe 0
    }

    @Test
    fun `Trying to delete an instance of event that does not exist should not cause any error`() {
        val events = listOf(persEvent1, persEvent2, persEvent3)
        var writeRequest = EventWriteRequest(WriteActions.WRITEALL, fileAndPath, events)
        api.write(writeRequest).errors shouldBe false
        writeRequest = EventWriteRequest(WriteActions.DELETE, fileAndPath, listOf(persEventNew))
        api.write(writeRequest).errors shouldBe false
        val readRequest = EventReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 3
    }

}