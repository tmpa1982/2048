package net.tmpa.game2048.model

class Board2048(private val board: Array<Array<CellValue>> = Array(SIZE) { Array(SIZE) { CellValue.EMPTY } }) {
    companion object {
        const val SIZE = 4

        private val allowedInitialValues = listOf(CellValue.EMPTY, CellValue.V2)

        fun initializeRandomBoard(valueGenerator: () -> CellValue = { allowedInitialValues.random() }) : Board2048 {
            val board = Board2048()
            var nonEmptyCount = 0
            for (r in 0 until SIZE) {
                for (c in 0 until SIZE) {
                    val value = valueGenerator()
                    board.setCellValue(r, c, value)
                    if (value != CellValue.EMPTY) {
                        nonEmptyCount++
                    }
                }
            }

            if (nonEmptyCount < 2) {
                ensureTwoNonEmptyCells(board)
            }
            return board
        }

        private fun ensureTwoNonEmptyCells(board: Board2048) {
            var placed = 0
            while (placed < 2) {
                val r = (0 until SIZE).random()
                val c = (0 until SIZE).random()
                if (board.getCellValue(r, c) == CellValue.EMPTY) {
                    board.setCellValue(r, c, CellValue.V2)
                    placed++
                }
            }
        }
    }

    fun getCellValue(row: Int, col: Int): CellValue {
        return board[row][col]
    }

    private fun setCellValue(row: Int, col: Int, value: CellValue) {
        board[row][col] = value
    }

    fun mergeLeft(): Board2048 {
        val newBoard = Board2048()
        for (r in 0 until SIZE) {
            val lane = EvaluationLane(board[r].toList())
            val evaluatedLane = lane.evaluate()
            for (c in 0 until SIZE) {
                newBoard.setCellValue(r, c, evaluatedLane.cells[c])
            }
        }
        return newBoard
    }

    fun mergeRight(): Board2048 {
        val newBoard = Board2048()
        for (r in 0 until SIZE) {
            val lane = EvaluationLane(board[r].reversed())
            val evaluatedLane = lane.evaluate()
            for (c in 0 until SIZE) {
                newBoard.setCellValue(r, SIZE - 1 - c, evaluatedLane.cells[c])
            }
        }
        return newBoard
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board2048) return false

        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                if (this.getCellValue(r, c) != other.getCellValue(r, c)) {
                    return false
                }
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }
}
