package net.tmpa.game2048.ai

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import net.tmpa.game2048.model.CellValue

interface GameEvaluator {
    @SystemMessage(
        """
        You are a 2048 game player. The goal is to reach the 2048 cell value.
        Given the current board state, find the best move (up, down, left, right) to achieve this goal.
        Respond in JSON format with the best move.
        """
    )
    fun evaluate(@UserMessage board: List<List<CellValue>>): MoveEvaluation
}
