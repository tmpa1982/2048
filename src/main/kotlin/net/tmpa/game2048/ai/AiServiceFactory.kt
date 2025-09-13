package net.tmpa.game2048.ai

import com.azure.identity.DefaultAzureCredentialBuilder
import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.chat.request.ResponseFormat
import dev.langchain4j.service.AiServices
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AiServiceFactory(
    @Value($$"${azure.openai.endpoint}") private val endpoint: String,
    @Value($$"${azure.aifoundry.endpoint}") private val foundryEndpoint: String,
) {
    private val tools = GameTools()

    fun createService(deploymentName: String): GameEvaluator {
        fun selectEndpoint() = if (deploymentName == "gpt-5-chat") foundryEndpoint else endpoint

        val credential = DefaultAzureCredentialBuilder().build()

        val model = AzureOpenAiChatModel.builder()
            .endpoint(selectEndpoint())
            .deploymentName(deploymentName)
            .tokenCredential(credential)
            .responseFormat(ResponseFormat.JSON)
            .build()

        val evaluator = AiServices.builder(GameEvaluator::class.java)
            .chatModel(model)
            .tools(tools)
            .build()

        return evaluator
    }
}
