package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language
import org.dgawlik.parsing.Parser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class HistogramTest {

    val f1 = Feature("LANG1", "first feature", BinaryField())

    @Test
    @DisplayName("Uniform array should have 0 entropy")
    fun zero_entropy() {


        val array = arrayOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 1)))
        )

        val hist = Histogram(f1, array)

        assertEquals(0.0, hist.entropy())
    }

    @Test
    @DisplayName("Half-split array should have maximum entropy")
    fun maximum_entropy() {


        val array = arrayOf(
            Language("L1", "lang 1", arrayOf(FeatureRealization(f1, 1))),
            Language("L2", "lang 2", arrayOf(FeatureRealization(f1, 0)))
        )
        val hist = Histogram(f1, array)

        assertEquals(1.0, hist.entropy())
    }

    @Test
    @DisplayName("x")
    fun x() {
        val db = BinaryField::class.java.getResource("/Database.md")!!.readText()
        val parser = Parser(db)
        parser.parse()

        val hist = Histogram(parser.features[0], parser.languages)
        hist.entropy()
    }
}