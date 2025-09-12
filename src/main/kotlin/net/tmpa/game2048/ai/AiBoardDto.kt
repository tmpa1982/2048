package net.tmpa.game2048.ai

import net.tmpa.game2048.model.CellValue

data class AiBoardDto(
    val cells: List<CellValue>,
    val size: Int,
)
