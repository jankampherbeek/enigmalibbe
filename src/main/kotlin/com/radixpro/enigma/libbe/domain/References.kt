/*
 *
 *  * Jan Kampherbeek, (c) 2021.
 *  * EnigmaLibBe is open source.
 *  * Please check the file copyright.txt in the root of the source for further details.
 *
 */

package com.radixpro.enigma.libbe.domain

import swisseph.SweConst

/**
 * Different parameters that define a calculation.
 * The long values will be or'ed to define the flag value for the SE.
 */
enum class SeFlags(val seValue: Long) {
    SWISSEPH(SweConst.SEFLG_SWIEPH.toLong()),       // 2L
    HELIOCENTRIC(SweConst.SEFLG_HELCTR.toLong()),   // 8L
    SPEED(SweConst.SEFLG_SPEED.toLong()),           // 256L
    EQUATORIAL(SweConst.SEFLG_EQUATORIAL.toLong()), // 2048L
    TOPOCENTRIC(SweConst.SEFLG_TOPOCTR.toLong()),   // 32*1024L
    SIDEREAL(SweConst.SEFLG_SIDEREAL.toLong()),     // 64*1024L
    HORIZONTAL(SweConst.SE_ECL2HOR.toLong());       // 0 int! Not to be combined with other flags

}

/**
 * The position of the observer.
 */
enum class ObserverPos {
    GEOCENTRIC,
    TOPOCENTRIC,
    HELIOCENTRIC
}

/**
 * The types of coordinates.
 */
enum class CoordinateTypes {
    ECLIPTICAL,
    EQUATORIAL,
    HORIZONTAL
}

/**
 * Ratings according to Louise Rodden, including an 'UNKNOWN' rating.
 */
enum class Ratings{
    UNKNOWN,
    AA,
    A,
    B,
    C,
    DD,
    X,
    XX
}

/**
 * Types of charts.
 */
enum class ChartTypes {
    UNKNOWN,
    FEMALE,
    MALE,
    NATAL,
    EVENT,
    HORARY,
    ELECTION
}

/**
 * Types of chartrequests.
 */
enum class ChartRequestTypes {
    SIMPLE,
    BASE,
    FULL
}


/**
 * Categories for celestial points.
 */
enum class CelPointCategory {
    CLASSICS,
    MODERN,
    EXTRA_PLUT,
    ASTEROIDS,
    CENTAURS,
    INTERSECTIONS,
    HYPOTHETS
}

/**
 * Celestial points: Celestial objects and mathematical points htat are treated as a kind of planet (like the lunar node).
 */
enum class CelPoints(
    val id: Int,
    val seId: Int,
    val category: CelPointCategory
) {
    SUN(1, 0, CelPointCategory.CLASSICS),
    MOON(2, 1, CelPointCategory.CLASSICS),
    MERCURY(3, 2, CelPointCategory.CLASSICS),
    VENUS(4, 3, CelPointCategory.CLASSICS),
    EARTH(5, 14, CelPointCategory.CLASSICS),
    MARS(6, 4, CelPointCategory.CLASSICS),
    JUPITER(7, 5, CelPointCategory.CLASSICS),
    SATURN(8, 6, CelPointCategory.CLASSICS),
    URANUS(9, 7, CelPointCategory.MODERN),
    NEPTUNE(10, 8, CelPointCategory.MODERN),
    PLUTO(11, 9, CelPointCategory.MODERN),
    CHEIRON(12, 15, CelPointCategory.CENTAURS),
    MEAN_NODE(13, 10, CelPointCategory.INTERSECTIONS),
    TRUE_NODE(14, 11, CelPointCategory.INTERSECTIONS),
    PHOLUS(15, 16, CelPointCategory.CENTAURS),
    CERES(16, 17, CelPointCategory.ASTEROIDS),
    PALLAS(17, 18, CelPointCategory.ASTEROIDS),
    JUNO(18, 19, CelPointCategory.ASTEROIDS),
    VESTA(19, 20, CelPointCategory.ASTEROIDS),
    NESSUS(20, 17066, CelPointCategory.CENTAURS),
    HUYA(21, 48628, CelPointCategory.EXTRA_PLUT),
    MAKEMAKE(22, 146472, CelPointCategory.EXTRA_PLUT),
    HAUMEA(23, 146108, CelPointCategory.EXTRA_PLUT),
    ERIS(24, 146199, CelPointCategory.EXTRA_PLUT),
    IXION(25, 38978, CelPointCategory.EXTRA_PLUT),
    ORCUS(26, 100482, CelPointCategory.EXTRA_PLUT),
    QUAOAR(27, 60000, CelPointCategory.EXTRA_PLUT),
    SEDNA(28, 100377, CelPointCategory.EXTRA_PLUT),
    VARUNA(29, 30000, CelPointCategory.EXTRA_PLUT),
    MEAN_APOGEE(30, 12, CelPointCategory.INTERSECTIONS),
    OSCU_APOGEE(31, 13, CelPointCategory.INTERSECTIONS)
}

/**
 * Specifications of house systems.
 */
    enum class HouseSystems(
        val id: Int,
        val seId: Char,
        val nrOfCusps: Int,
        val isCounterClockwise: Boolean,
        val isQuadrantSystem: Boolean,
        val isCuspIsStart: Boolean
    ) {
        NO_HOUSES(1, 'W', 0, false, false, false),
        WHOLESIGN(2, 'W', 12, true, false, true),
        EQUAL(3, 'A', 12, true, false, true),
        EQUAL_MC(4, 'D', 12, true, false, true),
        VEHLOW(5, 'V', 12, true, false, false),
        PLACIDUS(6, 'P', 12, true, true, true),
        KOCH(7, 'K', 12, true, true, true),
        PORPHYRI(8, 'O', 12, true, true, true),
        REGIOMONTANUS(9, 'R', 12, true, true, true),
        CAMPANUS(10, 'C', 12, true, true, true),
        ALCABITIUS(11, 'B', 12, true, true, true),
        TOPOCENTRIC(12, 'T', 12, true, true, true),
        KRUSINSKI(13, 'U', 12, true, true, true),
        APC(14, 'Y', 12, true, true, true),
        MORIN(15, 'M', 12, true, false, true),
        AXIAL(16, 'X', 12, true, false, true),
        HORIZON(17, 'H', 12, true, false, true),
        CARTER(18, 'F', 12, true, false, true),
        EQUAL_ARIES(19, 'N', 12, true, false, true),
        GAUQUELIN(20, 'G', 36, true, false, true),
        SUNSHINE(21, 'i', 12, true, false, false),
        SUNSHINE_TREINDL(22, 'I', 12, true, false, true)
    }

/**
 * Ayanamsha's including the id for the SE.
 */
enum class Ayanamshas(val seId: Int){
        NONE(-1),
        FAGAN(0),
        LAHIRI(1),
        DELUCE(2),
        RAMAN(3),
        USHA_SHASHI(4),
        KRISHNAMURTI(5),
        DJWHAL_KHUL(6),
        YUKTESHWAR(7),
        BHASIN(8),
        KUGLER_1(9),
        KUGLER_2(10),
        KUGLER_3(11),
        HUBER(12),
        ETA_PISCIUM(13),
        ALDEBARAN_15TAU(14),
        HIPPARCHUS(15),
        SASSANIAN(16),
        GALACT_CTR_0SAG(17),
        J2000(18),
        J1900(19),
        B1950(20),
        SURYASIDDHANTA(21),
        SURYASIDDHANTA_MEAN_SUN(22),
        ARYABHATA(23),
        ARYABHATA_MEAN_SUN(24),
        SS_REVATI(25),
        SS_CITRA(26),
        TRUE_CITRA(27),
        TRUE_REVATI(28),
        TRUE_PUSHYA(29),
        GALACTIC_CTR_BRAND(30),
        GALACTIC_EQ_IAU1958(31),
        GALACTIC_EQ(32),
        GALACTIC_EQ_MID_MULA(33),
        SKYDRAM(34),
        TRUE_MULA(35),
        DHRUVA(36),
        ARYABHATA_522(37),
        BRITTON(38),
        GALACTIC_CTR_0CAP(39)
    }

/**
 * Aspects including the angle.
 */
    enum class Aspects(angle: Double) {
        CONJUNCTION(0.0),
        OPPOSITION(180.0),
        TRIANGLE(120.0),
        SQUARE(90.0),
        SEXTILE(60.0),
        SEMISEXTILE(30.0),
        INCONJUNCT(150.0),
        SEMISQUARE(45.0),
        SESQUIQUADRATE(135.0),
        QUINTILE(72.0),
        BIQUINTILE(144.0),
        SEPTILE(51.42857143),
        VIGINTILE(18.0),
        SEMIQUINTILE(36.0),
        TRIDECILE(108.0),
        BISEPTILE(102.857142857),
        TRISEPTILE(154.2857142857),
        NOVILE(40.0),
        BINOVILE(80.0),
        QUADRANOVILE(160.0),
        UNDECILE( 32.7272727272),
        CENTILE(100.0)

    }

/**
 * Types of write-actions for persistable data.
 */
    enum class WriteActions {
        WRITEALL,
        ADD,
        UPDATE,
        DELETE
    }

/**
 * Types of read-actions for persisted data.
 */
    enum class ReadActions {
        READALL,
        READFORID,
        READFORCHARTID,
        SEARCHFORNAME
    }




