package com.lucaspowered.bfk

import java.io.OutputStream

class Interpreter(byteArraySize: Int = 30_000, private val stream: OutputStream = System.out, private var preloaded: String = "") {
    private val memory = CharArray(byteArraySize)
    private var memoryIndex = 0

    fun run(input: String) {
        StaticAnalysis.all(input)
        memoryIndex = 0
        if (memory.any { it > '\u0000' }) memory.forEachIndexed { i, _ -> memory[i] = '\u0000' }
        handle(input)
    }

    private fun handle(input: String) {
        var inputIndex = 0
        while (inputIndex < input.length) {
            if (memoryIndex > memory.lastIndex) throw RuntimeException("Invalid memory index: $memoryIndex")

            when (input[inputIndex]) {
                '+' -> if (memory[memoryIndex].code + 1 > 255) memory[memoryIndex] = '\u0000' else memory[memoryIndex]++

                '-' -> if (memory[memoryIndex].code - 1 < 0) memory[memoryIndex] = '\u00FF' else memory[memoryIndex]--

                '>' -> memoryIndex++

                '<' -> memoryIndex--

                '.' -> {
                    stream.write(memory[memoryIndex].code)
                    stream.flush()
                }

                ',' -> {
                    if (preloaded.isNotEmpty()) {
                        memory[memoryIndex] = preloaded[0]
                        preloaded = preloaded.drop(1)
                    } else {
                        TODO(" Set character mode on terminal and turn it off after program exit ")
                        memory[memoryIndex] = '\u0000'
                    }
                }

                '[' -> {
                    val l = parseLoop(input, inputIndex)
                    while (memory[memoryIndex] > '\u0000') {
                        handle(l)
                    }
                    inputIndex += l.length
                }
            }
            inputIndex++
        }
    }

    private fun parseLoop(input: String, index: Int): String {
        var result = ""
        var bracketCount = 0
        input.substring(index + 1).forEach {
            when {
                it == '[' -> bracketCount++
                it == ']' && bracketCount == 0 -> return result
                it == ']' -> bracketCount--
            }
            result += it
        }
        return result
    }
}