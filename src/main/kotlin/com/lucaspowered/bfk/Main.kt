package com.lucaspowered.bfk

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "bfk", mixinStandardHelpOptions = true, description = ["Kotlin brainf*** interpreter"])
class CliInterpreter : Callable<Int> {

    @Parameters(index = "0", description = ["Path to your code file"])
    private lateinit var file: File

    override fun call(): Int {
        val interpreter = Interpreter()
        interpreter.run(file.readText())
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(CliInterpreter()).execute(*args))
