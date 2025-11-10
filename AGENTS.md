# Repository Guidelines

## Project Structure & Module Organization
Core entrypoint lives in `src/main/java/kjstyle/jwtloginsample/JwtLoginSampleApplication.java`. Authentication helpers are grouped under `auth` (e.g., `LoginUser`, `LoginUserArgumentResolver`), token utilities under `jwt` (`JwtUtil`, `JwtInterceptorHelper`), and request guards under `interceptor/AuthenticationInterceptor`. Use `sample/SampleController` as the reference endpoint while adding new controllers. Cross-cutting glue such as exception handling and MVC config stay in `mvc` and `config`. Static configuration defaults, including the JWT secret and expiration, live in `src/main/resources/application.yml`. Tests mirror the main tree inside `src/test/java/kjstyle/jwtloginsample` with shared scaffolding inside `common`.

## Build, Test, and Development Commands
- `./gradlew clean build` — compile with Java 21, run unit/integration tests, and assemble the boot JAR.
- `./gradlew test` — execute the JUnit 5 suite; use `--info` when chasing flaky behavior.
- `./gradlew bootRun` — start the Spring Boot app on port 8080 using local configuration, ideal for manual testing with curl/Postman.

## Coding Style & Naming Conventions
Stick to the existing Spring Boot + Lombok idioms: 4-space indentation, `UpperCamelCase` for types, `camelCase` for methods/fields, and `UPPER_SNAKE_CASE` for constants. Keep packages lowercase (e.g., `kjstyle.jwtloginsample.jwt`). Controllers should stay annotation-driven with constructor injection (when needed) and log through `@Slf4j`. Favor DTOs over maps for request/response payloads, and return immutable objects such as `LoginUser`. When introducing filters/interceptors, colocate helpers beside them (`interceptor` or `jwt`) to keep cross-cutting code easy to trace.

## Testing Guidelines
All tests run on JUnit 5 with Spring Boot test slices. Reuse `BaseTest` for context loads and `BaseMockMvcTest` when you need `MockMvc` with UTF-8 enforcement and printed output. Name tests descriptively (underscores and Korean sentences, as seen in `SampleControllerTest`, are acceptable) and assert both HTTP status and payload fields via `jsonPath`. Cover every branch that manipulates JWTs, especially failure cases throwing `InvalidTokenException` or `ExpiredTokenException`. Prefer factory helpers for token creation (`JwtUtil#createAccessToken`) to keep assertions deterministic.

## Commit & Pull Request Guidelines
Follow the history pattern: concise, present-tense summaries (~50 chars) such as `리프레시 토큰 기능 구현` or `static inner class 정리`. Each PR should explain motivation, list key changes, and call out any new endpoints or configs. Link the relevant issue, include reproduction steps or curl examples, and attach screenshots/log excerpts when behavior changes.

## Security & Configuration Tips
Never commit real secrets—override `spring.security.secret` and `spring.security.expiration` via environment variables or an external profile before deploying. Treat generated JWTs like credentials; redact them in logs or tests unless intentionally mocked. When sharing sample tokens, place them under `src/test/resources` fixtures instead of inline constants so they cannot leak into production builds.
