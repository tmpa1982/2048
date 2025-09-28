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
            assertEquals(EvaluationLaneResult(lane, 0), evaluatedLane)
        }
    }

    @Nested
    inner class NoObstacleMerges {
        @Test
        fun `identical neighbouring values are merged correctly`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.EMPTY, CellValue.EMPTY))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
            assertLane(lane, expectedLane, 4)
        }

        @Test
        fun `multiple identical neighbouring values are merged correctly`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V2, CellValue.V2))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V4, CellValue.EMPTY, CellValue.EMPTY))
            assertLane(lane, expectedLane, 8)
        }

        @Test
        fun `complex lane is merged correctly`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V4, CellValue.V4))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V8, CellValue.EMPTY, CellValue.EMPTY))
            assertLane(lane, expectedLane, 12)
        }

        @Test
        fun `lane with non-neighbouring identical values is merged correctly when separated by empty value`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.EMPTY, CellValue.V2, CellValue.EMPTY))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.EMPTY, CellValue.EMPTY, CellValue.EMPTY))
            assertLane(lane, expectedLane, 4)
        }

        @Test
        fun `first two cell out of three identical values in a row are merged`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V2, CellValue.EMPTY))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.EMPTY, CellValue.EMPTY))
            assertLane(lane, expectedLane, 4)
        }

        @Test
        fun `merges penultimate and last cells`() {
            val lane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.V4, CellValue.V4))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V2, CellValue.V8, CellValue.EMPTY))
            assertLane(lane, expectedLane, 8)
        }

        private fun assertLane(
            lane: EvaluationLane,
            expectedLane: EvaluationLane,
            expectedScore: Int,
        ) {
            val evaluatedLane = lane.evaluate()
            assertEquals(EvaluationLaneResult(expectedLane, expectedScore), evaluatedLane)
        }
    }

    @Nested
    inner class ObstacleMerges {
        @Test
        fun `lane with obstacle and identical values before and after obstacle merges correctly`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.OBSTACLE, CellValue.V2, CellValue.V2))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.EMPTY, CellValue.OBSTACLE, CellValue.V4, CellValue.EMPTY))
            evaluateLane(lane, expectedLane, 8)
        }

        @Test
        fun `lane with obstacle and non-identical values before and after obstacle does not merge`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V4, CellValue.OBSTACLE, CellValue.V2, CellValue.V4))
            val expectedLane = EvaluationLane(listOf(CellValue.V2, CellValue.V4, CellValue.OBSTACLE, CellValue.V2, CellValue.V4))
            evaluateLane(lane, expectedLane, 0)
        }

        @Test
        fun `lane with obstacle at the beginning of the lane merges correctly`() {
            val lane = EvaluationLane(listOf(CellValue.OBSTACLE, CellValue.V2, CellValue.V2, CellValue.V4))
            val expectedLane = EvaluationLane(listOf(CellValue.OBSTACLE, CellValue.V4, CellValue.V4, CellValue.EMPTY))
            evaluateLane(lane, expectedLane, 4)
        }

        @Test
        fun `lane with obstacle at the end of the lane merges correctly`() {
            val lane = EvaluationLane(listOf(CellValue.V2, CellValue.V2, CellValue.V4, CellValue.OBSTACLE))
            val expectedLane = EvaluationLane(listOf(CellValue.V4, CellValue.V4, CellValue.EMPTY, CellValue.OBSTACLE))
            evaluateLane(lane, expectedLane, 4)
        }

        private fun evaluateLane(
            lane: EvaluationLane,
            expectedLane: EvaluationLane,
            expectedScore: Int,
        ) {
            val evaluatedLane = lane.evaluate()
            assertEquals(EvaluationLaneResult(expectedLane, expectedScore), evaluatedLane)
        }
    }
}
