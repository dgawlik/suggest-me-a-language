package org.dgawlik

import org.dgawlik.domain.BinaryField
import org.dgawlik.domain.NumericField
import org.dgawlik.parsing.Parser
import java.lang.RuntimeException
import java.util.*


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw RuntimeException("Path to database required.")
    }

    val parser = Parser(args[0])
    parser.parse()

    println("== Fill in language criteria ==\n")

    var text = ""
    parser.features.forEach {
        var prompt = it.description + " "

        var min: Int
        var max: Int
        prompt += if (it.fieldType is BinaryField) {
            min = 0
            max = 1
            "[0/1]: "
        } else {
            val ft = it.fieldType as NumericField
            min = ft.min
            max = ft.max
            "[${ft.min}/${ft.max}]: "
        }

        print(prompt)

        val answer =  Scanner(System.`in`).nextInt()

        if(answer < min || answer > max) {
            throw RuntimeException("Unexpected answer")
        }

        val line = String.format("|%-14s|%-10s|%-7d|%-423s|\n", " *", it.id, answer, " *")
        text += line
    }

    println(text)

    println("Done!")
}