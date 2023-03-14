fun <T> List<T>.customAppend(list: List<T>): List<T> {
    val appendedList = mutableListOf<T>()

    for(item in this)
        appendedList.add(item)
    for(item in list)
        appendedList.add(item)

    return appendedList

}

fun List<Any>.customConcat(): List<Any> {
    return this.flatMap { if(it is List<*>){
            it.filterNotNull().customConcat()
        }else listOf(it)
    }
}

fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
    val filteredList = mutableListOf<T>()

    for(item in this)
        if(predicate(item))
            filteredList.add(item)
        else
            continue
    return filteredList
}

val List<Any>.customSize: Int
    get() {
    var count = 0
    this.forEach { _ -> count++}
    return count
}

fun <T, U> List<T>.customMap(transform: (T) -> U): List<U> {
    val mappedList = mutableListOf<U>()

    this.forEach { mappedList.add(transform(it)) }
    return mappedList
}

fun <T, U> List<T>.customFoldLeft(initial: U, f: (U, T) -> U): U {
    return if(this.isEmpty()) //caso terminale
        initial
    else this.drop(1).customFoldLeft(f(initial, this.first()), f) //ricorsione: ricorro togliendo un elemento dall'inizio
}

fun <T, U> List<T>.customFoldRight(initial: U, f: (T, U) -> U): U {
    return if(this.isEmpty()) //caso terminale
        initial
    else this.dropLast(1).customFoldRight(f(this.last(), initial), f) //ricorsione: ricorro togliendo un elemento dalla fine
}

fun <T> List<T>.customReverse(): List<T> {
    val reverseList = mutableListOf<T>()

    for (item in this)
        reverseList.add(0,item)

    return reverseList
}
