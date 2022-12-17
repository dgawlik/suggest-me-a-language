package org.dgawlik.parsing

import io.mockk.every
import io.mockk.spyk
import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.Feature
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertIs

internal class ParserTest{

    @Test
    @DisplayName("Should parse valid database")
    fun valid_database(){
        val text = """
            ### Languages

            | Language     | Feature | Value | Description |
            |--------------|---------|-------|-------------|
            | Python       | *       | *     | x           |
            | *            | LANG1   | 1     | *           |
            | *            | LANG17  | 5     | *           |
            
            ### Features

            | Id       | Description              | Type     | Min/Max |
            |----------|--------------------------|----------|---------|
            | LANG1    | Ease of use              | Binary   | -       |
            | LANG17   | Verbosity                | Numeric  | 1/10    |
        """.trimIndent()

        val parser = spyk<Parser>(Parser("mock"))

        every { parser.readDatabaseText("mock") } returns text

        parser.parse()

        assertEquals(2, parser.features.size)
        assertEquals(parser.features[0].id, "LANG1")
        assertEquals(parser.features[0].description, "Ease of use")
        assertIs<BinaryField>(parser.features[0].fieldType)

        assertEquals(1, parser.languages.size)
        assertEquals(2, parser.languages[0].features.size)
    }
}