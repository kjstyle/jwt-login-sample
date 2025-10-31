# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.3.3 sample project demonstrating JWT-based authentication using Spring MVC interceptors and argument resolvers. The project uses Java 21 and Gradle as the build tool.

**Key Requirements:**
- Access token for authentication, refresh token for renewal (refresh logic is TODO)
- Argument resolvers to keep JWT logic out of controllers (separation of concerns)
- Interceptor-based authentication (may evolve to Spring Security in future)
- Test-driven development approach

## Build and Test Commands

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "kjstyle.jwtloginsample.jwt.JwtUtilTest"

# Run a specific test method
./gradlew test --tests "kjstyle.jwtloginsample.jwt.JwtUtilTest.testMethodName"

# Run the application
./gradlew bootRun

# Clean build
./gradlew clean build
```

## Architecture

### JWT Authentication Flow

The authentication system is designed around **separation of concerns**, keeping JWT logic out of controllers:

1. **Request arrives** → `AuthenticationInterceptor` (order: 1)
   - Path pattern: `/**` (excludes: `/`, `/open/*`)
   - Extracts token via `JwtInterceptorHelper` from:
     - Header: `Authorization: Bearer <token>`
     - Cookie: `AUTH_ACCESS_TOKEN` (fallback)
   - Validates token via `JwtUtil`
   - Sets `loginUser` request attribute
   - **Note:** Expired token refresh logic is TODO (see line 29 in `AuthenticationInterceptor.java`)

2. **Controller method** → `LoginUserArgumentResolver`
   - Automatically resolves `LoginUser` parameter from request attribute
   - Controllers receive `LoginUser` object directly - no JWT logic needed

### Key Components

**JWT Layer** (`kjstyle.jwtloginsample.jwt`)
- `JwtUtil`: Token creation and validation using jjwt library (io.jsonwebtoken)
  - Secret key: hardcoded in class (consider externalizing)
  - Default expiration: 24 hours (86400000ms)
  - Claims: `userNo` (Long), `userId` (String)
  - Throws `ExpiredTokenException` for expired tokens, `InvalidTokenException` for invalid/tampered tokens
- `JwtInterceptorHelper`: Token extraction from request (header or cookie)

**Auth Layer** (`kjstyle.jwtloginsample.auth`)
- `LoginUser`: Immutable DTO containing authenticated user info (userNo, userId)
- `LoginUserArgumentResolver`: Resolves `LoginUser` parameters in controller methods

**Interceptor** (`kjstyle.jwtloginsample.interceptor`)
- `AuthenticationInterceptor`: Pre-handle authentication logic

**Configuration** (`kjstyle.jwtloginsample.config`)
- `WebConfig`: Registers interceptor and argument resolver

**Exception Handling** (`kjstyle.jwtloginsample.exceptions`)
- `ExpiredTokenException`: For expired tokens (should trigger refresh)
- `InvalidTokenException`: For invalid/tampered tokens
- `UnauthenticatedException`: For missing/failed authentication
- Global exception handling in `ControllerExceptionAdvice` (`mvc` package)

### Testing Infrastructure

**Base Test Classes** (`kjstyle.jwtloginsample.common`)
- `BaseTest`: Basic Spring Boot test setup with `@SpringBootTest`
- `BaseMockMvcTest`: Extends `BaseTest`, provides `MockMvc` with UTF-8 encoding and auto-print
  - Use this for controller tests
  - `MockMvc` is set up in `@BeforeEach` with character encoding filter

**Test Convention:**
- Follow TDD approach: write tests as features are developed
- Controller tests extend `BaseMockMvcTest`
- Unit tests extend `BaseTest`

## Important Notes

### Code Style Conventions (from git history)
- **Lombok usage:** Applied to DTOs and services; avoid on static inner classes
- **Builder pattern:** Use simple constructors for required fields instead of Lombok `@Builder` when controlling required parameters is important
- **Static fields:** Use `static` for constants
- **Imports:** Keep organized and remove unused imports
- **Interceptor methods:** Don't override methods unnecessarily

### Current Limitations
- Refresh token logic is **not implemented** (TODO in `AuthenticationInterceptor.java:29`)
- Secret key is hardcoded in `JwtUtil` (should be externalized to application.yml)
- No database/persistence layer - this is a sample/demo project
- Exception handling for expired tokens exists but refresh flow is incomplete

### Dependencies
- Spring Boot 3.3.3 (Web, Validation)
- jjwt (JWT library): 0.12.6
- Lombok: for reducing boilerplate
- JUnit 5 + Spring Boot Test
