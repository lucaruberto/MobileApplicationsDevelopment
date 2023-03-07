data class MinesweeperBoard constructor (val input: List<String>){
    fun withNumbers(): List<String> {

        val returnBoard: List<String> = emptyList()

        if (input.isNotEmpty()) {
            val rows = input.size
            val columns = input[0].length

            if(input[0] != ""){
                for(i in 0 until rows){
                    val line: String = input[i]
                    for(j in 0 until columns){
                        //TODO to be completed
                    }
                }
                return returnBoard
            } else
                return input

        }
        else
            return input
    }
}
