/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.handlers

import com.radixpro.enigma.libbe.api.*
import com.radixpro.enigma.libbe.di.Injector
import com.radixpro.enigma.libbe.domain.*
import com.radixpro.enigma.libbe.persistency.ChartDao
import com.radixpro.enigma.libbe.persistency.Dao
import com.radixpro.enigma.libbe.persistency.EventDao

abstract class PersistencyHandler {

    abstract var dao: Dao

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

    fun read(request: ReadRequest): ReadResponse {
        var errors = false
        var comments = ""
        var resultData: List<Persistable> = ArrayList()
        try {
            if (request.action == ReadActions.READALL) resultData = dao.readAll(request.fileAndPath)
            if (request.action == ReadActions.READFORID) resultData = dao.readForId(request.fileAndPath, request.searchId)
            if (request.action == ReadActions.SEARCHFORNAME) {
                if (request is ChartReadRequest) {
                    resultData = (dao as ChartDao).searchForName(request.fileAndPath, request.searchPartOfName)
                } else {
                    errors = true
                    comments += "Search for name is only supported for charts"
                }
            }
            if (request.action == ReadActions.READFORCHARTID) {
                if (request is EventReadRequest) resultData =
                    (dao as EventDao).readForChartId(request.fileAndPath, request.searchId)

                errors = true
                comments+= "Read for chartId is only supported for events"
            }
        } catch(e: Exception) {
            errors = true
            comments+= "Error while reading chart(s): " + e.message
        }
        return createResponse(resultData, errors, comments)
    }

    abstract fun createResponse(resultData: List<Persistable>, errors: Boolean, comments: String): ReadResponse


}


class ChartPersistencyHandler: PersistencyHandler() {

    override var dao: Dao  = ChartDao()

    override fun createResponse(resultData: List<Persistable>, errors: Boolean, comments: String): ReadResponse {
        return ChartReadResponse(resultData as List<ChartData>, errors, comments)
    }

}

class EventPersistencyHandler: PersistencyHandler() {

    override var dao: Dao  = EventDao()

    override fun createResponse(resultData: List<Persistable>, errors: Boolean, comments: String): ReadResponse {
        return EventReadResponse(resultData as List<ChartEvent>, errors, comments)
    }
}

class ConfigPersistencyHandler: PersistencyHandler()  {

    override var dao: Dao = Injector.injectConfigDao()

    override fun createResponse(resultData: List<Persistable>, errors: Boolean, comments: String): ReadResponse {
        return ConfigReadResponse(resultData as List<Config>, errors, comments)
    }

}