package net.tmpa.game2048.model

data class EvaluationLaneResult(private val lane: EvaluationLane, val scoreDelta: Int = 0) {
    val cells = lane.cells
}

data class EvaluationLane(val cells: List<CellValue>) {
    private class NewCellResult(val newCells: List<CellValue>, val scoreDelta: Int)

    fun evaluate(): EvaluationLaneResult {
        for (i in cells.indices) {
            if (cells[i] == CellValue.OBSTACLE) {
                val leftResult = EvaluationLane(cells.subList(0, i)).evaluate()
                val rightResult = EvaluationLane(cells.subList(i + 1, cells.size)).evaluate()

                val lane = EvaluationLane(leftResult.cells + listOf(CellValue.OBSTACLE) + rightResult.cells)
                val score = leftResult.scoreDelta + rightResult.scoreDelta
                return EvaluationLaneResult(lane, score)
            }
        }

        val nonEmptyCells = cells.filter { it != CellValue.EMPTY }
        if (nonEmptyCells.isEmpty()) return EvaluationLaneResult(EvaluationLane(List(cells.size) { CellValue.EMPTY }), 0)

        if (nonEmptyCells.size == 1) {
            return EvaluationLaneResult(padWithEmpties(listOf(nonEmptyCells[0])), 0)
        }

        val evalResult = evaluateNewCells(nonEmptyCells)
        val newCells = evalResult.newCells

        if (newCells.size == cells.size) {
            return EvaluationLaneResult(this, 0)
        }

        return EvaluationLaneResult(padWithEmpties(newCells), evalResult.scoreDelta)
    }

    private fun evaluateNewCells(nonEmptyCells: List<CellValue>): NewCellResult {
        val newCells = mutableListOf<CellValue>()
        var score = 0
        var previousCell: CellValue? = null
        for (cell in nonEmptyCells) {
            if (previousCell == null) {
                previousCell = cell
            } else {
                if (previousCell == cell) {
                    val next = previousCell.getNext()
                    newCells.add(next)
                    score += next.value
                    previousCell = null
                } else {
                    newCells.add(previousCell)
                    previousCell = cell
                }
            }
        }
        if (previousCell != null) {
            newCells.add(previousCell)
        }
        return NewCellResult(newCells, score)
    }

    private fun padWithEmpties(newCells: List<CellValue>): EvaluationLane {
        val paddedCells = newCells + List(cells.size - newCells.size) { CellValue.EMPTY }
        return EvaluationLane(paddedCells)
    }
}
