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

        @Test
        fun `initialized board has at least two V2 cells`() {
            val board = Board2048.initializeRandomBoard { CellValue.EMPTY }
            var v2Count = 0
            for (r in 0 until Board2048.SIZE) {
                for (c in 0 until Board2048.SIZE) {
                    if (board.getCellValue(r, c) == CellValue.V2) {
                        v2Count++
                    }
                }
            }
            assertTrue(v2Count >= 2, "Board should have at least two cells with value 2")
        }
    }

    @Nested
    inner class MergeBoard {
        @Test
        fun `merge left`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.V2, CellValue.V4, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V8, CellValue.V8),
                    arrayOf(CellValue.V16, CellValue.V16, CellValue.EMPTY, CellValue.V16),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )

            val mergedBoard = board.mergeLeft()

            val expectedBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.V16, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.V32, CellValue.V16, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )

            assertEquals(expectedBoard, mergedBoard)
        }

        @Test
        fun `merge right`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.V2, CellValue.V4, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V8, CellValue.V8),
                    arrayOf(CellValue.V16, CellValue.V16, CellValue.EMPTY, CellValue.V16),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )

            val mergedBoard = board.mergeRight()

            val expectedBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.V16),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V16, CellValue.V32),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )

            assertEquals(expectedBoard, mergedBoard)
        }

        @Test
        fun `merge up`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.V2, CellValue.V4, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V8, CellValue.V8),
                    arrayOf(CellValue.V16, CellValue.V16, CellValue.EMPTY, CellValue.V16),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )

            val mergedBoard = board.mergeUp()

            val expectedBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.V16, CellValue.V2, CellValue.V4, CellValue.V8),
                    arrayOf(CellValue.V2, CellValue.V16, CellValue.V16, CellValue.V32),
                    arrayOf(CellValue.EMPTY, CellValue.V4, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY),
                )
            )

            assertEquals(expectedBoard, mergedBoard)
        }
    }
}
