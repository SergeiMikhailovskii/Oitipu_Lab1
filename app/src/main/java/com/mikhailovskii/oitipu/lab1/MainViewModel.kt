package com.mikhailovskii.oitipu.lab1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    val input: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = ""
        }
    }

    val output: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            value = ""
        }
    }

    fun calculatePln() {
        val values: Stack<Double> = Stack()
        var lastSpace = 0
        var secondValue: Double
        var item: String

        val pln = "${convertToReversePolish(input.value ?: "")} "

        for (i in pln.indices) {

            if (pln[i] == ' ') {
                item = pln.substring(lastSpace, i)
                item = item.replace("\\s+".toRegex(), "")

                when (item) {
                    "+" -> {
                        values.push(values.pop() + values.pop())
                    }
                    "-" -> {
                        secondValue = values.pop()
                        values.push(values.pop() - secondValue)
                    }
                    "*" -> {
                        values.push(values.pop() * values.pop())
                    }
                    "/" -> {
                        secondValue = values.pop()
                        values.push(values.pop() / secondValue)
                    }
                    "sqrt" -> {
                        values.push(sqrt(values.pop()))
                    }
                    else -> {
                        values.push(item.toDouble())
                    }
                }
                lastSpace = i
            }
        }

        output.value = if (values.peek() - values.peek().toLong() == 0.0)
            values.peek().toLong().toString() else values.peek().toString()

    }


    private fun convertToReversePolish(exp: String?): String? {
        if (exp == null) return null
        var res = ""
        val len = exp.length
        val operator = Stack<Char>()
        val reversePolish = Stack<String>()

        operator.push('#')

        var i = 0
        while (i < len) {

            //deal with space
            while (i < len && exp[i] == ' ') i++

            if (i == len) break

            if (exp[i].isDigit()) {
                var num = ""
                while (i < len && exp[i].isDigit()) num += exp[i++]
                reversePolish.push(num)
            } else if (isOperator(exp[i])) {

                when (val op = exp[i]) {
                    '(' -> operator.push(op)
                    ')' -> {
                        while (operator.peek() != '(') reversePolish.push(
                            operator.pop().toString()
                        )
                        operator.pop()
                    }
                    '+', '-' -> if (operator.peek() == '(') operator.push(op) else {
                        while (operator.peek() != '#' && operator.peek() != '(') reversePolish.push(
                            operator.pop().toString()
                        )
                        operator.push(op)
                    }
                    '*', '/' -> if (operator.peek() == '(') operator.push(op) else {
                        while (operator.peek() != '#' && operator.peek() != '+' && operator.peek() != '-' && operator.peek() != '('
                        ) reversePolish.push(operator.pop().toString())
                        operator.push(op)
                    }
                }
                i++
            }
        }
        while (operator.peek() != '#') reversePolish.push(operator.pop().toString())
        while (!reversePolish.isEmpty()) res =
            if (res.isEmpty()) reversePolish.pop().toString() + res else reversePolish.pop()
                .toString() + " " + res
        return res
    }

    private fun isOperator(c: Char) = c in "+-*/()"

}