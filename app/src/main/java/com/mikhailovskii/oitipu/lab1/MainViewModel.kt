package com.mikhailovskii.oitipu.lab1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import javax.script.ScriptEngineManager
import javax.script.ScriptException
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

    fun calculateWithLib() {
        val inputValue = input.value ?: return

        val scriptEngineManager = ScriptEngineManager()
        val scriptEngine = scriptEngineManager.getEngineByName("rhino")

        try {
            val expression = inputValue
                .replace(" ", "")
                .replace("sin", "Math.sin")
                .replace("cos", "Math.cos")
                .replace("tan", "Math.tan")
                .replace("√", "Math.sqrt")
                .replace("exp", "Math.exp")
                .replace("ln", "Math.log")
                .replaceAbs()
                .replacePow()

            val value = scriptEngine.eval(expression) as Double

            if (value.isNaN()) {
                throw ScriptException("")
            }

            output.value =
                if (value - value.toLong() == 0.0) value.toLong().toString() else value.toString()

        } catch (e: ScriptException) {
            output.value = "Expression cannot be evaluated :("
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

    private fun String.replaceAbs() = buildString {
        var inAbsBracket = false
        for (c in this@replaceAbs) {
            if (c == '|') {
                inAbsBracket = !inAbsBracket
                append(if (inAbsBracket) "Math.abs(" else ")")
            } else
                append(c)
        }
    }

    private fun String.replacePow(): String {
        var result = ""
        val elements = this.split('+', '-', '*', '/', '=').toMutableList()
        elements.forEach { s ->
            if (s.contains('^')) {
                val localSplit = s.split('^')
                result = this.replace(s, "Math.pow(${localSplit[0]},${localSplit[1]})")
            }
        }

        return result
    }

    private fun isOperator(c: Char) = c in "+-*/()"

}