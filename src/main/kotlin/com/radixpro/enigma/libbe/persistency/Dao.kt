/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.persistency

import com.radixpro.enigma.libbe.domain.Persistable

abstract class Dao{


    fun add(fileAndPath: String, item: Persistable) {
        val allItems = readAll(fileAndPath)
        val nextId = findNextId(allItems)
        item.id = nextId
        allItems.add(defineItemType(item))
        writeAll(fileAndPath, allItems)
    }

    /**
     * Calculates the next available id, comparable to a sequence in a RDBMS.
     */
    private fun findNextId(allItems: List<Persistable>): Int {
        var nextId = 1
        for (item: Persistable in allItems) {
            if (item.id >= nextId) nextId = item.id+1
        }
        return nextId
    }

    /**
     * Returns all items that have a specific id.
     * The result is a list but should contain not more than one item as id is unique.
     * If no item is found the list will be empty.
     */
    fun readForId(fileAndPath: String, id: Int): List<Persistable> {
        val allItems = readAll(fileAndPath)
        val searchResult = mutableListOf<Persistable>()
        for (persItem: Persistable in allItems) {
            if (persItem.id == id) searchResult.add(persItem)
        }
        return searchResult
    }

    /**
     * Updates a specific item that has the same id as item2Update.
     */
    fun update(fileAndPath: String, item2Update: Persistable) {
        val id = item2Update.id
        val allItems = readAll(fileAndPath)
        val newItem: MutableList<Persistable> = mutableListOf()
        for (persItem: Persistable in allItems) {
            if (persItem.id != id) newItem.add(persItem)
        }
        newItem.add(item2Update)
        writeAll(fileAndPath, newItem)
    }

    /**
     * Deletes an item that has the same id as item
     * 2Delete.
     */
    fun delete(fileAndPath: String, item2Delete: Persistable) {
        val id = item2Delete.id
        val allItems = readAll(fileAndPath)
        val newItems: MutableList<Persistable> = mutableListOf()
        for (item: Persistable in allItems) {
            if (item.id != id) newItems.add(item)
        }
        writeAll(fileAndPath, newItems)
    }


    abstract fun defineItemType(item: Persistable): Persistable
    abstract fun readAll(fileAndPath: String): MutableList<Persistable>
    abstract fun writeAll(fileAndPath: String, items: List<Persistable>)

}