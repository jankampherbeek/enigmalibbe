/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.astron

import com.radixpro.enigma.libbe.domain.CoordinateTypes
import com.radixpro.enigma.libbe.domain.ObserverPos

object FlagConstructor {

    fun defineFlags(observerPos: ObserverPos, coordinateTypes: CoordinateTypes) : Int {
        var flags = 2L or 256L    // Swisseph and speed
        if (observerPos == ObserverPos.HELIOCENTRIC) flags = flags or 8L
        if (observerPos == ObserverPos.TOPOCENTRIC) flags = flags or 32*1024L
        if (coordinateTypes == CoordinateTypes.EQUATORIAL) flags = flags or 2048L
        return flags.toInt()
    }

}