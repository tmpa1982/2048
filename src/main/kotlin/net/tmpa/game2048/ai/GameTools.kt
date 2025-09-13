package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.MoveDirection
import org.slf4j.LoggerFactory

class GameTools {
    private val logger = LoggerFactory.getLogger(GameTools::class.java)

    @Tool("Merge a 2048 board by applying one or more move directions. " +
            "Input: the current board state (list of cells and board size) and a list of move directions (UP, DOWN, LEFT, RIGHT). " +
            "Output: the new board state after applying the moves.")
    fun applyMoves(board: AiBoardDto, directions: Array<MoveDirection>): AiBoardDto {
        val board = Board2048.createFromList(board.cells, board.size)
        val directionList = directions.toList()
        val reduced = board.nextBoard(directionList)
        val result = AiBoardDto(reduced.asFlatList(), board.size)
        logger.info("Tools call - merging $directionList on board: $board. Result: ${result.cells}")
        return result
    }
}
