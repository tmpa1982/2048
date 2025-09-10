package net.tmpa.game2048.controller

import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.Board2048
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluate")
class EvaluationController {
    @PostMapping
    fun evaluateBoard(@RequestBody request: EvaluationRequest): EvaluationResponse {
        val board = Board2048(request.board.map { it.toTypedArray() }.toTypedArray())
        val newBoard = board.mergeUp()

        return EvaluationResponse(newBoard.asList())
    }
}
