package org.dgawlik.domain

data class Language(
    val name: String,
    val description: String,
    val features: Array<FeatureRealization>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Language

        if (name != other.name) return false
        if (description != other.description) return false
        if (!features.contentEquals(other.features)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + features.contentHashCode()
        return result
    }
}

interface Field {
}

data class Binary(val value: String) : Field

data class Numeric(val value: Int, val min: Int, val max: Int) : Field

data class FeatureRealization(val feature: Feature, val weight: Int)

data class Feature(val id: String, val description: String,  val value: Field)