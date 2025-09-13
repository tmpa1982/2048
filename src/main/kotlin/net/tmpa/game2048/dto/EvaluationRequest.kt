package net.tmpa.game2048.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import net.tmpa.game2048.model.CellValue

class EvaluationRequest(
    val board: List<List<CellValue>>,
    val model: String = "gpt-5-chat",
) {
    @JsonIgnore
    fun isValid(): Boolean {
        val size = board.size
        if (size == 0) return false
        if (board.any { it.size != size }) return false
        return true
    }
}
