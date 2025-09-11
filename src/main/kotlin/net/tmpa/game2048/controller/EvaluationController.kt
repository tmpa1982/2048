package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.AiEvaluator
import net.tmpa.game2048.ai.MoveDirection
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluate")
class EvaluationController(private val evaluator: AiEvaluator) {
    @PostMapping
    fun evaluateBoard(@RequestBody request: EvaluationRequest): EvaluationResponse {
        val board = Board2048(request.board.map { it.toTypedArray<CellValue>() }.toTypedArray<Array<CellValue>>())
        val moveEvaluation = evaluator.evaluate(board)

        return EvaluationResponse(moveEvaluation, nextBoard(board, moveEvaluation.bestMove).asList())
    }

    private fun nextBoard(board: Board2048, move: MoveDirection) = when (move) {
        MoveDirection.LEFT -> board.mergeLeft()
        MoveDirection.RIGHT -> board.mergeRight()
        MoveDirection.UP -> board.mergeUp()
        MoveDirection.DOWN -> board.mergeDown()
    }
}
