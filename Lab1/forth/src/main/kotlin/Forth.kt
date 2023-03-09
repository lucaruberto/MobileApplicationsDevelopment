import java.lang.Exception
import java.util.*

class Forth {
    fun evaluate(vararg line: String): List<Int> {
        val argList = line.toList()
        val numbers = mutableListOf<Int>()

        for(arg in argList){
            for(a in arg.split(' ')) {
                if (a.toIntOrNull() != null ) { //number
                    numbers.add(a.toInt())
                } else { //operator
                    val operator = a.lowercase(Locale.getDefault())
                    if(numbers.isEmpty())
                        throw Exception("empty stack")
                    if(numbers.size <= 1 && operator != "dup" && operator != "drop") //only dup and drop admit single value
                        throw Exception("only one value on the stack")
                    when (operator) {
                        "+" -> {
                            val n1 = numbers[numbers.size - 1]
                            val n2 = numbers[numbers.size - 2]
                            numbers.removeAt(numbers.size - 1)
                            numbers.removeAt(numbers.size - 1)
                            numbers.add(0, n1+n2)
                        }
                        "-" -> {
                            val n1 = numbers[numbers.size - 2]
                            val n2 = numbers[numbers.size - 1]
                            numbers.removeAt(numbers.size - 1)
                            numbers.removeAt(numbers.size - 1)
                            numbers.add(0, n1-n2)
                        }
                        "*" -> {
                            val n1 = numbers[numbers.size - 1]
                            val n2 = numbers[numbers.size - 2]
                            numbers.removeAt(numbers.size - 1)
                            numbers.removeAt(numbers.size - 1)
                            numbers.add(0, n1*n2)
                        }
                        "/" -> {
                            val n1 = numbers[numbers.size - 2]
                            val n2 = numbers[numbers.size - 1]
                            numbers.removeAt(numbers.size - 1)
                            numbers.removeAt(numbers.size - 1)
                            if(n2 == 0) throw Exception("divide by zero")
                            else numbers.add(0, n1/n2)
                        }
                        "dup" -> {
                            val numberToDup = numbers[numbers.size - 1]
                            numbers.add(numberToDup)
                        }
                        "drop" -> {
                            numbers.removeAt(numbers.size -1 )
                        }
                        "swap" -> {
                            val tmp = numbers[numbers.size - 1]
                            numbers[numbers.size - 1] = numbers[numbers.size - 2]
                            numbers[numbers.size - 2] = tmp
                        }
                        "over" -> {
                            val numberToCopy = numbers[numbers.size - 2]
                            numbers.add(numberToCopy)
                        }
                    }
                }
            }
        }

        return numbers
    }
}
