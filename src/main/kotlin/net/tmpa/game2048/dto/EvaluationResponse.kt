package net.tmpa.game2048.dto

import net.tmpa.game2048.ai.MoveEvaluation
import net.tmpa.game2048.model.CellValue

class EvaluationResponse(
    val evaluation: MoveEvaluation,
    val board: List<List<CellValue>>,
)
