package net.tmpa.game2048.controller

import net.tmpa.game2048.ai.AiServiceFactory
import net.tmpa.game2048.ai.GameEvaluator
import net.tmpa.game2048.config.TestConfig
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.test.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestConfig::class)
abstract class IntegrationTestBase {
    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var aiServiceFactory: AiServiceFactory

    protected val gameEvaluator = mock<GameEvaluator>()

    @BeforeEach
    fun setup() {
        whenever(aiServiceFactory.createService(anyString())).thenReturn(gameEvaluator)
    }
}
