import java.util.*
import kotlin.collections.ArrayDeque

class Forth {
    private var  stack = ArrayDeque<Int>()
    private var customWords : MutableMap<String,List<String>> = mutableMapOf()
    fun evaluate(vararg line: String): List<Int> {
        stack.clear()
        for( str in line) {
            if (str.startsWith(":")) { //manage user defined commands
                val command = str.substring(2,str.length-2).uppercase(Locale.getDefault())
                val splitCommand = command.split(" ").toMutableList()
                if(splitCommand[0].toIntOrNull()!=null)
                    throw  Exception ("illegal operation")
                
                var i=1
                while(i<splitCommand.size) {
                    var c=splitCommand[i]
                    while(customWords.containsKey(c)) {
                        splitCommand.addAll(i+1,customWords.getValue(c))
                        splitCommand.removeAt(i)
                        c=splitCommand[i]
                    }
                    i++
                }
                customWords[splitCommand[0].uppercase()] = splitCommand.drop(1)
            }
            else //manage other cases (numbers or default commands)
                str.split(" ").forEach{ manageStack(it) }
        }
        return stack.toList()
    }

    private fun manageStack(value:String){
        if(customWords.containsKey(value.uppercase(Locale.getDefault())))
            for(word in customWords.getValue(value.uppercase())) 
                operations(word.uppercase())
        else 
            operations(value.uppercase(Locale.getDefault()))
        return
    }

    private fun operations(check:String) {
        when (check) {
            "+"->   {
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                if(stack.isEmpty())
                    throw  Exception("empty stack")
                val firstOp= stack.last()
                stack.removeLast()
                val secondOp = stack.last()
                stack.removeLast()
                stack.add(secondOp+firstOp)
            }
            "-"->   {
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                if(stack.isEmpty())
                    throw  Exception("empty stack")
                
                val firstOp= stack.last()
                stack.removeLast()
                val secondOp = stack.last()
                stack.removeLast()
                stack.add(secondOp-firstOp)}

            "*"->    {
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                if(stack.isEmpty())
                    throw  Exception("empty stack")

                val firstOp= stack.last()
                stack.removeLast()
                val secondOp = stack.last()
                stack.removeLast()
                stack.add(secondOp*firstOp)
            }
            "/"->    {
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                if(stack.isEmpty())
                    throw  Exception("empty stack")

                val firstOp= stack.last()
                stack.removeLast()
                val secondOp = stack.last()
                stack.removeLast()
                if(firstOp==0)
                {
                    throw  Exception("divide by zero")
                }
                stack.add(secondOp/firstOp)
            }
            "DUP"->    {
                if(stack.size==0)
                    throw  Exception("empty stack")

                val firstOp= stack.last()
                stack.add(firstOp)
            }
            "DROP"->    {
                if(stack.size==0)
                    throw  Exception("empty stack")

                stack.removeLast()
            }
            "SWAP"->   {
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                if(stack.isEmpty())
                    throw  Exception("empty stack")

                val firstOp=stack.last()
                stack.removeLast()
                val secondOp=stack.last()
                stack.removeLast()
                stack.add(firstOp)
                stack.add(secondOp)
            }
            "OVER"->   {
                if(stack.size==0)
                    throw  Exception("empty stack")
                if(stack.size==1)
                    throw Exception("only one value on the stack")
                val firstOp= stack[stack.size-2]
                stack.add(firstOp)
            }
            else -> {
                if(check.toIntOrNull()!=null)
                    stack.add(check.toInt())
                else throw Exception ("undefined operation")
            }
        }

    }
}