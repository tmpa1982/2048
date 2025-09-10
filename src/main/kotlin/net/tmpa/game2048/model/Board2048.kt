package net.tmpa.game2048.model

class Board2048(private val board: Array<Array<CellValue>> = Array(DEFAULT_SIZE) { Array(DEFAULT_SIZE) { CellValue.EMPTY } }) {
    companion object {
        const val DEFAULT_SIZE = 4

        private val allowedInitialValues = listOf(CellValue.EMPTY, CellValue.V2)

        fun initializeRandomBoard(valueGenerator: () -> CellValue = { allowedInitialValues.random() }) : Board2048 {
            val board = Board2048()
            var nonEmptyCount = 0
            for (r in 0 until DEFAULT_SIZE) {
                for (c in 0 until DEFAULT_SIZE) {
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
                val r = (0 until DEFAULT_SIZE).random()
                val c = (0 until DEFAULT_SIZE).random()
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
        for (r in 0 until board.size) {
            val lane = EvaluationLane(board[r].toList())
            val evaluatedLane = lane.evaluate()
            for (c in 0 until board.size) {
                newBoard.setCellValue(r, c, evaluatedLane.cells[c])
            }
        }
        return newBoard
    }

    fun mergeRight(): Board2048 {
        val newBoard = Board2048()
        for (r in 0 until board.size) {
            val lane = EvaluationLane(board[r].reversed())
            val evaluatedLane = lane.evaluate()
            for (c in 0 until board.size) {
                newBoard.setCellValue(r, board.size - 1 - c, evaluatedLane.cells[c])
            }
        }
        return newBoard
    }

    fun mergeUp(): Board2048 {
        val newBoard = Board2048()
        for (c in 0 until board.size) {
            val lane = EvaluationLane(List(board.size) { r -> board[r][c] })
            val evaluatedLane = lane.evaluate()
            for (r in 0 until board.size) {
                newBoard.setCellValue(r, c, evaluatedLane.cells[r])
            }
        }
        return newBoard
    }

    fun mergeDown(): Board2048 {
        val newBoard = Board2048()
        for (c in 0 until board.size) {
            val lane = EvaluationLane(List(board.size) { r -> board[board.size - 1 - r][c] })
            val evaluatedLane = lane.evaluate()
            for (r in 0 until board.size) {
                newBoard.setCellValue(board.size - 1 - r, c, evaluatedLane.cells[r])
            }
        }
        return newBoard
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board2048) return false

        for (r in 0 until board.size) {
            for (c in 0 until board.size) {
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

    fun isWinning() = board.any { row -> row.any { cell -> cell == CellValue.V2048 } }

    fun isLosing(): Boolean {
        for (r in 0 until board.size) {
            for (c in 0 until board.size) {
                if (getCellValue(r, c) == CellValue.EMPTY) {
                    return false
                }
                if (c < board.size - 1 && getCellValue(r, c) == getCellValue(r, c + 1)) {
                    return false
                }
                if (r < board.size - 1 && getCellValue(r, c) == getCellValue(r + 1, c)) {
                    return false
                }
            }
        }

        return true
    }

    fun asList() = board.map { it.toList() }
}
