package org.dgawlik.parsing

import io.mockk.every
import io.mockk.spyk
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class ParserTest{

    @Test
    @DisplayName("Should parse valid database")
    fun valid_database(){
        val text = """
            ### Languages

            | Language     | Feature | Value | Description |
            |--------------|---------|-------|-------------|
            | Python       | LANG1   | 1     | x           |
            
            ### Features

            | Id       | Description              | Type     | Min/Max |
            |----------|--------------------------|----------|---------|
            | LANG1    | Ease of use              | Binary   | -       |
        """.trimIndent()

        val parser = spyk<Parser>(Parser("mock"))

        every { parser.readDatabaseText("mock") } returns text

        parser.parse()
    }
}