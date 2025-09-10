package net.tmpa.game2048.ai

import dev.langchain4j.service.SystemMessage
import dev.langchain4j.service.UserMessage

interface GameEvaluator {
    @SystemMessage("You are a 2048 game evaluator. Always return valid JSON.")
    fun evaluate(@UserMessage board: String): MoveEvaluation
}
