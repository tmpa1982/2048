package net.tmpa.game2048.ai

import net.tmpa.game2048.dto.BoardDto
import net.tmpa.game2048.dto.EvaluationRequest
import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.Test

class NextMoveEvaluatorServiceTest {
    private val serviceFactory = mock<AiServiceFactory>()
    private val evaluator = mock<GameEvaluator>()
    private val sut = NextMoveEvaluatorService(serviceFactory)

    private val sampleBoardAsList = listOf(
        listOf(CellValue.V2, CellValue.EMPTY),
        listOf(CellValue.EMPTY, CellValue.V2),
    )
    private val sampleRequest = EvaluationRequest(sampleBoardAsList)
    val sampleEvaluation = MoveEvaluation(MoveDirection.UP, "testReason")

    @BeforeEach
    fun setup() {
        whenever(serviceFactory.createService(anyString())).thenReturn(evaluator)
        whenever(evaluator.evaluate(any())).thenReturn(sampleEvaluation)
    }

    @Test
    fun `returns evaluation and modified board`() {
        val response = sut.evaluate(sampleRequest)

        assertEquals(sampleEvaluation, response.evaluation)
        val expectedBoard = BoardDto(
            listOf(
                listOf(CellValue.V2, CellValue.V2),
                listOf(CellValue.EMPTY, CellValue.EMPTY),
            )
        )
        assertEquals(expectedBoard, response.board)
    }

    @Test
    fun `returns evaluation and same board if evaluation does not provide a direction`() {
        val evaluation = MoveEvaluation(null, "no move possible")
        whenever(evaluator.evaluate(any())).thenReturn(evaluation)

        val response = sut.evaluate(sampleRequest)

        assertEquals(evaluation, response.evaluation)
        assertEquals(BoardDto(sampleBoardAsList), response.board)
    }

    @Test
    fun `creates service with defined model`() {
        val model = "my-awesome-model"
        val modelRequest = EvaluationRequest(sampleBoardAsList, model)
        sut.evaluate(modelRequest)

        verify(serviceFactory).createService(model)
    }
}
