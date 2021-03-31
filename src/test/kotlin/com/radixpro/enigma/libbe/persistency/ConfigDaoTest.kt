/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.di.Injector
import com.radixpro.enigma.libbe.domain.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.File

internal class ConfigDaoTest {

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testConfigs.csv"

    private val celPoints = arrayListOf(PersistedCelPoint(CelPoints.SUN, 100,true, "a"),
        PersistedCelPoint(CelPoints.MOON, 100,true, "b"),
        PersistedCelPoint(CelPoints.MERCURY, 80,true, "C"))
    private val aspects = arrayListOf(PersistedAspect(Aspects.CONJUNCTION, 100,true, "B"),
        PersistedAspect(Aspects.OPPOSITION, 100,true, "C" ))
    private val config1 = Config(1, "Western", "descr-1", Ayanamshas.NONE,
        HouseSystems.PLACIDUS, ObserverPos.GEOCENTRIC, celPoints, aspects, 10.0, 1.6, 2.0, 1.0)
    private val config2 = Config(2, "Fagan", "descr-2", Ayanamshas.FAGAN,
        HouseSystems.EQUAL, ObserverPos.GEOCENTRIC, celPoints, aspects, 10.0, 1.6, 2.0, 1.0)
    private val config3 = Config(3, "Helio", "descr-3", Ayanamshas.NONE,
        HouseSystems.NO_HOUSES, ObserverPos.HELIOCENTRIC, celPoints, aspects, 10.0, 1.6, 2.0, 1.0)
    private val configNew = Config(0, "New", "descr-new", Ayanamshas.NONE,
        HouseSystems.NO_HOUSES, ObserverPos.HELIOCENTRIC, celPoints, aspects, 10.0, 1.6, 2.0, 1.0)
    private val configUpdate = Config(2, "Update", "descr-updated", Ayanamshas.NONE,
        HouseSystems.ALCABITIUS, ObserverPos.TOPOCENTRIC, celPoints, aspects, 10.0, 1.6, 2.0, 1.0)

    private val dao = Injector.injectConfigDao()


    @Test
    fun `Writing and reading a set of configs should result in the originally saved configs`() {
        initializeCsvFile()
        val results = dao.readAll(fileAndPath) as List<Config>
        results.size shouldBe 3
        for (config: Config in results) {
            if (config.id == 2) config.description shouldBe "descr-2"
        }
    }


    @Test
    fun `Reading a config for a specific id should return the correct config`() {
        initializeCsvFile()
        val result = dao.readForId(fileAndPath, 3) as List<Config>
        result.size shouldBe 1
        result[0].id shouldBe 3
        result[0].description shouldBe "descr-3"
    }

    @Test
    fun `Adding a config to an existing file should give the correct result`() {
        initializeCsvFile()
        dao.add(fileAndPath,configNew)
        val result1 = dao.readAll(fileAndPath) as List<Config>
        result1.size shouldBe 4
        val result2 = dao.readForId(fileAndPath,4) as List<Config>
        result2[0].description shouldBe "descr-new"
    }

    @Test
    fun `Updating a config should give the expected results`() {
        initializeCsvFile()
        dao.update(fileAndPath, configUpdate)
        dao.readAll(fileAndPath).size shouldBe 3
        val result = dao.readForId(fileAndPath, 2) as List<Config>
        result[0].description shouldBe "descr-updated"
        result[0].name shouldBe "Update"
    }

    @Test
    fun `Deleting a config shoulkd give the expected results`() {
        initializeCsvFile()
        dao.delete(fileAndPath, config2)
        dao.readAll(fileAndPath).size shouldBe 2
        dao.readForId(fileAndPath, 2).size shouldBe 0
    }

    private fun initializeCsvFile() {
        val allConfigs = listOf(config1, config2, config3)
        dao.writeAll(fileAndPath, allConfigs)
    }

}