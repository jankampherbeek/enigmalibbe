/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.domain.ChartTypes
import com.radixpro.enigma.libbe.domain.PersistedChart
import com.radixpro.enigma.libbe.domain.Ratings
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

internal class ChartDaoTest {

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testCharts.csv"
    private val persChart1 = PersistedChart(1, "Name1", "Descr1", Ratings.AA, ChartTypes.MALE,
        2434406.8125, 52.0, 5.5,
        "Source: someone told me, Location: Amsterdam, Coordinates: 52.00.00 N/5.30.00 E, Date: 1953/01/29 G, Time: 8:37:30 CET")
    private val persChart2 = PersistedChart(2, "Name2", "Descr2", Ratings.X, ChartTypes.FEMALE,
        2434416.0, 50.0, -5.5,
        "Source: anything, Location: Somewhere, Coordinates: 50.00.00 N/5.00.00 W, Date: 1953/02/07 G, Time: 12:00:00 UT")
    private val persChart3 = PersistedChart(3, "Name3", "Descr3", Ratings.B, ChartTypes.HORARY,
        2434516.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")
    private val persChartNew = PersistedChart(0, "Name-new", "Descr-new", Ratings.C, ChartTypes.EVENT,
        2434616.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")
    private val persChartUpdate = PersistedChart(2, "Name-updated", "Descr-updated", Ratings.C, ChartTypes.EVENT,
        2434616.0, -60.0, -55.5,
        "Source: my info, Location: my place, Coordinates: 60.00.00 S/55.30.00 W, Date: ..... G, Time: .....")
    private val dao = ChartDao()

    @Test
    fun `Writing and reading a set of charts should result in the originally saved charts`() {
        initializeCsvFile()
        val results = dao.readAll(fileAndPath)
        results.size shouldBe 3
        for (chart: PersistedChart in results) {
            if (chart.id == 2) chart.description shouldBe "Descr2"
        }
    }

    @Test
    fun `Reading a chart for a specific id should return the correct chart`() {
        initializeCsvFile()
        val result = dao.readForId(fileAndPath, 3)
        result.size shouldBe 1
        result[0].id shouldBe 3
        result[0].description shouldBe "Descr3"
    }

    @Test
    fun `Searching a chart where the name uniquely contains a specific String should return the correct chart`() {
        initializeCsvFile()
        val result = dao.searchForName(fileAndPath, "me2")
        result.size shouldBe 1
        result[0].id shouldBe 2
    }

    @Test
    fun `Searching multiple charts where the name contains a specific String should return the correct charts`() {
        initializeCsvFile()
        val result = dao.searchForName(fileAndPath, "Name")
        result.size shouldBe 3
        result[0].id shouldBe 1
    }

    @Test
    fun `Adding a chart to an existing file should give the correct result`() {
        initializeCsvFile()
        dao.add(fileAndPath, persChartNew)
        val result1 = dao.readAll(fileAndPath)
        result1.size shouldBe 4
        val result2 = dao.readForId(fileAndPath,4)
        result2[0].description shouldBe "Descr-new"
    }

    @Test
    fun `Updating a chart should give the expected results`() {
        initializeCsvFile()
        dao.update(fileAndPath, persChartUpdate)
        dao.readAll(fileAndPath).size shouldBe 3
        val result = dao.readForId(fileAndPath, 2)[0]
        result.name shouldBe "Name-updated"
    }

    @Test
    fun `Deleting a chart shoulkd give the expected results`() {
        initializeCsvFile()
        dao.delete(fileAndPath, persChart2)
        dao.readAll(fileAndPath).size shouldBe 2
        dao.readForId(fileAndPath, 2).size shouldBe 0
    }



    private fun initializeCsvFile() {
        val allCharts = listOf(persChart1, persChart2, persChart3)
        dao.writeAll(fileAndPath, allCharts)
    }

}