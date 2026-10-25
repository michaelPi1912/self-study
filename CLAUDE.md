# CLAUDE.md

Spring Boot 4.1.1 web app (Java 25, Maven, Lombok). Base package: `com.ai.aiengineer`.

## Commands (Windows / PowerShell)

- Run: `.\mvnw.cmd spring-boot:run`
- Build: `.\mvnw.cmd clean package`
- Test all: `.\mvnw.cmd test`
- Test one: `.\mvnw.cmd test -Dtest=ClassName#method`

## Layout

- `src/main/java/com/ai/aiengineer/` — code; entry point `AiEngineerApplication`
- `src/main/resources/application.yaml` — config (YAML, not .properties)
- `src/test/java/` — tests (`spring-boot-starter-webmvc-test`)

## Conventions

- Use Lombok for boilerplate; the annotation processor is configured explicitly in `pom.xml`.
- Web layer uses `spring-boot-starter-webmvc` (Spring MVC).
- Keep new code under `com.ai.aiengineer` subpackages (e.g. `controller`, `service`, `config`).
- Don't commit secrets (API keys); use environment variables referenced from `application.yaml`.
- Never edit `target/`.
