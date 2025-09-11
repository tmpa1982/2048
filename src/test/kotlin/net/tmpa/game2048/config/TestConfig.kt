package net.tmpa.game2048.config

import net.tmpa.game2048.ai.AiServiceFactory
import org.mockito.Mockito
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun mockAiServiceFactory(): AiServiceFactory {
        return Mockito.mock(AiServiceFactory::class.java)
    }
}
