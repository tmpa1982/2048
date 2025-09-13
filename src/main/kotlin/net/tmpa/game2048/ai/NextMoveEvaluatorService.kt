package net.tmpa.game2048.ai

import net.tmpa.game2048.dto.BoardDto
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.Board2048
import org.springframework.stereotype.Service

@Service
class NextMoveEvaluatorService(
    private val gameEvaluatorFactory: AiServiceFactory,
) {
    fun evaluate(request: EvaluationRequest): EvaluationResponse {
        val evaluator = gameEvaluatorFactory.createService(request.model)
        val board = Board2048(request.board)
        val moveEvaluation = evaluator.evaluate(AiBoardDto(board.asFlatList(), board.size))
        val nextBoard = if (moveEvaluation.bestMove != null) board.nextBoard(moveEvaluation.bestMove) else board
        val response = EvaluationResponse(moveEvaluation, BoardDto(nextBoard.asList()))
        return response
    }
}
