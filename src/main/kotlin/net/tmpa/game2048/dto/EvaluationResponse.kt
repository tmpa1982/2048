package net.tmpa.game2048.dto

import net.tmpa.game2048.ai.MoveEvaluation

class EvaluationResponse(
    val evaluation: MoveEvaluation,
    val board: BoardDto,
)
