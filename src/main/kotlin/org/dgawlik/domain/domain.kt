package org.dgawlik.domain

interface FieldType

class BinaryField: FieldType

data class NumericField(val min: Int, val max: Int) : FieldType

data class Language(
    val name: String,
    val description: String,
    var features: Array<FeatureRealization>
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

data class Feature(val id: String, val description: String,  val fieldType: FieldType)

data class FeatureRealization(val feature: Feature, val value: Int)