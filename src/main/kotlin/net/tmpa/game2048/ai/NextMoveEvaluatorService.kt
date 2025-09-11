package net.tmpa.game2048.ai

import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.Board2048
import org.springframework.stereotype.Service

@Service
class NextMoveEvaluatorService(
    private val gameEvaluatorFactory: AiServiceFactory,
) {
    fun evaluate(request: EvaluationRequest): EvaluationResponse {
        val evaluator = gameEvaluatorFactory.createService()
        val board = Board2048(request.board)
        val boardList = board.asList()
        val moveEvaluation = evaluator.evaluate(BoardDto(boardList.flatten(), boardList.size))
        val response = EvaluationResponse(moveEvaluation, board.nextBoard(moveEvaluation.bestMove).asList())
        return response
    }
}
