package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import org.slf4j.LoggerFactory

class GameTools {
    private val logger = LoggerFactory.getLogger(GameTools::class.java)

    @Tool("Move the 2048 game board to the left")
    fun mergeLeft(board: List<List<CellValue>>): List<List<CellValue>> {
        logger.info("Tools call - merging left for board: $board")
        return merge(board) { it.mergeLeft() }
    }

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: List<List<CellValue>>): List<List<CellValue>> {
        logger.info("Tools call - merging right for board: $board")
        return merge(board) { it.mergeRight() }
    }

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: List<List<CellValue>>): List<List<CellValue>> {
        logger.info("Tools call - merging up for board: $board")
        return merge(board) { it.mergeUp() }
    }

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: List<List<CellValue>>): List<List<CellValue>> {
        logger.info("Tools call - merging down for board: $board")
        return merge(board) { it.mergeDown() }
    }

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: List<List<CellValue>>): Boolean {
        logger.info("Tools call - evaluating if board is winning: $board")
        return Board2048(board).isWinning()
    }

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: List<List<CellValue>>): Boolean {
        logger.info("Tools call - evaluating if board is losing: $board")
        return Board2048(board).isLosing()
    }

    private fun merge(board: List<List<CellValue>>, transformer: (Board2048) -> Board2048) =
        transformer(Board2048(board)).asList()
}
