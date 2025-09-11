package net.tmpa.game2048.ai

import net.tmpa.game2048.model.CellValue

data class BoardDto(
    val cells: List<CellValue>,
    val size: Int,
)
