package com.lucaspowered.bfk

import java.io.OutputStream

class Interpreter(byteArraySize: Int, private val stream: OutputStream) {
    private val memory = CharArray(byteArraySize)
    private var memoryIndex = 0

    fun run(input: String) {
        memoryIndex = 0
        if (memory.any {it > '\u0000'})
            memory.forEachIndexed {i, _ -> memory[i] = '\u0000' }
        handle(input)
    }

    private fun handle(input: String) {
        var inputIndex = 0
        while (inputIndex < input.length) {
            if (memoryIndex > memory.lastIndex)
                throw RuntimeException("Invalid memory index: $memoryIndex")

            when (input[inputIndex]) {
                '+' -> if (memory[memoryIndex].code + 1 > 255) memory[memoryIndex] = '\u0000' else memory[memoryIndex]++

                '-' -> if (memory[memoryIndex].code - 1 < 0) memory[memoryIndex] = '\u00FF' else memory[memoryIndex]--

                '>' -> memoryIndex++

                '<' -> memoryIndex--

                '.' -> {
                    //stream.write(memory[memoryIndex].code)
                    stream.write(memory[memoryIndex].code)
                    stream.flush()
                }

                ',' -> {
                    val inputString = readln()
                    if (inputString.isNotEmpty()) memory[memoryIndex] = inputString[0] else memory[memoryIndex] =
                        '\u0000' // null char
                }

                '[' -> {
                    val l = parseLoop(input, inputIndex)
                    //println(l)
                    while (memory[memoryIndex] > '\u0000') {
                        //println(d)
                        handle(l)
                        //if (memory[memoryIndex].code - 1 < 0) memory[memoryIndex] = '\u0000' else memory[memoryIndex]--
                    }
                    inputIndex += l.length
                }
            }
            //memory.forEachIndexed{i, c -> print("${if (i == memoryIndex) '*' else ""}${c.code}, ")}
            //print('\n')
            inputIndex++
        }
    }

    private fun parseLoop(input: String, index: Int): String {
        val slice = input.substring(index + 1)
        var result = ""
        var bracketCount = 0
        for (i in slice) {
            if (i == '[') {
                bracketCount++
                result += i
            }
            else if (i == ']' && bracketCount == 0)
                return result
            else if (i == ']') {
                bracketCount--
                result += i
            }
            else
                result += i
        }
        return result
    }
}