/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.api

import com.radixpro.enigma.libbe.domain.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

internal class ChartPersistencyApiTest {

    private lateinit var api: ChartPersistencyApi

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testCharts.csv"
    private val persChart1 = ChartData(1, "Name1", "Descr1", Ratings.AA, ChartTypes.MALE,
        2434406.8125, 52.0, 5.5,
        "Source: someone told me, Location: Amsterdam, Coordinates: 52.00.00 N/5.30.00 E, Date: 1953/01/29 G, Time: 8:37:30 CET")
    private val persChart2 = ChartData(2, "Name2", "Descr2", Ratings.X, ChartTypes.FEMALE,
        2434416.0, 50.0, -5.5,
        "Source: anything, Location: Somewhere, Coordinates: 50.00.00 N/5.00.00 W, Date: 1953/02/07 G, Time: 12:00:00 UT")
    private val persChart3 = ChartData(3, "Name3", "Descr3", Ratings.B, ChartTypes.HORARY,
        2434516.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")
    private val persChartNew = ChartData(0, "Name-new", "Descr-new", Ratings.C, ChartTypes.EVENT,
        2434616.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")
    private val persChartUpdate = ChartData(2, "Name-updated", "Descr-updated", Ratings.C, ChartTypes.EVENT,
        2434616.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")

    @BeforeEach
    fun setUp() {
        api = ChartPersistencyApi()
    }

    @Test
    fun `Writing chartData and then reading it should result in the same chartData`() {
        val charts = listOf(persChart1)
        val writeRequest = ChartWriteRequest(WriteActions.WRITEALL, fileAndPath, charts)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ChartReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result[0] shouldBe persChart1
    }

    @Test
    fun `Updating an instance of chartData should be handled correctly`() {
        val charts = listOf(persChart1, persChart2, persChart3)
        var writeRequest = ChartWriteRequest(WriteActions.WRITEALL, fileAndPath, charts)
        api.write(writeRequest).errors shouldBe false
        val updateChart = listOf(persChartUpdate)
        writeRequest = ChartWriteRequest(WriteActions.UPDATE, fileAndPath, updateChart)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ChartReadRequest(ReadActions.READFORID, fileAndPath, 2)
        val chartData = api.read(readRequest)
        chartData.errors shouldBe false
        chartData.result[0] shouldBe persChartUpdate
    }

    @Test
    fun `Trying to update an instance of chartData that does not exist should add the chartData`() {
        val charts = listOf(persChart1, persChart2, persChart3)
        var writeRequest = ChartWriteRequest(WriteActions.WRITEALL, fileAndPath, charts)
        api.write(writeRequest).errors shouldBe false
        val updateChart = listOf(persChartNew)
        writeRequest = ChartWriteRequest(WriteActions.UPDATE, fileAndPath, updateChart)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ChartReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 4
    }

    @Test
    fun `Deleting an instance of chartData should be handled correctly`() {
        val charts = listOf(persChart1, persChart2, persChart3)
        var writeRequest = ChartWriteRequest(WriteActions.WRITEALL, fileAndPath, charts)
        api.write(writeRequest).errors shouldBe false
        writeRequest = ChartWriteRequest(WriteActions.DELETE, fileAndPath, listOf(persChart3))
        api.write(writeRequest).errors shouldBe false
        val readRequest = ChartReadRequest(ReadActions.READFORID, fileAndPath, 3)
        api.read(readRequest).result.size shouldBe 0
    }

    @Test
    fun `Trying to delete an instance of chartData that does not exist should not cause any error`() {
        val charts = listOf(persChart1, persChart2, persChart3)
        var writeRequest = ChartWriteRequest(WriteActions.WRITEALL, fileAndPath, charts)
        api.write(writeRequest).errors shouldBe false
        writeRequest = ChartWriteRequest(WriteActions.DELETE, fileAndPath, listOf(persChartNew))
        api.write(writeRequest).errors shouldBe false
        val readRequest = ChartReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 3
    }

}