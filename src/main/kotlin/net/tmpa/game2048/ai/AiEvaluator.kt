package net.tmpa.game2048.ai

import com.azure.identity.DefaultAzureCredentialBuilder
import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.service.AiServices
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AiEvaluator(
    @Value($$"${azure.openai.endpoint}") private val endpoint: String,
) {

    fun evaluate(board: String): MoveEvaluation {
        val credential = DefaultAzureCredentialBuilder().build()

        val model = AzureOpenAiChatModel.builder()
            .endpoint(endpoint)
            .deploymentName("gpt-4o-mini")
            .tokenCredential(credential)
            .build()

        val evaluator = AiServices.builder(GameEvaluator::class.java)
            .chatModel(model)
            .build()

        return evaluator.evaluate(board)
    }
}
