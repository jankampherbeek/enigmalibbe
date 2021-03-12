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
import com.radixpro.enigma.libbe.domain.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Writer

class ConfigDao(private val configMapper: ConfigMapper): Dao() {

    /**
     * Writes all configs to a file and overwrites any previous data.
     */
    override fun writeAll(fileAndPath: String, items: List<Persistable>) {
        val writer: Writer = FileWriter(fileAndPath)
        val persConfigs = configMapper.config2PersistedConfig(items as List<Config>)
        val beanToCsv: StatefulBeanToCsv<PersistedConfig> = StatefulBeanToCsvBuilder<PersistedConfig>(writer).build()
        beanToCsv.write(persConfigs)
        writer.close()
    }

    /**
     * Reads all configs from a file.
     */
    override fun readAll(fileAndPath: String): MutableList<Persistable> {
        return if (File(fileAndPath).exists()) {
            val persConfigs = CsvToBeanBuilder<PersistedConfig>(FileReader(fileAndPath))
                .withType(PersistedConfig::class.java).build().parse() as MutableList<Persistable>
            configMapper.persistedConfig2Config(persConfigs as List<PersistedConfig>) as MutableList<Persistable>
        } else mutableListOf()
    }

    override fun defineItemType(item: Persistable): Persistable {
        return item as Config
    }
 }

class ConfigMapper(private val celPointsTextMapper: CelPointsTextMapper, private val aspectsTextMapper: AspectsTextMapper) {

    fun persistedConfig2Config(persConfigs: List<PersistedConfig>): MutableList<Config> {
        val configs = mutableListOf<Config>()
        for (persConfig in persConfigs) {
            configs.add(persistedConfig2Config(persConfig))
        }
        return configs
    }

    fun config2PersistedConfig(configs: List<Config>): List<PersistedConfig> {
        val persConfigs = mutableListOf<PersistedConfig>()
        for (config in configs) {
            persConfigs.add(config2PersistedConfig(config))
        }
        return persConfigs
    }

    private fun persistedConfig2Config(persConfig: PersistedConfig): Config {
        val celPoints = celPointsTextMapper.createCelPoints(persConfig.celPointsText)
        val aspects = aspectsTextMapper.createAspects(persConfig.aspectsText)
        return Config(persConfig.id, persConfig.name, persConfig.description, persConfig.ayanamsha,
            persConfig.houseSystem, persConfig.observerPos, celPoints, aspects)
    }

    private fun config2PersistedConfig(config: Config): PersistedConfig {
        val pointsText = celPointsTextMapper.createText(config.celPoints)
        val aspectsText = aspectsTextMapper.createText(config.aspects)
        return PersistedConfig(config.id, config.name, config.description, config.ayanamsha, config.houseSystem,
            config.observerPos, pointsText, aspectsText)
    }

}


class CelPointsTextMapper {

    private val itemSep = ";"
    private val pointSep = "|"

    fun createText(persPoints: List<PersistedCelPoint>): String {
        var result = ""
        for ((counter, persPoint: PersistedCelPoint) in persPoints.withIndex()) {
            if (counter > 0) result += pointSep
            result += convertPointToText(persPoint)
        }
        return result
    }

    fun createCelPoints(text: String): List<PersistedCelPoint> {
        val persPointTexts = text.split(pointSep)
        val persPoints = ArrayList<PersistedCelPoint>()
        for (pointText: String in persPointTexts) {
            persPoints.add(convertTextToPoint(pointText))
        }
        return persPoints
    }

    private fun convertPointToText(persPoint: PersistedCelPoint): String {
        return persPoint.celPoint.name + itemSep + persPoint.showInDrawing.toString() + itemSep + persPoint.glyph
    }

    private fun convertTextToPoint(pointText: String): PersistedCelPoint {
        val pointItems = pointText.split(itemSep)
        return PersistedCelPoint(CelPoints.valueOf(pointItems[0]), pointItems[1] == "true", pointItems[2])
    }

}


class AspectsTextMapper {

    private val itemSep = ";"
    private val aspectSep = "|"

    fun createText(persAspects: List<PersistedAspect>): String {
        var result = ""
        for ((counter, persAspect: PersistedAspect) in persAspects.withIndex()) {
            if (counter > 0) result += aspectSep
            result += convertAspectToText(persAspect)
        }
        return result
    }

    fun createAspects(text: String): List<PersistedAspect> {
        val persAspectTexts = text.split(aspectSep)
        val persAspects = ArrayList<PersistedAspect>()
        for (aspectText: String in persAspectTexts) {
            persAspects.add(convertTextToAspect(aspectText))
        }
        return persAspects
    }

    private fun convertAspectToText(persAspect: PersistedAspect): String {
        return persAspect.aspect.name + itemSep + persAspect.showInDrawing.toString() + itemSep + persAspect.glyph
    }

    private fun convertTextToAspect(aspectText: String): PersistedAspect {
        val aspectItems = aspectText.split(itemSep)
        return PersistedAspect(Aspects.valueOf(aspectItems[0]), aspectItems[1] == "true", aspectItems[2])
    }
}