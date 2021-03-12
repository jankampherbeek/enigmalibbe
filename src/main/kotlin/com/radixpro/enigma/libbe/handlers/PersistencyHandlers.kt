/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.handlers

import com.radixpro.enigma.libbe.api.*
import com.radixpro.enigma.libbe.domain.*
import com.radixpro.enigma.libbe.persistency.ChartDao
import com.radixpro.enigma.libbe.persistency.ConfigDao
import com.radixpro.enigma.libbe.persistency.Dao
import com.radixpro.enigma.libbe.persistency.EventDao

abstract class PersistencyHandler(val dao: Dao) {

    fun write(request: WriteRequest): WriteResponse {
        var errors = false
        var comments = ""
        var nrOfChanges = 0
        try {
            nrOfChanges = 1
            if (request.action == WriteActions.ADD) dao.add(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.WRITEALL) {
                dao.writeAll(request.fileAndPath, request.items)
                nrOfChanges = request.items.size
            }
            if (request.action == WriteActions.UPDATE) dao.update(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.DELETE) dao.delete(request.fileAndPath, request.items[0])
        } catch(e: Exception) {
            errors = true
            comments+= "Error while writing chart(s): " + e.message
        }
        return WriteResponse(nrOfChanges, errors, comments)
    }
}


class ChartPersistencyHandler(private val dao: ChartDao) {

    fun write(request: ChartWriteRequest): WriteResponse {
        var errors = false
        var comments = ""
        var nrOfChanges = 0
        try {
            nrOfChanges = 1
            if (request.action == WriteActions.ADD) dao.add(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.WRITEALL) {
                dao.writeAll(request.fileAndPath, request.items)
                nrOfChanges = request.items.size
            }
            if (request.action == WriteActions.UPDATE) dao.update(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.DELETE) dao.delete(request.fileAndPath, request.items[0])
        } catch(e: Exception) {
            errors = true
            comments+= "Error while writing chart(s): " + e.message
        }
        return WriteResponse(nrOfChanges, errors, comments)
    }

    fun read(request: ChartReadRequest): ChartReadResponse {
        var errors = false
        var comments = ""
        var resultData: List<ChartData> = ArrayList()
        try {
            if (request.action == ReadActions.READALL) resultData = dao.readAll(request.fileAndPath) as List<ChartData>
            if (request.action == ReadActions.READFORID) resultData = dao.readForId(request.fileAndPath, request.searchId) as List<ChartData>
            if (request.action == ReadActions.SEARCHFORNAME) resultData = dao.searchForName(request.fileAndPath, request.searchPartOfName)
            if (request.action == ReadActions.READFORCHARTID) {
                errors = true
                comments+= "Read for chartId is only supported for events"
            }
        } catch(e: Exception) {
            errors = true
            comments+= "Error while reading chart(s): " + e.message
        }
        return ChartReadResponse(resultData, errors, comments)
    }
}

class EventPersistencyHandler(private val dao: EventDao) {

    fun write(request: EventWriteRequest): WriteResponse {
        var errors = false
        var comments = ""
        var nrOfChanges = 0
        try {
            nrOfChanges = 1
            if (request.action == WriteActions.ADD) dao.add(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.WRITEALL) {
                dao.writeAll(request.fileAndPath, request.items)
                nrOfChanges = request.items.size
            }
            if (request.action == WriteActions.UPDATE) dao.update(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.DELETE) dao.delete(request.fileAndPath, request.items[0])
        } catch(e: Exception) {
            errors = true
            comments+= "Error while writing chart(s): " + e.message
        }
        return WriteResponse(nrOfChanges, errors, comments)
    }

    fun read(request: EventReadRequest): EventReadResponse {
        var errors = false
        var comments = ""
        var resultData: List<ChartEvent> = ArrayList()
        try {
            if (request.action == ReadActions.READALL) resultData = dao.readAll(request.fileAndPath) as List<ChartEvent>
            if (request.action == ReadActions.READFORID) resultData = dao.readForId(request.fileAndPath, request.searchId) as List<ChartEvent>
            if (request.action == ReadActions.READFORCHARTID) resultData = dao.readForChartId(request.fileAndPath, request.searchId)
        } catch(e: Exception) {
            errors = true
            comments+= "Error while reading event(s): " + e.message
        }
        return EventReadResponse(resultData, errors, comments)
    }

}



class ConfigPersistencyHandler(private val dao: ConfigDao) {

    fun write(request: ConfigWriteRequest): WriteResponse {
        var errors = false
        var comments = ""
        var nrOfChanges = 0
        try {
            nrOfChanges = 1
            if (request.action == WriteActions.ADD) dao.add(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.WRITEALL) {
                dao.writeAll(request.fileAndPath, request.items)
                nrOfChanges = request.items.size
            }
            if (request.action == WriteActions.UPDATE) dao.update(request.fileAndPath, request.items[0])
            if (request.action == WriteActions.DELETE) dao.delete(request.fileAndPath, request.items[0])
        } catch(e: Exception) {
            errors = true
            comments+= "Error while writing config(s): " + e.message
        }
        return WriteResponse(nrOfChanges, errors, comments)
    }

    fun read(request: ConfigReadRequest): ConfigReadResponse {
        var errors = false
        var comments = ""
        var resultData: List<Config> = ArrayList()
        try {
            if (request.action == ReadActions.READALL) resultData = dao.readAll(request.fileAndPath) as List<Config>
            if (request.action == ReadActions.READFORID) resultData = dao.readForId(request.fileAndPath, request.searchId) as List<Config>
            if (request.action == ReadActions.READFORCHARTID) {
                errors = true
                comments+= "Read for chartId is only supported for events"
            }
        } catch(e: Exception) {
            errors = true
            comments+= "Error while reading config(s): " + e.message
        }
        return ConfigReadResponse(resultData, errors, comments)
    }

}