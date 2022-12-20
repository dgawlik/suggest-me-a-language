package org.dgawlik

import org.dgawlik.domain.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ServicesKtTest {

    @Test
    fun printListLanguages() {
        val languages = arrayOf(
            Language("Lang1", "dynamic language", arrayOf()),
            Language("Lang2", "static language", arrayOf()),
        )

        val result = printListLanguages(languages)

        assertEquals(
            """[1] Lang1
            |[2] Lang2
            |""".trimMargin(), result
        )
    }

    @Test
    fun printListFeatures() {
        val f1 = Feature("FEAT1", "description1", BinaryField())
        val f2 = Feature("FEAT2", "description2", BinaryField())
        val f3 = Feature("FEAT3", "description3", NumericField(1, 10))

        val features = arrayOf(
            FeatureRealization(f1, 0),
            FeatureRealization(f2, 1),
            FeatureRealization(f3, 5)
        )

        val result = printListFeatures(features)

        assertEquals(
            """[*] description2
            |[*] description3 = MODERATE
            |
        """.trimMargin(), result
        )
    }

    @Test
    fun languagesMatchingFeatures() {
        val f1 = Feature("FEAT1", "description1", BinaryField())
        val f2 = Feature("FEAT2", "description2", BinaryField())
        val f3 = Feature("FEAT3", "description3", NumericField(1, 10))

        val lang1 = Language(
            "LANG1", "lang desc 1", arrayOf(
                FeatureRealization(f1, 1),
                FeatureRealization(f3, 5)
            )
        )

        val lang2 = Language(
            "LANG2", "lang desc 2", arrayOf(
                FeatureRealization(f2, 1),
                FeatureRealization(f3, 10)
            )
        )

        val res1 = languagesMatchingFeatures(
            arrayOf(lang1, lang2), arrayOf(
                FeatureRealization(f1, 1),
                FeatureRealization(f3, 4)
            )
        ).map { it.name }.toTypedArray()

        assertArrayEquals(arrayOf("LANG1"), res1)

        val res2 = languagesMatchingFeatures(
            arrayOf(lang1, lang2), arrayOf(
                FeatureRealization(f1, 1),
                FeatureRealization(f3, 6)
            )
        ).map { it.name }.toTypedArray()

        assertArrayEquals(arrayOf(), res2)

        val res3 = languagesMatchingFeatures(
            arrayOf(lang1, lang2), arrayOf(
                FeatureRealization(f2, 1),
                FeatureRealization(f3, 6)
            )
        ).map { it.name }.toTypedArray()

        assertArrayEquals(arrayOf("LANG2"), res3)
    }
}