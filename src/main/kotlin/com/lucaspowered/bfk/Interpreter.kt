package com.lucaspowered.bfk

import java.io.OutputStream

class Interpreter(private val byteArraySize: Int, private val stream: OutputStream) {
    private val memory = CharArray(byteArraySize)
    private var memoryIndex = 0

    fun parse(input: String) {
        var inputIndex = 0
        while (inputIndex < input.lastIndex) {
            if (memoryIndex > memory.lastIndex)
                throw RuntimeException("Invalid memory index: $memoryIndex")

            when (input[inputIndex]) {
                '+' -> memory[memoryIndex]++

                '-' -> memory[memoryIndex]--

                '>' -> memoryIndex++

                '<' -> memoryIndex--

                '.' -> {
                    stream.write(memory[memoryIndex].code)
                    stream.flush()
                }

                ',' -> {
                    val inputString = readln()
                    if (inputString.isNotEmpty()) memory[memoryIndex] = inputString[0] else memory[memoryIndex] =
                        '\u0000' // null char
                }

                '[' -> {
                    val d = input.substring(inputIndex + 1).takeWhile {it != ']'}
                    while (memory[memoryIndex] > '\u0000') {
                        //println(d)
                        parse(input.substring(inputIndex + 1,  d.length + inputIndex + 1))
                        memory[memoryIndex]--
                    }
                    inputIndex = d.length + inputIndex
                }
            }
            inputIndex++
        }
    }
}