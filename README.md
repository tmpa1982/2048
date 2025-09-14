# Development

## Requirements

Ensure **Java 21** is installed. All other dependencies is per codebase.

## Deployment

- Swagger: https://2048-fecwgmdehaf5dyca.eastasia-01.azurewebsites.net/swagger-ui/index.html
- Azure App Service Plan with F1 (Free) SKU in East Asia region

### User Interface

For easier visualization

- UI deployment: https://tmpa2048.azurewebsites.net/
- UI code repository: http://github.com/tmpa1982/2048-ui

## AI Integration

Requires Azure OpenAI / AI Foundry service with EntraID authentication.

- Foundry endpoint for GPT-5 deployment (East US 2)
- OpenAI endpoint for other GPT-4x deployments (East US)

Replace the endpoints to your own in `application.properties`.

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
