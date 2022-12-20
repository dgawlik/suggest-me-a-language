package org.dgawlik

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.FeatureRealization
import org.dgawlik.domain.Language


fun printListLanguages(langs: Array<Language>): String {
    var s = ""

    langs.forEachIndexed { index, language ->
        s += "[${index + 1}] ${language.name}\n"
    }

    return s
}

fun printListFeatures(features: Array<FeatureRealization>): String {
    val quantifiers = arrayOf("VERY WEAK", "WEAK", "MODERATE", "STRONG", "VERY STRONG")

    var s = ""
    features.forEach {
        if (it.feature.fieldType is BinaryField) {
            if (it.value > 0) {
                s += "[*] ${it.feature.description}\n"
            }
        } else {
            s += "[*] ${it.feature.description} = ${quantifiers[it.value / 2]}\n"
        }
    }

    return s
}


fun languagesMatchingFeatures(langs: Array<Language>, features: Array<FeatureRealization>): Array<Language> {
    return langs.filter {
        features.all { it2 -> it.features.any { it3 -> it3.feature.id == it2.feature.id && it3.value >= it2.value } }
    }.toTypedArray()
}