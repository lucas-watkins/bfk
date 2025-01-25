package com.lucaspowered.bfk

import picocli.CommandLine
import picocli.CommandLine.Command
import java.io.File
import picocli.CommandLine.Option
import java.util.concurrent.Callable
import kotlin.system.exitProcess

@Command(name = "bfk", mixinStandardHelpOptions = true, description = ["Kotlin brainf*** interpreter"])
class CliInterpreter : Callable<Int> {

    @Option(names = ["-f", "-file"], description = ["File to interpret"], defaultValue = "")
    private lateinit var file: File

    @Option(names = ["-i", "-input"], description = ["Preloaded input string"], defaultValue = "")
    private lateinit var preloaded: String

    @Option(names = ["-c", "-code"], description = ["Input code as argument"], defaultValue = "")
    private lateinit var codeInput: String

    override fun call(): Int {
        val interpreter = Interpreter(preloaded = this.preloaded)

        when {
            file.path != "" && codeInput != "" -> {
                println("Code file and code as argument are mutually exclusive.")
            }

            file.path != "" -> {
                interpreter.run(file.readText())
            }

            codeInput != "" -> {
                interpreter.run(codeInput)
            }

            else -> {
                val help = CommandLine(this).help
                println("${help.fullSynopsis()}" +
                        "${help.optionList()}" +
                        "${help.footer()}")
            }
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(CliInterpreter()).execute(*args))
