package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.domain.NumericField
import kotlin.math.abs
import kotlin.math.min


class SplitterSorterException(msg: String) : RuntimeException(msg)

data class Quadruple<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)


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

    fun bestSplit(languages: List<Language>, selector: Feature): Quadruple<List<Language>, List<Language>, Int, Double> {
        val sorted = sort(languages, selector)

        var left: List<Language> = arrayListOf()
        var right: List<Language> = arrayListOf()
        var splitVal = -1
        var penalty = 2.0

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

            val m = min(lhs.size, rhs.size)
            val t = (lhs.size + rhs.size).toDouble()
            val p = 2*(t/2-m)/t
            if (p < penalty) {
                splitVal = split
                penalty = p
                left = lhs
                right = rhs
            }

        }

        return Quadruple(left, right, splitVal, penalty)
    }
}