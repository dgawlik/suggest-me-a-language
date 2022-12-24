package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.domain.NumericField
import kotlin.math.abs


class SplitterSorterException(msg: String) : RuntimeException(msg)


class SplitterSorter {

    fun sort(languages: List<Language>, selector: Feature): List<Language> {
        val index = languages.getOrNull(0)?.features?.indexOfFirst { it.feature.id == selector.id } ?: -1
        if (index == -1) {
            throw SplitterSorterException("Feature not found")
        }

        val map = languages.associate { it.name to it.features[index] }

        return languages.sortedBy {
            map[it.name]!!.value
        }
    }

    fun bestSplit(languages: List<Language>, selector: Feature): Triple<List<Language>, List<Language>, Int> {
        val sorted = sort(languages, selector)

        var left: List<Language> = arrayListOf()
        var right: List<Language> = arrayListOf()
        var splitVal = -1
        var splitDistance = languages.size

        val min: Int
        val max: Int
        when (selector.fieldType) {
            is BinaryField -> {
                min = 0
                max = 1
            }

            is NumericField -> {
                min = selector.fieldType.min
                max = selector.fieldType.max
            }

            else -> {
                throw SplitterSorterException("Unknown field type")
            }
        }

        val index = languages.getOrNull(0)?.features?.indexOfFirst { it.feature.id == selector.id } ?: -1
        if (index == -1) {
            throw SplitterSorterException("Feature not found")
        }

        val map = languages.associate { it.name to it.features[index] }

        for (split in min + 1..max + 1) {
            val (lhs, rhs) = sorted.partition {
                map[it.name]!!.value < split
            }

            val distance = abs(languages.size / 2 - lhs.size)
            if (distance < splitDistance) {
                splitVal = split
                splitDistance = distance
                left = lhs
                right = rhs
            }

        }

        return Triple(left, right, splitVal)
    }
}