package org.dgawlik.domain

data class Language(
    val name: String,
    val description: String,
)

interface Field {
}

data class Binary(val value: String) : Field

data class Numeric(val value: Int, val min: Int, val max: Int) : Field

data class FeatureRealization(val feature: Feature, val weight: Int)

data class Feature(val id: String, val description: String,  val value: Field)