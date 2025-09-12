package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.MoveEvaluation
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.dto.EvaluationResponse
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class EvaluationControllerIntegrationTest : IntegrationTestBase() {
    @Test
    fun `returns best evaluation from AI`() {
        val moveEvaluation = MoveEvaluation(MoveDirection.LEFT, "")
        whenever(gameEvaluator.evaluate(any())).thenReturn(
            moveEvaluation
        )

        val request = EvaluationRequest(
            board = listOf(
                listOf(CellValue.V2, CellValue.V4, CellValue.V2, CellValue.V2),
                listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.V2),
                listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.V2),
                listOf(CellValue.EMPTY, CellValue.V4, CellValue.EMPTY, CellValue.V2),
            ),
        )
        val response = restTemplate.postForEntity("/api/evaluate", HttpEntity(request), EvaluationResponse::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(moveEvaluation, response.body!!.evaluation)
    }

    @Test
    fun `returns 400 for invalid board`() {
        val request = EvaluationRequest(
            board = listOf(
                listOf(CellValue.V2, CellValue.V4, CellValue.V2),
                listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.V2),
                listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.V2),
                listOf(CellValue.EMPTY, CellValue.V4, CellValue.EMPTY, CellValue.V2),
            ),
        )
        val response = restTemplate.postForEntity("/api/evaluate", HttpEntity(request), String::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }
}
