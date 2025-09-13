package net.tmpa.game2048.ai

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage

interface GameEvaluator {
    @SystemMessage("""
        You are a 2048 game AI. The goal is to reach the V2048 tile.

        - You may simulate moves by calling the provided tools to see the result of applying moves.
        - When calling the tools, you must provide the full board flattened as a single list, along with the size of the board.
        - For example, for a 4x4 board, the size is 4 and the cells are provided as a list of 16 values in row-major order.
        - **Your final output must only recommend the immediate next move** from the current board given as input.
        - Do not propose moves based on boards you have already modified.
        - Explain why you chose the move in your reasoning.
        - Respond in JSON format like: {"bestMove": "UP", "reasoning": "reason why you think this is the best move"}.
        - If no move is possible, return {"bestMove": null, "reasoning": "no moves possible"}.
    """)
    fun evaluate(@UserMessage board: AiBoardDto): MoveEvaluation
}
