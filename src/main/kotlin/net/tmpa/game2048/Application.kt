package net.tmpa.game2048

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class Application {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer = object : WebMvcConfigurer {
        override fun addCorsMappings(registry: CorsRegistry) {
            registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
        }
    }
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
