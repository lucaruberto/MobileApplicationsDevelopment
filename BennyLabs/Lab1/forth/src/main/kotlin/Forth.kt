import java.util.*
import kotlin.collections.ArrayDeque

class Forth {

    private var  stack = ArrayDeque<Int>();
    private var customWords : MutableMap<String,List<String>> = mutableMapOf();
    fun evaluate(vararg line: String): List<Int> {
        stack.clear();
            for( str in line)
            {
                if (str.startsWith(":"))
                {
                    val command = str.substring(2,str.length-2).uppercase(Locale.getDefault());
                    val splitted = command.split(" ").toMutableList();
                    if(splitted[0].toIntOrNull()!=null)
                    {
                        throw  Exception ("illegal operation")
                    }
                    var i=1;
                    while(i<splitted.size)
                    {
                        var c=splitted[i];
                            while(customWords.containsKey(c))
                            {
                                splitted.addAll(i+1,customWords.getValue(c));
                                splitted.removeAt(i);
                                c=splitted[i];
                            }
                        i++;
                    }
                   customWords[splitted[0].uppercase()] = splitted.drop(1);
                }
                else
                {var valori=str.split(" ");
                    valori.forEach{ manageStack(it)};}
                }


        return stack.toList();
    }

    private fun manageStack(value:String){

        if(customWords.containsKey(value.uppercase(Locale.getDefault())))
        {
            for(valore in customWords.getValue(value.uppercase()))
            {
                operations(valore.uppercase());
            }
        }
        else {
            val check = value.uppercase(Locale.getDefault());
            operations(check);
            }
        return;
    }

    private fun operations(check:String)
    {

        when (check)
        {
            "+"->   {   if(stack.size==1)
            {
                throw Exception("only one value on the stack")
            }
                if(stack.isEmpty())
                {
                    throw  Exception("empty stack");
                }
                var newvalue= stack.last();
                stack.removeLast();
                var newvalue2 = stack.last();
                stack.removeLast();
                stack.add(newvalue2+newvalue);
            }
            "-"->   {   if(stack.size==1)
            {

                throw Exception("only one value on the stack")
            }
                if(stack.isEmpty())
                {

                    throw  Exception("empty stack");
                }
                var newvalue= stack.last();
                stack.removeLast();
                var newvalue2 = stack.last();
                stack.removeLast();
                stack.add(newvalue2-newvalue);}

            "*"->    {   if(stack.size==1)
            {
                throw Exception("only one value on the stack")
            }
                if(stack.isEmpty())
                {
                    throw  Exception("empty stack");
                }
                var newvalue= stack.last();
                stack.removeLast();
                var newvalue2 = stack.last();
                stack.removeLast();
                stack.add(newvalue2*newvalue);
            }
            "/"->    {   if(stack.size==1)
            {
                throw Exception("only one value on the stack")
            }
                if(stack.isEmpty())
                {
                    throw  Exception("empty stack");
                }
                var newvalue= stack.last();
                stack.removeLast();
                var newvalue2 = stack.last();
                stack.removeLast();
                if(newvalue==0)
                {
                    throw  Exception("divide by zero");
                }
                stack.add(newvalue2/newvalue);
            }
            "DUP"->    {    if(stack.size==0)
            {
                throw  Exception("empty stack");
            }
                var newvalue= stack.last();
                stack.add(newvalue);
            }
            "DROP"->    {    if(stack.size==0)
            {
                throw  Exception("empty stack");
            }
                stack.removeLast();
            }
            "SWAP"->   {    if(stack.size==0)
            {
                throw  Exception("empty stack");
            }
                if(stack.size==1)
                {
                    throw Exception("only one value on the stack");
                }
                var newvalue=stack.last();
                stack.removeLast();
                var newvalue2=stack.last();
                stack.removeLast();
                stack.add(newvalue);
                stack.add(newvalue2);
            }
            "OVER"->   { if(stack.size==0)
            { throw  Exception("empty stack"); }
                if(stack.size==1)
                { throw Exception("only one value on the stack"); }
                var newvalue=  stack.get(stack.size-2);
                stack.add(newvalue);


            }
            else -> {
                if(check.toIntOrNull()!=null)
                {
                    stack.add(check.toInt());
                }
                else throw Exception ("undefined operation");
                }
        }

    }
}
