package org.dgawlik.tree

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.domain.NumericField
import kotlin.math.log


class HistogramException(msg: String) : RuntimeException(msg)

class Histogram(selector: Feature, array: Array<Language>) {

    var counts: Array<Int> = arrayOf()
    var total: Int

    init {
        counts = when (selector.fieldType) {
            is BinaryField -> {
                Array(2) { 0 }
            }

            is NumericField -> {
                val size = with(selector.fieldType) { max - min + 1 }
                Array(size) { 0 }
            }

            else -> {
                throw HistogramException("Unknown selector type.")
            }
        }

        val min: Int = if (selector.fieldType is NumericField) {
            selector.fieldType.min
        } else {
            0
        }

        array.forEach {
            val fr = it.features.find { it.feature.id == selector.id }?.value ?: throw HistogramException("Feature not found")
            counts[fr - min]++
        }

        total = counts.sum()
    }

    fun entropy(): Double {
        var sum = 0.0
        counts.forEach {
            val p = (it.toDouble()) / (total.toDouble())
            if (p > 0) {
                sum -= p * log(p, counts.size.toDouble())
            }
        }
        return sum
    }

    fun entropyGiven(parent: Histogram): Double {
        var sum = 0.0
        for( (index, value) in counts.withIndex() ) {
            val pA = total.toDouble()/parent.total.toDouble()
            val pB = parent.counts[index].toDouble() / parent.total.toDouble()
            val pBA = (value.toDouble()) / total.toDouble()

            if (pA > 0 && pBA > 0 && pB > 0) {
                sum -= (pA*pBA/pB) * log(pA*pBA/pB, counts.size.toDouble())
            }
        }
        return sum
    }
}