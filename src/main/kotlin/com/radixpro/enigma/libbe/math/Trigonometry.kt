/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */
package com.radixpro.enigma.libbe.math

import kotlin.math.PI

object Trigonometry {

    private fun deg2Rad(degree: Double): Double {
        return degree * (PI / 180.0)
    }

    private fun rad2Deg(rad: Double): Double {
        return rad * (180.0 / PI)
    }

    /**
     * Calculate sine. Input and output are in degrees.
     */
    fun sin(value: Double): Double {
        return kotlin.math.sin(deg2Rad(value))
    }

    /**
     * Calculate cosine. Input and output are in degrees.
     */
    fun cos(value: Double): Double {
        return kotlin.math.cos(deg2Rad(value))
    }

    /**
     * Calculate tangent. Input and output are in degrees.
     */
    fun tan(value: Double): Double {
        return kotlin.math.tan(deg2Rad(value))
    }

    /**
     * Calculate arcsine. Input and output are in degrees.
     */
    fun asin(value: Double): Double {
        return rad2Deg(kotlin.math.asin(value))
    }

    /**
     * Calculate arccosine. Input and output are in degrees.
     */
    fun acos(value: Double): Double {
        return rad2Deg(kotlin.math.acos(value))
    }

    /**
     * Calculate arctangent. Input and output are in degrees.
     */
    fun atan(value: Double): Double {
        return rad2Deg(kotlin.math.atan(value))
    }

    /**
     * Calculate atan2. Input and output are in degrees.
     */
    fun atan2(value1: Double, value2: Double): Double {
        return rad2Deg(kotlin.math.atan2(value1, value2))
    }
}