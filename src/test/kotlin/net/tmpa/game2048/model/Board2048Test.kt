package net.tmpa.game2048.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class Board2048Test {

    @Nested
    inner class InitializeRandomBoard {
        @Test
        fun `initialized board contains only allowed initial values`() {
            val allowedInitialValues = listOf(CellValue.EMPTY, CellValue.V2)
            val board = Board2048.initializeRandomBoard()
            for (r in 0 until Board2048.SIZE) {
                for (c in 0 until Board2048.SIZE) {
                    assertTrue(board.getCellValue(r, c) in allowedInitialValues)
                }
            }
        }
    }
}
