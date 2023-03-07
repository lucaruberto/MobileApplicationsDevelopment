data class MinesweeperBoard constructor (val input: List<String>){
    fun withNumbers(): List<String> {

        var returnBoard: List<String> = emptyList()

        if (input.isNotEmpty()) {
            val maxRows = input.size
            val maxColumns = input[0].length

            if(input[0] != ""){
                for(i in 0 until maxRows){
                    var line = ""
                    for(j in 0 until maxColumns){
                        if(input[i][j] == ' ') {
                            var count = 0
                            for(r_ind in i-1 .. i+1) {
                                for (c_ind in j - 1 .. j + 1) {
                                    if (r_ind < 0 || r_ind >= maxRows || c_ind < 0 || c_ind >= maxColumns)
                                        continue
                                    else
                                        if (input[r_ind][c_ind] == '*')
                                            count++
                                }
                            }
                            if(count==0) line += ' '
                            else line += count
                        }
                        else
                            line += '*'
                    }
                    returnBoard = returnBoard.plus(line)
                }
                return returnBoard
            } else
                return input

        }
        else
            return input
    }
}
