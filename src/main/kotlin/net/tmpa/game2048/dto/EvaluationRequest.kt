package net.tmpa.game2048.dto

import net.tmpa.game2048.model.CellValue

class EvaluationRequest(val board: List<List<CellValue>>) {
    fun isValid(): Boolean {
        val size = board.size
        if (size == 0) return false
        if (board.any { it.size != size }) return false
        return true
    }
}
