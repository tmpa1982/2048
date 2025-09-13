package net.tmpa.game2048.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.Test
import kotlin.test.assertContains

class Board2048Test {

    @Nested
    inner class InitializeRandomBoard {
        @Test
        fun `initialized board contains only allowed initial values`() {
            val allowedInitialValues = listOf(CellValue.EMPTY, CellValue.V2)
            val board = Board2048.initializeRandomBoard()
            for (r in 0 until Board2048.DEFAULT_SIZE) {
                for (c in 0 until Board2048.DEFAULT_SIZE) {
                    assertTrue(board.getCellValue(r, c) in allowedInitialValues)
                }
            }
        }

        @Test
        fun `initialized board has at least two V2 cells`() {
            val board = Board2048.initializeRandomBoard { CellValue.EMPTY }
            var v2Count = 0
            for (r in 0 until Board2048.DEFAULT_SIZE) {
                for (c in 0 until Board2048.DEFAULT_SIZE) {
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

            val mergedBoard = board.nextBoard(MoveDirection.LEFT)

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

            val mergedBoard = board.nextBoard(MoveDirection.RIGHT)

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

            val mergedBoard = board.nextBoard(MoveDirection.UP)

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

        @Test
        fun `merge down`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.V2, CellValue.V4, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.V8, CellValue.V8),
                    arrayOf(CellValue.V16, CellValue.V16, CellValue.EMPTY, CellValue.V16),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                )
            )
            val mergedBoard = board.nextBoard(MoveDirection.DOWN)

            val expectedBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.V2, CellValue.EMPTY, CellValue.EMPTY),
                    arrayOf(CellValue.V16, CellValue.V16, CellValue.V4, CellValue.V8),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V16, CellValue.V32),
                )
            )
            assertEquals(expectedBoard, mergedBoard)
        }

        @ParameterizedTest
        @EnumSource(MoveDirection::class)
        fun `merging keeps board size unchanged`(direction: MoveDirection) {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V2),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY),
                )
            )
            val mergedBoard = board.nextBoard(direction)
            assertEquals(board.size, mergedBoard.size)
        }
    }

    @Nested
    inner class Evaluation {
        @Test
        fun `board is winning when it contains V2048 cell`() {
            val winningBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                    arrayOf(CellValue.V32, CellValue.V64, CellValue.V128, CellValue.V256),
                    arrayOf(CellValue.V512, CellValue.V1024, CellValue.V2048, CellValue.EMPTY),
                    arrayOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY),
                )
            )
            assertTrue(winningBoard.isWinning())
            assertFalse(winningBoard.isLosing())
        }

        @Test
        fun `board is losing when no moves are possible`() {
            val losingBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V2),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V2),
                )
            )
            assertTrue(losingBoard.isLosing())
        }

        @Test
        fun `board is not losing when two horizontal neighboring cells can be merged`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V2, CellValue.V4, CellValue.V8),
                    arrayOf(CellValue.V16, CellValue.V32, CellValue.V64, CellValue.V128),
                    arrayOf(CellValue.V256, CellValue.V512, CellValue.V1024, CellValue.V2),
                    arrayOf(CellValue.V4, CellValue.V8, CellValue.V16, CellValue.V32),
                )
            )
            assertFalse(board.isLosing())
        }

        @Test
        fun `board is not losing when two vertical neighboring cells can be merged`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                    arrayOf(CellValue.V2, CellValue.V32, CellValue.V64, CellValue.V128),
                    arrayOf(CellValue.V256, CellValue.V512, CellValue.V1024, CellValue.V2),
                    arrayOf(CellValue.V4, CellValue.V8, CellValue.V16, CellValue.V32),
                )
            )
            assertFalse(board.isLosing())
        }

        @Test
        fun `board is not losing when at least one cell is empty`() {
            val board = Board2048(
                arrayOf(
                    arrayOf(CellValue.EMPTY, CellValue.V4, CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V2),
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V2),
                )
            )
            assertFalse(board.isLosing())
        }
    }

    @Nested
    inner class Transformation {
        @Test
        fun `board can be transformed from list`() {
            val flattenedList = listOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16)
            val expected = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4),
                    arrayOf(CellValue.V8, CellValue.V16),
                )
            )
            val actual = Board2048.createFromList(flattenedList, 2)
            assertEquals(expected, actual)
        }

        @Test
        fun `throws if list has less elements`() {
            val flattenedList = listOf(CellValue.V2, CellValue.V4, CellValue.V8)
            assertThrows<IllegalArgumentException> {
                Board2048.createFromList(flattenedList, 2)
            }
        }


        @Test
        fun `throws if list has more elements`() {
            val flattenedList = listOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16, CellValue.V32)
            assertThrows<IllegalArgumentException> {
                Board2048.createFromList(flattenedList, 2)
            }
        }
    }

    @Nested
    inner class AddRandomCell {
        @Test
        fun `returns the same board when trying to add a cell to a full board`() {
            val fullBoard = Board2048(
                arrayOf(
                    arrayOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16),
                    arrayOf(CellValue.V32, CellValue.V64, CellValue.V128, CellValue.V256),
                    arrayOf(CellValue.V512, CellValue.V1024, CellValue.V2048, CellValue.V2),
                    arrayOf(CellValue.V4, CellValue.V8, CellValue.V16, CellValue.V32),
                )
            )

            assertSame(fullBoard, fullBoard.addRandomCell())
        }

        @Test
        fun `adds a random cell to one of the empty positions`() {
            val boardArray = arrayOf(
                arrayOf(CellValue.V2, CellValue.EMPTY, CellValue.V4, CellValue.EMPTY),
                arrayOf(CellValue.EMPTY, CellValue.V8, CellValue.EMPTY, CellValue.V16),
                arrayOf(CellValue.V32, CellValue.EMPTY, CellValue.V64, CellValue.EMPTY),
                arrayOf(CellValue.EMPTY, CellValue.V128, CellValue.EMPTY, CellValue.V256),
            )
            val board = Board2048(boardArray)

            val emptyCells = mutableListOf<Pair<Int, Int>>()
            for (r in 0 until board.size) {
                for (c in 0 until board.size) {
                    if (boardArray[r][c] == CellValue.EMPTY) {
                        emptyCells.add(Pair(r, c))
                    }
                }
            }

            fun assertModifiedBoard(targetCell: Pair<Int, Int>, boardWithNewCell: Board2048) {
                for (r in 0 until board.size) {
                    for (c in 0 until board.size) {
                        if (r == targetCell.first && c == targetCell.second) {
                            assertEquals(CellValue.V4, boardWithNewCell.getCellValue(r, c))
                        } else {
                            assertEquals(boardArray[r][c], boardWithNewCell.getCellValue(r, c))
                        }
                    }
                }
            }

            for (targetCell in emptyCells) {
                val boardWithNewCell = board.addRandomCell(
                    emptyCellPicker = { targetCell },
                    valueGenerator = { CellValue.V4 }
                )

                assertModifiedBoard(targetCell, boardWithNewCell)
            }
        }

        @Test
        fun `does not add random cell if move does not change the board`() {
            val boardArray = arrayOf(
                arrayOf(CellValue.V2, CellValue.V4),
                arrayOf(CellValue.EMPTY, CellValue.EMPTY),
            )
            val board = Board2048(boardArray)

            val newBoard = board.nextBoardAndCell(MoveDirection.LEFT)

            assertEquals(board, newBoard)
        }

        @Test
        fun `moves and adds random cell in combined call`() {
            val boardArray = arrayOf(
                arrayOf(CellValue.V2, CellValue.V2),
                arrayOf(CellValue.EMPTY, CellValue.EMPTY),
            )
            val board = Board2048(boardArray)

            val newBoard = board.nextBoardAndCell(MoveDirection.LEFT)

            assertEquals(CellValue.V4, newBoard.getCellValue(0, 0))
            val newCellValue = listOf(
                newBoard.getCellValue(0, 1),
                newBoard.getCellValue(1, 0),
                newBoard.getCellValue(1, 1),
            ).single { it != CellValue.EMPTY }

            assertContains(Board2048.NEW_CELL_VALUES, newCellValue)
        }
    }
}
