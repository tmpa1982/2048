package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.NextMoveEvaluatorService
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/evaluate")
class EvaluationController(private val evaluator: NextMoveEvaluatorService) {
    private val logger = LoggerFactory.getLogger(EvaluationController::class.java)

    @PostMapping
    fun evaluateBoard(@RequestBody request: EvaluationRequest): EvaluationResponse {
        logger.info("Evaluating board: ${request.board}")
        if (!request.isValid()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid board")
        }
        val response = evaluator.evaluate(request)
        logger.info("Evaluation result: ${response.evaluation}, Next board: ${response.board}")
        return response
    }
}
