package net.tmpa.game2048.ai

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage
import net.tmpa.game2048.model.CellValue

interface GameEvaluator {
    @SystemMessage("""
        You are a 2048 game AI. The goal is to reach the V2048 tile.

        - You may simulate moves by calling the provided tools to see the result of applying moves.
        - **Your final output must only recommend the immediate next move** from the current board given as input.
        - Do not propose moves based on boards you have already modified.
        - Respond in JSON format like: {"bestMove": "UP"}.
    """)
    fun evaluate(@UserMessage board: List<List<CellValue>>): MoveEvaluation
}
