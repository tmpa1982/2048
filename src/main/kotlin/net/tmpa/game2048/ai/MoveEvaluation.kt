package net.tmpa.game2048.ai

enum class MoveDirection {
    UP, DOWN, LEFT, RIGHT
}

data class MoveEvaluation(val bestMove: MoveDirection, val score: Int)
