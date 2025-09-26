package net.tmpa.game2048.model

class Board2048(board: List<List<CellValue>> = List(DEFAULT_SIZE) { List(DEFAULT_SIZE) { CellValue.EMPTY } }) {
    private val board = Array(board.size) { r -> Array(board.size) { c -> board[r][c] } }

    init {
        require(board.size >= 2) { "Board size must be at least 2" }
        require(board.all { it.size == board.size }) { "Board must be square" }
    }

    companion object {
        const val DEFAULT_SIZE = 4

        val NEW_CELL_VALUES = listOf(CellValue.V2, CellValue.V4)

        fun initializeRandomBoard() : Board2048 {
            val board = Board2048()
            val availableCells = List(DEFAULT_SIZE * DEFAULT_SIZE) { i -> Pair(i / DEFAULT_SIZE, i % DEFAULT_SIZE) }
                .toMutableList()
            val numberOfNonEmptyCells = (2..DEFAULT_SIZE).random()
            repeat(numberOfNonEmptyCells) {
                val (r, c) = availableCells.random()
                availableCells.remove(Pair(r, c))
                board.setCellValue(r, c, CellValue.V2)
            }
            return board
        }

        fun createFromList(cells: List<CellValue>, size: Int = DEFAULT_SIZE): Board2048 {
            require(cells.size == size * size) { "The number of cells must be equal to size*size" }
            val boardArray = List(size) { r -> List(size) { c -> cells[r * size + c] } }
            return Board2048(boardArray)
        }
    }

    private constructor(size: Int) :
        this(List(size) { List(size) { CellValue.EMPTY } })

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

    fun nextBoard(directions: List<MoveDirection>): Board2048 {
        var current = this
        for (direction in directions) {
            current = current.nextBoard(direction)
        }
        return current
    }

    fun addRandomCell(
        emptyCellPicker: (List<Pair<Int, Int>>) -> Pair<Int, Int> = { it.random() },
        valueGenerator: () -> CellValue = { NEW_CELL_VALUES.random() },
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
        require(board[r][c] == CellValue.EMPTY)
        val newBoard = Board2048(this.board.map { it.toList() })
        newBoard.setCellValue(r, c, valueGenerator())
        return newBoard
    }

    fun nextBoardAndCell(direction: MoveDirection): Board2048 {
        val next = nextBoard(direction)
        if (this == next) {
            return this
        }
        return next.addRandomCell()
    }

    private fun mergeLeft(): Board2048 {
        val newBoard = Board2048(this.size)
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
        val newBoard = Board2048(this.size)
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
        val newBoard = Board2048(this.size)
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
        val newBoard = Board2048(this.size)
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
                val cellValue = getCellValue(r, c)
                if (cellValue == CellValue.EMPTY) {
                    return false
                }
                if (cellValue == CellValue.V2048) {
                    return false
                }
                if (c < board.size - 1 && cellValue == getCellValue(r, c + 1)) {
                    return false
                }
                if (r < board.size - 1 && cellValue == getCellValue(r + 1, c)) {
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
