package net.tmpa.game2048.model

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class CellValueTest {
    @Test
    fun `test enum values`() {
        var expectedCellValue = CellValue.EMPTY
        var expectedNumeric = 0
        for (cellValue in CellValue.entries.filter { it != CellValue.OBSTACLE }) {
            assertEquals(expectedCellValue, cellValue)
            assertEquals(expectedNumeric, cellValue.value)
            if (cellValue != CellValue.V2048) {
                expectedCellValue = cellValue.getNext()
                expectedNumeric = if (expectedNumeric == 0) 2 else expectedNumeric * 2
            }
        }
    }

    @Test
    fun `next value on 2048 throws exception`() {
        assertThrows<IllegalStateException> { CellValue.V2048.getNext() }
    }

    @Test
    fun `next value on obstacle throws exception`() {
        assertThrows<IllegalStateException> { CellValue.OBSTACLE.getNext() }
    }
}
