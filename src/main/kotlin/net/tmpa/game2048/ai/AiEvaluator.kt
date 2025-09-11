package net.tmpa.game2048.ai

import com.azure.identity.DefaultAzureCredentialBuilder
import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.request.ResponseFormat
import dev.langchain4j.service.AiServices
import net.tmpa.game2048.model.Board2048
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AiEvaluator(
    @Value($$"${azure.openai.endpoint}") private val endpoint: String,
) {

    private val tools = GameTools()

    fun evaluate(board: Board2048): MoveEvaluation {
        val credential = DefaultAzureCredentialBuilder().build()

        val model = AzureOpenAiChatModel.builder()
            .endpoint(endpoint)
            .deploymentName("gpt-4o-mini")
            .tokenCredential(credential)
            .responseFormat(ResponseFormat.JSON)
            .build()

        val evaluator = AiServices.builder(GameEvaluator::class.java)
            .chatModel(model)
            .tools(tools)
            .build()

        return evaluator.evaluate(board.asList())
    }
}
