package org.dgawlik.parsing

import org.dgawlik.domain.Feature
import org.dgawlik.domain.Language
import java.io.File


fun <T : Throwable> check(failMessage: String, ctor: (msg: String) -> T, pred: () -> Boolean) {
    if (!pred()) {
        throw ctor(failMessage)
    }
}


class TableParsingException(msg: String) : RuntimeException(msg)
class ParserException(msg: String) : RuntimeException(msg)

class Table(text: String, position: Int) {

    var title: String = ""
    var columns: Array<String> = arrayOf()
    var rows: Array<Array<String>> = arrayOf()
    var endingPosition: Int = position

    init {
        var pos: Int
        val (retS, p) = readHeader(text, position)
        this.title = retS
        pos = p

        val (headers, p2) = readTableLine(text, pos)
        check("No table found", ::TableParsingException) {headers.isNotEmpty()}
        this.columns = headers
        pos = p2

        val (separators, p3) = readTableLine(text, pos)
        check("No table found", ::TableParsingException) {separators.isNotEmpty()}
        check("Numer of columns not matching", ::TableParsingException) { separators.size == columns.size }
        pos = p3

        pos = takeWhile(text, pos) { Character.isWhitespace(it) }

        while (pos < text.length && text[pos] == '|') {
            val (line, p4) = readTableLine(text, pos)
            check("Numer of columns not matching", ::TableParsingException) { line.size == columns.size }
            pos = p4
            rows += line

            pos = takeWhile(text, pos) { Character.isWhitespace(it) }
        }

        check("Empty table", ::TableParsingException) {rows.isNotEmpty()}

        endingPosition = pos
    }

    private fun readHeader(text: String, cursor: Int): Pair<String, Int> {
        var cur = cursor;

        cur = takeWhile(text, cur) { it != '#' }
        check("Can't find header", ::TableParsingException) { cur != text.length }
        cur = takeWhile(text, cur) { it == '#' }
        check("Empty header", ::TableParsingException) { cur != text.length && text[cur] != '\n' }

        val snapshot = cur;
        cur = takeWhile(text, cur) { it != '\n' }

        return Pair(text.substring(snapshot until cur).trim(), cur)
    }

    private fun readTableLine(text: String, position: Int): Pair<Array<String>, Int> {
        var arr: Array<String> = arrayOf();

        var pos = position

        pos = takeWhile(text, pos) {it != '|'}

        while (pos < text.length) {
            val snapshot = pos + 1
            pos = takeWhile(text, pos + 1) { it != '|' && it != '\n' }

            if (pos == text.length || text[pos] == '\n') {
                check("Unexpected trailer of table row", ::TableParsingException) {
                    val token = text.substring(snapshot until pos)
                    token.all() { Character.isWhitespace(it) }
                }
                break
            }

            val token = text.substring(snapshot until pos)
            arr += token.trim()
        }

        return Pair(arr, pos)
    }


    private fun takeWhile(text: String, start: Int, pred: (Char) -> Boolean): Int {
        var cur = start;
        while (cur < text.length && pred(text[cur])) {
            cur++;
        }
        return cur;
    }
}


class Parser(val databasePath: String) {

    var languages: Array<Language> = arrayOf()
    var features: Array<Feature> = arrayOf()

    fun parse() {
        val text = readDatabaseText(databasePath)

        val languagesTable = Table(text, 0)

        check("Doesn't have title", ::ParserException) { languagesTable.title == "Languages" }
        check("Doesn't have 4 columns", ::ParserException) { languagesTable.columns.size == 4 }
        check("Doesn't have Language column", ::ParserException) { languagesTable.columns[0] == "Language" }
        check("Doesn't have Feature column", ::ParserException) { languagesTable.columns[1] == "Feature" }
        check("Doesn't have Value column", ::ParserException) { languagesTable.columns[2] == "Value" }
        check("Doesn't have Description column", ::ParserException) { languagesTable.columns[3] == "Description" }

        val featuresTable = Table(text, languagesTable.endingPosition)

        check("Doesn't have title", ::ParserException) { featuresTable.title == "Features" }
        check("Doesn't have 4 columns", ::ParserException) { featuresTable.columns.size == 4 }
        check("Doesn't have Language column", ::ParserException) { featuresTable.columns[0] == "Id" }
        check("Doesn't have Feature column", ::ParserException) { featuresTable.columns[1] == "Description" }
        check("Doesn't have Value column", ::ParserException) { featuresTable.columns[2] == "Type" }
        check("Doesn't have Description column", ::ParserException) { featuresTable.columns[3] == "Min/Max" }

    }

    fun readDatabaseText(databasePath: String): String {
        return File(databasePath).readText()
    }
}