package com.lucaspowered.bfk

@Suppress("Unused")
object StaticAnalysis {
    fun all(input: String) {
        this::class.java.methods.forEach {
            if (it.declaringClass == this::class.java && it.name != "all") it.invoke(this, input)
        }
    }

    fun unmatchedParentheses(input: String) {
        if (input.count {it == ']'} != input.count {it == '['}) {
            throw RuntimeException("Unmatched parentheses")
        }
    }

    // More functions here when ideas come
}