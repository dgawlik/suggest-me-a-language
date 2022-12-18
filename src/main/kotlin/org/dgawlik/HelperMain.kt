package org.dgawlik

import org.dgawlik.parsing.Parser
import java.lang.RuntimeException


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw RuntimeException("Path to database required.")
    }

    val parser = Parser(args[0])
    parser.parse()

    println("Hello World 2!")
}