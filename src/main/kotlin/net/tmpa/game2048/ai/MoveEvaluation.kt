package net.tmpa.game2048.ai

import net.tmpa.game2048.model.MoveDirection

data class MoveEvaluation(
    val bestMove: MoveDirection,
    val reasoning: String
)
