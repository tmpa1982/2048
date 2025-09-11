package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue

class GameTools {
    @Tool("Move the 2048 game board to the left")
    fun mergeLeft(board: List<List<CellValue>>) =
        merge(board) { it.mergeLeft() }

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: List<List<CellValue>>) =
        merge(board) { it.mergeRight() }

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: List<List<CellValue>>) =
        merge(board) { it.mergeUp() }

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: List<List<CellValue>>) =
        merge(board) { it.mergeDown() }

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: List<List<CellValue>>) =
        Board2048(board).isWinning()

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: List<List<CellValue>>) =
        Board2048(board).isLosing()

    private fun merge(board: List<List<CellValue>>, transformer: (Board2048) -> Board2048) =
        transformer(Board2048(board)).asList()
}
