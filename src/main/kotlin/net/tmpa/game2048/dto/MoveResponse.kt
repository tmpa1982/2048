package net.tmpa.game2048.dto

class MoveResponse(
    val isWinning: Boolean,
    val isLosing: Boolean,
    val board: BoardDto,
)
