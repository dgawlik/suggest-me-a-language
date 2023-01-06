package org.dgawlik.parsing

import org.dgawlik.domain.BinaryField
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

internal class ParserTest {

    @Test
    @DisplayName("Should parse valid database")
    fun valid_database() {
        val text = """
            ### Languages

            | Language     | FeatureValues       | Description | Gist |
            |--------------|---------------------|-------------|------|
            | Python       | LANG1{1} LANG17{5}  | x           | -    |
            
            ### Features

            | Id       | Description              | Type     | Min/Max | Generality |
            |----------|--------------------------|----------|---------|------------|
            | LANG1    | Ease of use              | Binary   | -       | 1          |
            | LANG17   | Verbosity                | Numeric  | 1/10    | 1          |
        """.trimIndent()


        val parser = Parser(text)

        parser.parse()

        assertEquals(2, parser.features.size)
        assertEquals(parser.features[0].id, "LANG1")
        assertEquals(parser.features[0].description, "Ease of use")
        assertIs<BinaryField>(parser.features[0].fieldType)

        assertEquals(1, parser.languages.size)
        assertEquals(2, parser.languages[0].features.size)
    }

    @Test
    @DisplayName("Should throw on numeric field out of bounds")
    fun numeric_field_out_of_bounds() {
        val text = """
            ### Languages

            | Language     | FeatureValues       | Description | Gist |
            |--------------|---------------------|-------------|------|
            | Python       | LANG1{1} LANG17{11} | x           | -    |
            
            ### Features

            | Id       | Description              | Type     | Min/Max | Generality |
            |----------|--------------------------|----------|---------|------------|
            | LANG1    | Ease of use              | Binary   | -       | 1          |
            | LANG17   | Verbosity                | Numeric  | 1/10    | 1          |
        """.trimIndent()

        val parser = Parser(text)

        Assertions.assertThrows(ParserException::class.java, { parser.parse() }, "Numeric field out of bounds")
    }

    @Test
    @DisplayName("Should throw on unknown field type")
    fun unknown_field_type() {
        val text = """
            ### Languages

            | Language     | FeatureValues       | Description | Gist |
            |--------------|---------------------|-------------|------|
            | Python       | LANG1{1} LANG17{7}  | x           | -    |
            
            ### Features

            | Id       | Description              | Type     | Min/Max | Generality |
            |----------|--------------------------|----------|---------|------------|
            | LANG1    | Ease of use              | Binary   | -       | 1          |
            | LANG17   | Verbosity                | RegeX    | 1/10    | 1          |
        """.trimIndent()

        val parser = Parser(text)

        Assertions.assertThrows(ParserException::class.java, { parser.parse() }, "Field not numeric or binary")
    }

    @Test
    @DisplayName("Should throw on invalid bounds definition")
    fun invalid_bounds_definition() {
        val text = """
            ### Languages

            | Language     | FeatureValues       | Description | Gist |
            |--------------|---------------------|-------------|------|
            | Python       | LANG1{1} LANG17{7}  | x           | -    |
            
            ### Features

            | Id       | Description              | Type     | Min/Max | Generality |
            |----------|--------------------------|----------|---------|------------|
            | LANG1    | Ease of use              | Binary   | -       | 1          |
            | LANG17   | Verbosity                | RegeX    | a/b     | 1          |
        """.trimIndent()

        val parser = Parser(text)

        Assertions.assertThrows(ParserException::class.java, { parser.parse() }, "Bounds pattern not matching")
    }

    @Test
    @DisplayName("Should throw on empty cell")
    fun empty_cell() {
        val text = """
            ### Languages

            | Language     | FeatureValues       | Description | Gist |
            |--------------|---------------------|-------------|------|
            | Python       |                     | x           | -    |
            
            ### Features

            | Id       | Description              | Type     | Min/Max | Generality |
            |----------|--------------------------|----------|---------|------------|
            | LANG1    | Ease of use              | Binary   | -       | 1          |
            | LANG17   | Verbosity                | RegeX    | a/b     | 1          |
        """.trimIndent()

        val parser = Parser(text)

        Assertions.assertThrows(ParserException::class.java, { parser.parse() }, "Has blank fields")
    }
}