package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.AiEvaluator
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
        val boardString = board.asList().toString()
        println("Received board for evaluation: $boardString")
        val aiResult = evaluator.evaluate(boardString)
        println("AI Evaluation Result: $aiResult")

        return EvaluationResponse(board.asList())
    }
}
