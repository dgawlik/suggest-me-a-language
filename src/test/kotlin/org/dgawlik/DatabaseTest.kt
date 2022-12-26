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

    fun noneOf(languages: Array<Language>, feature: Feature, names: Set<String>): Boolean{
        languages.forEach {
            if (it.features.any { feat ->  feat.feature.id == feature.id && feat.value > 0}  && names.contains(it.name)){
                return false
            }
        }
        return true
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

    @Test
    fun none_of_not_crossplatform_languages(){
        val feature = parser.features.find { it.id == "TARGET2" }!!

        val invalidLanguages = setOf("C#", "F#", "Swift", "Objective C", "Visual Basic", "SAS")

        assert(noneOf(parser.languages, feature, invalidLanguages))
    }

    @Test
    fun all_valid_declarative_languages(){
        val feature = parser.features.find { it.id == "LANG8" }!!

        val validLanguages = setOf("Haskell", "PureScript", "Elm", "Erlang", "Lisp", "Scheme", "Clojure", "Prolog", "Coq", "Julia",
            "F#", "Elixir", "TLA+", "Nickel", "Matlab", "SAS", "R")

        assert(containsExactly(parser.languages, feature, validLanguages))
    }

    @Test
    fun all_valid_languages_with_fibers(){
        val feature = parser.features.find { it.id == "LANG14" }!!

        val validLanguages = setOf("Python", "C++", "Java", "C#", "JavaScript", "Go", "Swift", "Ruby", "Objective C",
            "Rust", "Kotlin", "Scala", "F#", "Erlang", "Groovy", "Elixir", "Clojure", "PureScript", "TypeScript", "Ada")

        assert(containsExactly(parser.languages, feature, validLanguages))
    }
}