package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class SplitterSorterTest {

    val f1 = Feature("LANG1", "first feature", BinaryField())

    @Test
    @DisplayName("Should sort correctly")
    fun should_sort() {
        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0)))
        )

        val result = SplitterSorter().sort(array, f1)

        assertEquals(0, result[0].features[0].value)
        assertEquals(1, result[1].features[0].value)
    }

    @Test
    @DisplayName("Should find best split")
    fun best_split() {
        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 0))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0))),
            Language("L3", "lang 3", arrayOf(FeatureRealization(f1, 0))),
            Language("L4", "lang 4", arrayOf(FeatureRealization(f1, 1))),
            Language("L5", "lang 5", arrayOf(FeatureRealization(f1, 1)))
        )

        val (left, right) = SplitterSorter().bestSplit(array, f1)
        assertEquals(3, left.size)
        assertEquals(2, right.size)

        assertArrayEquals(arrayOf("L1", "L2", "L3"), left.map { it.name }.toTypedArray())
    }

    @Test
    @DisplayName("Should find best split in corner case")
    fun best_split_corner_case() {
        val array = arrayListOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 0))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0))),
        )

        val (left, right) = SplitterSorter().bestSplit(array, f1)
        assertEquals(2, left.size)
        assertEquals(0, right.size)

        assertArrayEquals(arrayOf("L1", "L2"), left.map { it.name }.toTypedArray())
    }
}