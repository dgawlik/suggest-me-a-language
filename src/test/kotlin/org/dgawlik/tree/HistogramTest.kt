package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class HistogramTest{

    val f1 = Feature("LANG1", "first feature", BinaryField())

    @Test
    @DisplayName("Uniform array should have 0 entropy")
    fun zero_entropy() {


        val array = arrayOf(FeatureRealization(f1, 1), FeatureRealization(f1, 1))

        val hist = Histogram(f1, array)

        assertEquals(0.0, hist.entropy())
    }

    @Test
    @DisplayName("Half-split array should have maximum entropy")
    fun maximum_entropy() {


        val array = arrayOf(FeatureRealization(f1, 1), FeatureRealization(f1, 0))

        val hist = Histogram(f1, array)

        assertEquals(1.0, hist.entropy())
    }
}