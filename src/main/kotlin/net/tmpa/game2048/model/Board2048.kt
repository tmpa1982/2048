package net.tmpa.game2048.model

class Board2048 {
    companion object {
        const val SIZE = 4

        private val allowedInitialValues = listOf(CellValue.EMPTY, CellValue.V2)

        fun initializeRandomBoard(valueGenerator: () -> CellValue = { allowedInitialValues.random() }) : Board2048 {
            val board = Board2048()
            for (r in 0 until SIZE) {
                for (c in 0 until SIZE) {
                    board.setCellValue(r, c, valueGenerator())
                }
            }
            return board
        }
    }

    private val board: Array<Array<CellValue>> = Array(SIZE) { Array(SIZE) { CellValue.EMPTY } }

    fun getCellValue(row: Int, col: Int): CellValue {
        return board[row][col]
    }

    fun setCellValue(row: Int, col: Int, value: CellValue) {
        board[row][col] = value
    }

    fun clear() {
        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                board[r][c] = CellValue.EMPTY
            }
        }
    }
}
