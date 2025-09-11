package net.tmpa.game2048.ai

import net.tmpa.game2048.model.Board2048
import org.springframework.stereotype.Service

@Service
class AiEvaluator(
    private val gameEvaluatorFactory: AiServiceFactory,
) {
    fun evaluate(board: Board2048): MoveEvaluation {
        val evaluator = gameEvaluatorFactory.createService()
        val boardList = board.asList()
        return evaluator.evaluate(BoardDto(boardList.flatten(), boardList.size))
    }
}
