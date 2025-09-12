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

        fun createFromList(cells: List<CellValue>, size: Int = DEFAULT_SIZE): Board2048 {
            require(cells.size == size * size) { "The number of cells must be equal to size*size" }
            val boardArray = Array(size) { r -> Array(size) { c -> cells[r * size + c] } }
            return Board2048(boardArray)
        }
    }

    constructor(board: List<List<CellValue>>):
        this(Array(board.size) { r -> Array(board.size) { c -> board[r][c] } })

    fun getCellValue(row: Int, col: Int): CellValue {
        return board[row][col]
    }

    private fun setCellValue(row: Int, col: Int, value: CellValue) {
        board[row][col] = value
    }

    fun nextBoard(direction: MoveDirection): Board2048 {
        return when (direction) {
            MoveDirection.LEFT -> mergeLeft()
            MoveDirection.RIGHT -> mergeRight()
            MoveDirection.UP -> mergeUp()
            MoveDirection.DOWN -> mergeDown()
        }
    }

    fun addRandomCell(
        emptyCellPicker: (List<Pair<Int, Int>>) -> Pair<Int, Int> = { it.random() },
        valueGenerator: () -> CellValue = { listOf(CellValue.V2, CellValue.V4).random() },
    ): Board2048 {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until board.size) {
            for (c in 0 until board.size) {
                if (getCellValue(r, c) == CellValue.EMPTY) {
                    emptyCells.add(Pair(r, c))
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return this
        }

        val (r, c) = emptyCellPicker(emptyCells)
        val newBoard = Board2048(this.board.map { it.toList() })
        newBoard.setCellValue(r, c, valueGenerator())
        return newBoard
    }

    private fun mergeLeft(): Board2048 {
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

    private fun mergeRight(): Board2048 {
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

    private fun mergeUp(): Board2048 {
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

    private fun mergeDown(): Board2048 {
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

    fun asFlatList() = board.flatten()

    val size: Int
        get() = board.size
}
