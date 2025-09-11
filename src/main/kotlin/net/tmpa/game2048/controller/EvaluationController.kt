package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.AiEvaluator
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/evaluate")
class EvaluationController(private val evaluator: AiEvaluator) {
    private val logger = LoggerFactory.getLogger(EvaluationController::class.java)

    @PostMapping
    fun evaluateBoard(@RequestBody request: EvaluationRequest): EvaluationResponse {
        logger.info("Evaluating board: ${request.board}")
        val board = Board2048(request.board.map { it.toTypedArray<CellValue>() }.toTypedArray<Array<CellValue>>())
        val moveEvaluation = evaluator.evaluate(board)

        val response = EvaluationResponse(moveEvaluation, board.nextBoard(moveEvaluation.bestMove).asList())
        logger.info("Evaluation result: ${response.evaluation}, Next board: ${response.board}")
        return response
    }
}
