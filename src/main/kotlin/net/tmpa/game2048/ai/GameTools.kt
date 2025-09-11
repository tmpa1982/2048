package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.slf4j.LoggerFactory

class GameTools {
    private val logger = LoggerFactory.getLogger(GameTools::class.java)

    @Tool("Move the 2048 game board to the left")
    fun mergeLeft(board: BoardDto) = merge(board, MoveDirection.LEFT)

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: BoardDto) = merge(board, MoveDirection.RIGHT)

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: BoardDto) = merge(board, MoveDirection.UP)

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: BoardDto) = merge(board, MoveDirection.DOWN)

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: BoardDto): Boolean {
        val result = createTwoDimensionalBoard(board).isWinning()
        logger.info("Tools call - evaluating if ${board.cells} is winning: $result")
        return result
    }

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: BoardDto): Boolean {
        val result = createTwoDimensionalBoard(board).isLosing()
        logger.info("Tools call - evaluating if ${board.cells} is losing: $result")
        return result
    }

    private fun merge(board: BoardDto, direction: MoveDirection): BoardDto {
        val result = BoardDto(createTwoDimensionalBoard(board).nextBoard(direction).asList().flatten(), board.size)
        logger.info("Tools call - merging $direction on board: $board. Result: ${result.cells}")
        return result
    }

    private fun createTwoDimensionalBoard(board: BoardDto): Board2048 {
        val size = board.size
        val twoDimensionalBoard = Array(size) { Array(size) { CellValue.EMPTY } }
        for (i in board.cells.indices) {
            twoDimensionalBoard[i / size][i % size] = board.cells[i]
        }
        return Board2048(twoDimensionalBoard)
    }
}
