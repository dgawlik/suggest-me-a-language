package org.dgawlik.parsing

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TableTest {

    @Test
    @DisplayName("Should parse valid table")
    fun valid_table(){
        val text = """
            # Table1
            
            | Header1 | Header2 |
            |---------|---------|
            | Cell1   | Cell2   |
        """.trimIndent()

        val parser = Table(text, 0)

        assertEquals(parser.columns.size, 2)
        assertEquals(parser.rows.size, 1)
        assertEquals(parser.rows[0].size, 2)

        assertEquals(parser.columns[0], "Header1")
        assertEquals(parser.columns[1], "Header2")

        assertEquals(parser.rows[0][0], "Cell1")
        assertEquals(parser.rows[0][1], "Cell2")

        assertEquals(parser.title, "Table1")
    }

    @Test
    @DisplayName("Should throw on no header")
    fun no_title() {
        val text = """
            | Header1 | Header2 |
            |---------|---------|
            | Cell1   | Cell2   |
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "Can't find header" )
    }

    @Test
    @DisplayName("Should throw on empty header")
    fun empty_title() {
        val text = """
            ##
            
            | Header1 | Header2 |
            |---------|---------|
            | Cell1   | Cell2   |
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "Empty header" )
    }

    @Test
    @DisplayName("Should throw on no table")
    fun no_table() {
        val text = """
            ## Table1
            
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "No table found" )
    }

    @Test
    @DisplayName("Should throw on no table")
    fun invalid_table() {
        val text = """
            ## Table1
            
            | Header1 | Header2 |
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "No table found" )
    }

    @Test
    @DisplayName("Should throw on empty table")
    fun empty_table() {
        val text = """
            ## Table1
            
            | Header1 | Header2 |
            |---------|---------|
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "Empty table" )
    }

    @Test
    @DisplayName("Should throw on invalid number of columns")
    fun invalid_table_columns() {
        val text = """
            ## Table1
            
            | Header1 | Header2 |
            |---------|---------|
            | Cell1   |
        """.trimIndent()

        assertThrows(TableParsingException::class.java, { Table(text, 0) }, "Numer of columns not matching" )
    }

    @Test
    @DisplayName("Should parse multiple tables")
    fun multiple_tables() {
        val text = """
            ## Table1
            
            | Header1 | Header2 |
            |---------|---------|
            | Cell1   | Cell2   |
            
            ## Table2
            
            | Header3 | Header4 |
            |---------|---------|
            | Cell3   | Cell4   |
        """.trimIndent()

        val table1 = Table(text, 0)
        val table2 = Table(text, table1.endingPosition)

        assertEquals(table2.columns.size, 2)
        assertEquals(table2.rows.size, 1)
        assertEquals(table2.rows[0].size, 2)

        assertEquals(table2.columns[0], "Header3")
        assertEquals(table2.columns[1], "Header4")

        assertEquals(table2.rows[0][0], "Cell3")
        assertEquals(table2.rows[0][1], "Cell4")

        assertEquals(table2.title, "Table2")
    }
}