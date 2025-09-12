package net.tmpa.game2048.dto

import net.tmpa.game2048.model.CellValue

data class BoardDto(val cells: List<List<CellValue>>)
