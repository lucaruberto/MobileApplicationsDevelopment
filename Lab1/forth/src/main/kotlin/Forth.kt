import java.lang.Exception
class Forth {

    fun evaluate(vararg line: String): List<Int> {
        val intList: MutableList<Int> = mutableListOf()
        val userDefinedCommands: MutableMap<String,List<String>> = mutableMapOf()
        for(rawCommand in line){
            val command = rawCommand.lowercase()
            if(command.startsWith(":")){
                //user command definition
                val cleanCommand = command.substring(2,command.length-2) //erase ": " and " ;"
                val splitCommand = cleanCommand.split(" ").toMutableList()
                if(splitCommand[0].toIntOrNull() != null){
                    //redefining numbers
                    throw Exception("illegal operation")
                }
                var i = 1   //ignore the first that is the name of the command
                while (i < splitCommand.size){
                    var c = splitCommand[i]
                    while(userDefinedCommands.containsKey(c)){
                        // translate user defined commands to base commands, to handle usage and redefinition
                        splitCommand.addAll(i+1, userDefinedCommands.getValue(c))
                        splitCommand.removeAt(i)
                        c = splitCommand[i]
                    }
                    i++
                }
                userDefinedCommands[splitCommand[0]] = splitCommand.drop(1)
            }
            else {
                val stringList = command.split(" ").toMutableList()
                var i = 0
                while (i < stringList.size) {
                    val stringValue = stringList[i]
                    val intValue = stringValue.toIntOrNull()
                    if (intValue != null) {
                        //string to int conversion successful
                        intList.add(intValue)
                    } else {
                        //not an int, so is a command
                        val size = intList.size
                        if (userDefinedCommands.containsKey(stringValue)) {
                            //command already present and must be applied
                            stringList.addAll(i + 1, userDefinedCommands.getValue(stringValue))
                        }
                        else {
                            when (stringValue) {
                                "+" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    }
                                    intList[size - 2] =
                                        intList[size - 2] + intList[size - 1] //second-last + last
                                    intList.removeLast() //remove last value
                                }
                                "-" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    }
                                    intList[size - 2] =
                                        intList[size - 2] - intList[size - 1] //second-last - last
                                    intList.removeLast() //remove last value
                                }
                                "*" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    }
                                    intList[size - 2] =
                                        intList[size - 2] * intList[size - 1] //second-last * last
                                    intList.removeLast() //remove last value
                                }
                                "/" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    } else if (intList.last() == 0) {
                                        throw Exception("divide by zero")
                                    }
                                    intList[size - 2] =
                                        intList[size - 2] / intList[size - 1] //second-last / last
                                    intList.removeLast() //remove last value
                                }
                                "dup" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    intList.add(intList.last())
                                }
                                "drop" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    intList.removeLast()
                                }
                                "swap" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    }
                                    val secondLast = intList[size - 2]
                                    intList[size - 2] = intList[size - 1]
                                    intList[size - 1] = secondLast
                                }
                                "over" -> {
                                    if (size == 0) {
                                        throw Exception("empty stack")
                                    }
                                    if (size == 1) {
                                        throw Exception("only one value on the stack")
                                    }
                                    intList.add(intList[size - 2])
                                }
                                else -> {
                                    //not user defined nor developer defined
                                    throw Exception("undefined operation")
                                }
                            }
                        }
                    }
                    i++
                }
            }
        }
        return intList
    }
}

