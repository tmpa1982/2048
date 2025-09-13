package net.tmpa.game2048.controller

import net.tmpa.game2048.dto.CreateGameResponse
import net.tmpa.game2048.dto.MoveRequest
import net.tmpa.game2048.dto.MoveResponse
import net.tmpa.game2048.model.Board2048
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.fail

class GameControllerIntegrationTest : IntegrationTestBase() {
    @Test
    fun `let's play a game`() {
        val response = restTemplate.postForEntity("/api/game", null, CreateGameResponse::class.java)
        val gameId = response.body!!.id
        val initialBoard = response.body!!.board.cells
        assertInitialBoard(initialBoard)

        val moveRequest = MoveRequest(MoveDirection.DOWN)
        val moveResponse = restTemplate.postForEntity("/api/game/$gameId/move", moveRequest, MoveResponse::class.java)
        val moveBody = moveResponse.body!!
        assertFalse(moveBody.isLosing)
        assertFalse(moveBody.isWinning)
        val newBoardCells = moveBody.board.cells
        assertBoardMovedDownExceptMaybeOne(newBoardCells)

        val initialBoardSum = initialBoard.flatten().sumOf { it.value }
        val newBoardSum = newBoardCells.flatten().sumOf { it.value }
        val difference = newBoardSum - initialBoardSum
        assertContains(Board2048.NEW_CELL_VALUES.map { it.value }, difference)
    }

    private fun assertInitialBoard(board: List<List<CellValue>>) {
        val cellValues = board.flatten()
        val numEmpty = cellValues.count { it == CellValue.EMPTY }
        assertNotEquals(0, numEmpty, "There should be some empty cells")

        val initialValue = cellValues.filter { it != CellValue.EMPTY }.distinct().single()
        assertEquals(CellValue.V2, initialValue)
    }

    private fun assertBoardMovedDownExceptMaybeOne(board: List<List<CellValue>>) {
        var newCell: Pair<Int, Int>? = null
        for (c in 0 until board.size) {
            var foundEmpty = false
            for (r in (board.size - 1) downTo 0) {
                if (board[r][c] == CellValue.EMPTY) {
                    foundEmpty = true
                } else if (foundEmpty) {
                    if (newCell == null) {
                        newCell = Pair(r, c)
                    } else {
                        fail("More than one new cell found at (${newCell.first}, ${newCell.second}) and ($r, $c)")
                    }
                }
            }
        }

        // new cell can be generated next to a cell that moved down
        if (newCell != null) {
            assertContains(Board2048.NEW_CELL_VALUES, board[newCell.first][newCell.second])
        }
    }
}
