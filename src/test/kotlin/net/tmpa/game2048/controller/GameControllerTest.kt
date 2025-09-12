package net.tmpa.game2048.controller

import net.tmpa.game2048.dto.CreateGameResponse
import net.tmpa.game2048.dto.MoveRequest
import net.tmpa.game2048.dto.MoveResponse
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.fail

class GameControllerIntegrationTest : IntegrationTestBase() {
    @Test
    fun `let's play a game`() {
        val response = restTemplate.postForEntity("/api/game", null, CreateGameResponse::class.java)
        val gameId = response.body!!.id
        val initialBoard = response.body!!.board
        assertInitialBoard(initialBoard.cells)

        val moveRequest = MoveRequest(MoveDirection.DOWN)
        val moveResponse = restTemplate.postForEntity("/api/game/$gameId/move", moveRequest, MoveResponse::class.java)
        val newBoard = moveResponse.body!!.board
        assertBoardMovedDown(newBoard.cells)

        val initialBoardSum = initialBoard.cells.flatten().sumOf { it.value }
        val newBoardSum = newBoard.cells.flatten().sumOf { it.value }
        val difference = newBoardSum - initialBoardSum
        assertContains(listOf(2, 4), difference)
    }

    private fun assertInitialBoard(board: List<List<CellValue>>) {
        val numEmpty = board.flatten().count { it == CellValue.EMPTY }
        assertNotEquals(0, numEmpty, "There should be some empty cells")
    }

    private fun assertBoardMovedDown(board: List<List<CellValue>>) {
        for (c in 0 until board.size) {
            var foundEmpty = false
            for (r in (board.size - 1) downTo 0) {
                if (board[r][c] == CellValue.EMPTY) {
                    foundEmpty = true
                } else if (foundEmpty) {
                    fail("Non-empty cell found above an empty cell in column $c")
                }
            }
        }
    }
}
