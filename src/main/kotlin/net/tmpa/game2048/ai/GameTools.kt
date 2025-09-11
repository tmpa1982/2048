package net.tmpa.game2048.ai

import dev.langchain4j.agent.tool.Tool
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import org.slf4j.LoggerFactory

data class BoardDto(
    val list: List<CellValue>,
    val size: Int,
)

class GameTools {
    private val logger = LoggerFactory.getLogger(GameTools::class.java)

    @Tool("Move the 2048 game board to the left")
    fun mergeLeft(board: BoardDto): BoardDto {
        logger.info("Tools call - merging left for board: $board")
        return merge(board) { it.mergeLeft() }
    }

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: BoardDto): BoardDto {
        logger.info("Tools call - merging right for board: $board")
        return merge(board) { it.mergeRight() }
    }

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: BoardDto): BoardDto {
        logger.info("Tools call - merging up for board: $board")
        return merge(board) { it.mergeUp() }
    }

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: BoardDto): BoardDto {
        logger.info("Tools call - merging down for board: $board")
        return merge(board) { it.mergeDown() }
    }

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: BoardDto): Boolean {
        logger.info("Tools call - evaluating if board is winning: $board")
        return createTwoDimensionalBoard(board).isWinning()
    }

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: BoardDto): Boolean {
        logger.info("Tools call - evaluating if board is losing: $board")
        return createTwoDimensionalBoard(board).isLosing()
    }

    private fun merge(board: BoardDto, transformer: (Board2048) -> Board2048) =
        BoardDto(transformer(createTwoDimensionalBoard(board)).asList().flatten(), board.size)

    private fun createTwoDimensionalBoard(board: BoardDto): Board2048 {
        val size = board.size
        val twoDimensionalBoard = Array(size) { Array(size) { CellValue.EMPTY } }
        for (i in board.list.indices) {
            twoDimensionalBoard[i / size][i % size] = board.list[i]
        }
        return Board2048(twoDimensionalBoard)
    }
}
