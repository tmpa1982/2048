package net.tmpa.game2048.ai

import net.tmpa.game2048.model.CellValue
import net.tmpa.game2048.model.MoveDirection
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GameToolsTest {
    private val sut = GameTools()

    @Test
    fun `applies moves to board`() {
        val board = AiBoardDto(listOf(CellValue.V2, CellValue.EMPTY, CellValue.EMPTY, CellValue.V2), 2)
        val actual = sut.applyMoves(board, arrayOf(MoveDirection.DOWN, MoveDirection.RIGHT))
        val expected = AiBoardDto(listOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.V4), 2)
        assertEquals(expected, actual)
    }
}
