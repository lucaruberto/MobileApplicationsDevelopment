class Forth {

    fun evaluate(vararg line: String): List<Int> {
        val intList: MutableList<Int> = mutableListOf()
        for(command in line){
            val stringList = command.split(" ")
            for (stringValue in stringList){
                val intValue = stringValue.toIntOrNull()
                if(intValue != null){
                    intList.add(intValue)
                }
                else{
                    //command handler
                    val size = intList.size
                    if(size == 0) {
                        throw Exception("empty stack")
                    }
                    when (stringValue) {
                        "+" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            intList[size-2] = intList[size-2] + intList[size-1] //second-last + last
                            intList.removeLast() //remove last value
                        }
                        "-" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            intList[size-2] = intList[size-2] - intList[size-1] //second-last - last
                            intList.removeLast() //remove last value
                        }
                        "*" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            intList[size-2] = intList[size-2] * intList[size-1] //second-last * last
                            intList.removeLast() //remove last value
                        }
                        "/" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            else if(intList.last() == 0) {
                                throw Exception("divide by zero")
                            }
                            intList[size-2] = intList[size-2] / intList[size-1] //second-last / last
                            intList.removeLast() //remove last value
                        }
                        "dup" -> {
                            intList.add(intList.last())
                        }
                        "drop" -> {
                            intList.removeLast()
                        }
                        "swap" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            val secondLast = intList[size-2]
                            intList[size-2] = intList[size-1]
                            intList[size-1] = secondLast
                        }
                        "over" -> {
                            if(size == 1) {
                                throw Exception("only one value on the stack")
                            }
                            intList.add(intList[size-2])
                        }
                    }
                }
            }
        }
        return intList
    }
}
