package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.AiServiceFactory
import net.tmpa.game2048.ai.GameEvaluator
import net.tmpa.game2048.config.TestConfig
import net.tmpa.game2048.dto.CreateGameResponse
import net.tmpa.game2048.dto.MoveRequest
import net.tmpa.game2048.dto.MoveResponse
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.test.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import kotlin.test.fail

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig::class)
class GameControllerIntegrationTest {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var aiServiceFactory: AiServiceFactory

    private val gameEvaluator = mock<GameEvaluator>()

    @BeforeEach
    fun setup() {
        whenever(aiServiceFactory.createService()).thenReturn(gameEvaluator)
    }

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
