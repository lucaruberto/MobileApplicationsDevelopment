data class MinesweeperBoard constructor (val input: List<String>){
    fun withNumbers(): List<String> {

        var returnBoard: List<String> = emptyList()

        if (input.isNotEmpty()) {
            val maxRows = input.size
            val maxColumns = input[0].length
            for(row in 0 until maxRows){
                var newLine = ""
                if(input[row].isNotEmpty()) {
                    for (col in 0 until maxColumns) {
                        var newChar = '*'
                        if (input[row][col] != '*') {
                            var count = 0
                            for (r in row - 1..row + 1) {
                                for (c in col - 1..col + 1) {
                                    if (r < 0 || r >= maxRows || c < 0 || c >= maxColumns)
                                        continue
                                    if (input[r][c] == '*')
                                        count++
                                }
                            }
                            newChar = if (count > 0) count.digitToChar() else ' '
                        }
                        newLine = newLine.plus(newChar)
                    }
                }
                returnBoard = returnBoard.plus(newLine)
            }
        }
        return returnBoard
    }
}
