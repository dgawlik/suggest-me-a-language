package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.domain.NumericField


class SplitterSorterException(msg: String) : RuntimeException(msg)

data class Quad<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)

class SplitterSorter {

    fun sort(languages: Array<Language>, selector: Feature): Array<Language> {
        return languages.sortedBy {
            val value = it.features.find { it2 -> it2.feature.id == selector.id }?.value ?: 0
            value
        }.toTypedArray()
    }

    fun bestSplit(languages: Array<Language>, selector: Feature): Quad<Array<Language>, Array<Language>, Double, Int> {
        val sorted = sort(languages, selector)

        var left: Array<Language> = arrayOf()
        var right: Array<Language> = arrayOf()
        var minEntropy = Double.MAX_VALUE
        var splitVal = -1

        val min: Int
        val max: Int
        if (selector.fieldType is BinaryField) {
            min = 0
            max = 1
        } else if (selector.fieldType is NumericField) {
            min = selector.fieldType.min
            max = selector.fieldType.max
        } else {
            throw SplitterSorterException("Unknown field type")
        }

        for (split in min..max + 1) {
            val (lhs, rhs) = sorted.partition {
                val value = it.features.find { it2 -> it2.feature.id == selector.id }?.value ?: 0
                value < split
            }

            val thisEntropy =
                Histogram(selector, lhs.toTypedArray()).entropy() + Histogram(selector, rhs.toTypedArray()).entropy()

            if (thisEntropy < minEntropy) {
                minEntropy = thisEntropy
                left = lhs.toTypedArray()
                right = rhs.toTypedArray()
                splitVal = split
            }
        }

        return Quad(left, right, minEntropy, splitVal)
    }
}