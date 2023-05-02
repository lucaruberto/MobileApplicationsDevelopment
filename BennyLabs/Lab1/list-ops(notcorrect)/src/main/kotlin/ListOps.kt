fun <T> List<T>.customAppend(list: List<T>): List<T> {
    var retval=this;
 for(element in list)
 {
     retval=retval.plus(element);
 }
    return retval;
}

fun List<Any>.customConcat(): List<Any> {

  return  this.flatMap { if(it is List<*>){
    it.filterNotNull().customConcat()
    }else listOf<Any>(it)
    }

}

fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
 return  this.filter { predicate(it) }
}

val List<Any>.customSize: Int get() = this.customFoldLeft(0){it, _ -> it+1};

fun <T, U> List<T>.customMap(transform: (T) -> U): List<U> {
   return this.map(transform)
}

fun <T, U> List<T>.customFoldLeft(initial: U, f: (U, T) -> U): U {
    var retval:U = initial;
    this.forEach{ retval = f(retval,it)}
    return retval;
}

fun <T, U> List<T>.customFoldRight(initial: U, f: (T, U) -> U): U {
var retval:U = initial;
this.reversed().forEach{retval = f(it,retval)}
    return retval;
}

fun <T> List<T>.customReverse(): List<T> {
return this.reversed();
}
