# Development

## Requirements

Ensure **Java 21** is installed. All other dependencies is per codebase.

## AI Integration

Requires Azure OpenAI service with EntraID authentication.

Replace the endpoint to your own in `application.properties`.

Application can run without this, but obviously AI evaluation won't work.

## Run

```
.\gradlew bootRun
```

Swagger will be available at:

```
https://localhost:8080/swagger-ui/index.html
```

# Limitations

Azure App Service doesn't support Java 24. Highest supported version is Java 21.
