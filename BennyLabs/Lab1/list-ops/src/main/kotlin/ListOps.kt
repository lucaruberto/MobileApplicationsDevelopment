fun <T> List<T>.customAppend(list: List<T>): List<T> {
return  list.customFoldLeft(this){acc,it -> acc+it};

}

fun List<Any>.customConcat(): List<Any> {

    return this.customFoldLeft(emptyList()){acc,it -> if(it is List<*>) acc + (it as List<Any>).customConcat() else acc + it}

}

fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
return  this.customFoldLeft(emptyList()){acc,it -> if (predicate(it)) acc+it else acc }
}

val List<Any>.customSize: Int get() = this.customFoldLeft(0){it, _ -> it+1};

fun <T, U> List<T>.customMap(transform: (T) -> U): List<U> {
   return this.customFoldLeft(emptyList()){acc, it -> acc + transform(it)}
}

fun <T, U> List<T>.customFoldLeft(initial: U, f: (U, T) -> U): U {
    var retval:U = initial;
    this.forEach{ retval = f(retval,it)}
    return retval;
}

fun <T, U> List<T>.customFoldRight(initial: U, f: (T, U) -> U): U {
var retval:U = initial;
this.customReverse().forEach{retval = f(it,retval)}
    return retval;
}

fun <T> List<T>.customReverse(): List<T> {
return this.customFoldLeft(emptyList()){acc,it -> listOf(it) + acc }
}
