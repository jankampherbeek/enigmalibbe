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

class ConfigPersistencyApitest {

    private val fileAndPath = "." + File.separator + "testdata" + File.separator + " testConfigs.csv"
    private lateinit var api: ConfigPersistencyApi

    private val celPoints = arrayListOf(
        PersistedCelPoint(CelPoints.SUN, true, "a"),
        PersistedCelPoint(CelPoints.MOON, true, "b"),
        PersistedCelPoint(CelPoints.MERCURY, true, "C")
    )
    private val aspects = arrayListOf(
        PersistedAspect(Aspects.CONJUNCTION, true, "B"),
        PersistedAspect(Aspects.OPPOSITION, true, "C" )
    )
    private val config1 = Config(1, "Western", "descr-1", Ayanamshas.NONE,
        HouseSystems.PLACIDUS, ObserverPos.GEOCENTRIC, celPoints, aspects)
    private val config2 = Config(2, "Fagan", "descr-2", Ayanamshas.FAGAN,
        HouseSystems.EQUAL, ObserverPos.GEOCENTRIC, celPoints, aspects)
    private val config3 = Config(3, "Helio", "descr-3", Ayanamshas.NONE,
        HouseSystems.NO_HOUSES, ObserverPos.HELIOCENTRIC, celPoints, aspects)
    private val configNew = Config(0, "New", "descr-new", Ayanamshas.NONE,
        HouseSystems.NO_HOUSES, ObserverPos.HELIOCENTRIC, celPoints, aspects)
    private val configUpdate = Config(2, "Update", "descr-updated", Ayanamshas.NONE,
        HouseSystems.ALCABITIUS, ObserverPos.TOPOCENTRIC, celPoints, aspects)


    @BeforeEach
    fun setUp() {
        api = ConfigPersistencyApi()
    }

    @Test
    fun `Writing configs and then reading it should result in the same config`() {
        val configs = listOf(config1)
        val writeRequest = ConfigWriteRequest(WriteActions.WRITEALL, fileAndPath, configs)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ConfigReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result[0] shouldBe config1
    }

    @Test
    fun `Updating an instance of config should be handled correctly`() {
        val configs = listOf(config1, config2, config3)
        var writeRequest = ConfigWriteRequest(WriteActions.WRITEALL, fileAndPath, configs)
        api.write(writeRequest).errors shouldBe false
        val updateConfig = listOf(configUpdate)
        writeRequest = ConfigWriteRequest(WriteActions.UPDATE, fileAndPath, updateConfig)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ConfigReadRequest(ReadActions.READFORID, fileAndPath, 2)
        val config = api.read(readRequest)
        config.errors shouldBe false
        config.result[0] shouldBe configUpdate
    }

    @Test
    fun `Trying to update an instance of config that does not exist should add the config`() {
        val configs = listOf(config1, config2, config3)
        var writeRequest = ConfigWriteRequest(WriteActions.WRITEALL, fileAndPath, configs)
        api.write(writeRequest).errors shouldBe false
        val updateConfig = listOf(configNew)
        writeRequest = ConfigWriteRequest(WriteActions.UPDATE, fileAndPath, updateConfig)
        api.write(writeRequest).errors shouldBe false
        val readRequest = ConfigReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 4
    }

    @Test
    fun `Deleting an instance of config should be handled correctly`() {
        val configs = listOf(config1, config2, config3)
        var writeRequest = ConfigWriteRequest(WriteActions.WRITEALL, fileAndPath, configs)
        api.write(writeRequest).errors shouldBe false
        writeRequest = ConfigWriteRequest(WriteActions.DELETE, fileAndPath, listOf(config3))
        api.write(writeRequest).errors shouldBe false
        val readRequest = ConfigReadRequest(ReadActions.READFORID, fileAndPath, 3)
        api.read(readRequest).result.size shouldBe 0
    }

    @Test
    fun `Trying to delete an instance of config that does not exist should not cause any error`() {
        val configs = listOf(config1, config2, config3)
        var writeRequest = ConfigWriteRequest(WriteActions.WRITEALL, fileAndPath, configs)
        api.write(writeRequest).errors shouldBe false
        writeRequest = ConfigWriteRequest(WriteActions.DELETE, fileAndPath, listOf(configNew))
        api.write(writeRequest).errors shouldBe false
        val readRequest = ConfigReadRequest(ReadActions.READALL, fileAndPath)
        api.read(readRequest).result.size shouldBe 3
    }

}