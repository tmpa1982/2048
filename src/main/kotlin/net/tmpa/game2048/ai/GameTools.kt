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
        val result = merge(board) { it.mergeLeft() }
        logger.info("Tools call - merging left: ${result.list}")
        return result
    }

    @Tool("Move the 2048 game board to the right")
    fun mergeRight(board: BoardDto): BoardDto {
        val result = merge(board) { it.mergeRight() }
        logger.info("Tools call - merging right: ${result.list}")
        return result
    }

    @Tool("Move the 2048 game board up")
    fun mergeUp(board: BoardDto): BoardDto {
        val result = merge(board) { it.mergeUp() }
        logger.info("Tools call - merging up: ${result.list}")
        return result
    }

    @Tool("Move the 2048 game board down")
    fun mergeDown(board: BoardDto): BoardDto {
        val result = merge(board) { it.mergeDown() }
        logger.info("Tools call - merging down for board: ${result.list}")
        return result
    }

    @Tool("Evaluate if the 2048 game board is winning")
    fun isWinning(board: BoardDto): Boolean {
        val result = createTwoDimensionalBoard(board).isWinning()
        logger.info("Tools call - evaluating if ${board.list} is winning: $result")
        return result
    }

    @Tool("Evaluate if the 2048 game board is losing")
    fun isLosing(board: BoardDto): Boolean {
        val result = createTwoDimensionalBoard(board).isLosing()
        logger.info("Tools call - evaluating if ${board.list} is losing: $result")
        return result
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
