package net.tmpa.game2048.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class EvaluationLaneTest {

    @Nested
    inner class IdenticalEvaluations {
        @Test
        fun `empty lane evaluates to empty lane`() {
            val lane = EvaluationLane(listOf(CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
            evaluateIdenticalLane(lane)
        }

        @Test
        fun `lane with single value evaluates to same lane`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
            evaluateIdenticalLane(lane)
        }

        @Test
        fun `lane with multiple values evaluates to same lane`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V4, CellValue.V8, CellValue.V16))
            evaluateIdenticalLane(lane)
        }

        @Test
        fun `lane with identical non-neighboring values evaluates to same lane`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V4, CellValue.V2, CellValue.EMPTY))
            evaluateIdenticalLane(lane)
        }

        fun evaluateIdenticalLane(lane: EvaluationLane) {
            val evaluatedLane = lane.evaluate()
            assertEquals(lane, evaluatedLane)
        }
    }

    @Test
    fun `identical neighbouring values are merged correctly`() {
        val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.EMPTY, CellValue.EMPTY))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }

    @Test
    fun `multiple identical neighbouring values are merged correctly`() {
        val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V2, CellValue.V2))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V4, CellValue.EMPTY, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }

    @Test
    fun `complex lane is merged correctly`() {
        val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V4, CellValue.V4))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V8, CellValue.EMPTY, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }

    @Test
    fun `lane with non-neighbouring identical values is merged correctly when separated by empty value`() {
        val lane = EvaluationLane(listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.EMPTY))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }

    @Test
    fun `first two cell out of three identical values in a row are merged`(){
        val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V2, CellValue.EMPTY))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.EMPTY, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }

    @Test
    fun `merges penultimate and last cells`() {
        val lane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V4))
        val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.V8, CellValue.EMPTY))
        val evaluatedLane = lane.evaluate()
        assertEquals(expectedLane, evaluatedLane)
    }
}
