package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.domain.NumericField
import kotlin.math.abs


class SplitterSorterException(msg: String) : RuntimeException(msg)


class SplitterSorter {

    fun sort(languages: Array<Language>, selector: Feature): Array<Language> {
        return languages.sortedBy {
            val value = it.features.find { it2 -> it2.feature.id == selector.id }?.value ?: 0
            value
        }.toTypedArray()
    }

    fun bestSplit(languages: Array<Language>, selector: Feature): Triple<Array<Language>, Array<Language>, Int> {
        val sorted = sort(languages, selector)
        val sortedHist = Histogram(selector, sorted)

        var left: Array<Language> = arrayOf()
        var right: Array<Language> = arrayOf()
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

        for (split in min+1..max + 1) {
            val (lhs, rhs) = sorted.partition {
                val value = it.features.find { it2 -> it2.feature.id == selector.id }?.value ?: 0
                value < split
            }

            val distance = abs(languages.size/2 - lhs.size)
            if(distance < splitDistance) {
                splitVal = split
                splitDistance = distance
                left = lhs.toTypedArray()
                right = rhs.toTypedArray()
            }

        }

        return Triple(left, right, splitVal)
    }
}