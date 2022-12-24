package org.dgawlik.tree

import org.dgawlik.domain.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class HistogramTest {

    val f1 = Feature("LANG1", "first feature", BinaryField())
    val f2 = Feature("LANG2", "second feature", NumericField(1, 4))

    @Test
    @DisplayName("Uniform array should have 0 entropy")
    fun zero_entropy() {


        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 1)))
        )

        val hist = Histogram(f1, array)

        assertEquals(0.0, hist.entropy())
    }

    @Test
    @DisplayName("Half-split array should have maximum entropy")
    fun maximum_entropy() {


        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0)))
        )
        val hist = Histogram(f1, array)

        assertEquals(1.0, hist.entropy())
    }

    @Test
    @DisplayName("should not exceed 1 for maximum entropy")
    fun numeric_maximum_entropy() {
        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f2, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f2, 2))),
            Language("L3", "lang 3", arrayOf(FeatureRealization(f2, 3))),
            Language("L4", "lang 4", arrayOf(FeatureRealization(f2, 4)))
        )
        val hist = Histogram(f2, array)

        assertTrue { hist.entropy() <= 1.0 }
    }

    @Test
    @DisplayName("should calculate conditional entropy")
    fun conditional_entropy(){
        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f2, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f2, 1))),
            Language("L3", "lang 3", arrayOf(FeatureRealization(f2, 2))),
            Language("L4", "lang 4", arrayOf(FeatureRealization(f2, 2)))
        )
        val parentHist = Histogram(f2, array)

        val childArray = array.subList(0, 2)

        val childHist = Histogram(f2, childArray)

        assertEquals(0.0, childHist.entropyGiven(parentHist))
    }
}