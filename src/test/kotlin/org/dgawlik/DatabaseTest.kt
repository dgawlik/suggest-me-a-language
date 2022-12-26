package org.dgawlik

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import org.dgawlik.parsing.Parser
import org.junit.jupiter.api.Test

internal class DatabaseTest {

    val parser: Parser

    init {
        val db = BinaryField::class.java.getResource("/Database.md")!!.readText()
        parser = Parser(db)
        parser.parse()
    }

    fun containsExactly(languages: Array<Language>, feature: Feature, names: Set<String>): Boolean{
        val found: MutableSet<String> = mutableSetOf()

        languages.forEach {
            if (it.features.any { feat ->  feat.feature.id == feature.id && feat.value > 0} ){
                found += it.name
            }
        }

        return names == found
    }

    @Test
    fun all_valid_languages_with_aspects(){
        val feature = parser.features.find { it.id == "LANG27" }!!

        val validLanguages = setOf("Java", "C#", "Kotlin", "Scala", "Groovy")

        assert(containsExactly(parser.languages, feature, validLanguages))
    }

    @Test
    fun all_valid_reflective_languages(){
        val feature = parser.features.find { it.id == "LANG29" }!!

        val validLanguages = setOf("Java", "C#", "F#", "Scala", "Kotlin", "Groovy", "Python", "Ruby", "JavaScript", "Go",
            "Objective C", "Lua", "Lisp", "Scheme", "Haxe", "TypeScript")

        assert(containsExactly(parser.languages, feature, validLanguages))
    }
}