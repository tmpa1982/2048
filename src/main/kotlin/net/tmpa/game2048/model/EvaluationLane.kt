package net.tmpa.game2048.model

data class EvaluationLane(val cells: List<CellValue>) {
    fun evaluate() : EvaluationLane {
        val nonEmptyCells = cells.filter { it != CellValue.EMPTY }
        if (nonEmptyCells.isEmpty()) return EvaluationLane(List(cells.size) { CellValue.EMPTY })

        if (nonEmptyCells.size == 1) {
            return padWithEmpties(listOf(nonEmptyCells[0]))
        }

        val newCells = evaluateNewCells(nonEmptyCells)

        if (newCells.size == cells.size) {
            return this
        }

        return padWithEmpties(newCells)
    }

    private fun evaluateNewCells(nonEmptyCells: List<CellValue>): MutableList<CellValue> {
        val newCells = mutableListOf<CellValue>()
        var previousCell: CellValue? = null
        for (cell in nonEmptyCells) {
            if (previousCell == null) {
                previousCell = cell
            } else {
                if (previousCell == cell) {
                    newCells.add(previousCell.getNext())
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
        return newCells
    }

    private fun padWithEmpties(newCells: List<CellValue>): EvaluationLane {
        val paddedCells = newCells + List(cells.size - newCells.size) { CellValue.EMPTY }
        return EvaluationLane(paddedCells)
    }
}
