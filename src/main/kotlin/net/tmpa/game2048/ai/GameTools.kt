package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.MoveDirection
import org.slf4j.LoggerFactory

class GameTools {
    private val logger = LoggerFactory.getLogger(GameTools::class.java)

    @Tool("Move the 2048 game board to the left")
    fun mergeLeft(board: AiBoardDto) = merge(board, MoveDirection.LEFT)

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: AiBoardDto) = merge(board, MoveDirection.RIGHT)

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: AiBoardDto) = merge(board, MoveDirection.UP)

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: AiBoardDto) = merge(board, MoveDirection.DOWN)

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: AiBoardDto): Boolean {
        val result = Board2048.createFromList(board.cells, board.size).isWinning()
        logger.info("Tools call - evaluating if ${board.cells} is winning: $result")
        return result
    }

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: AiBoardDto): Boolean {
        val result = Board2048.createFromList(board.cells, board.size).isLosing()
        logger.info("Tools call - evaluating if ${board.cells} is losing: $result")
        return result
    }

    private fun merge(board: AiBoardDto, direction: MoveDirection): AiBoardDto {
        val result = AiBoardDto(Board2048.createFromList(board.cells, board.size).nextBoard(direction).asFlatList(), board.size)
        logger.info("Tools call - merging $direction on board: $board. Result: ${result.cells}")
        return result
    }

}
