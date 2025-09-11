package net.tmpa.game2048.dto

import net.tmpa.game2048.model.CellValue
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class EvaluationRequestTest {
    @Test
    fun `square board is valid`() {
        val request = EvaluationRequest(
            listOf(
                listOf(CellValue.V2, CellValue.V4),
                listOf(CellValue.V8, CellValue.V16),
            )
        )
        assertTrue(request.isValid())
    }

    @Test
    fun `rectangle board is not valid`() {
        val request = EvaluationRequest(
            listOf(
                listOf(CellValue.V2, CellValue.V4, CellValue.V8),
                listOf(CellValue.V16, CellValue.V32, CellValue.V64),
            )
        )
        assertFalse(request.isValid())
    }

    @Test
    fun `empty board is not valid`() {
        val request = EvaluationRequest(emptyList())
        assertFalse(request.isValid())
    }

    @Test
    fun `board with empty rows is not valid`() {
        val request = EvaluationRequest(
            listOf(
                emptyList(),
                emptyList(),
            )
        )
        assertFalse(request.isValid())
    }

    @Test
    fun `board with inconsistent row sizes is not valid`() {
        val request = EvaluationRequest(
            listOf(
                listOf(CellValue.V2, CellValue.V4),
                listOf(CellValue.V8),
            )
        )
        assertFalse(request.isValid())
    }
}
