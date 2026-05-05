# Architecture Overview

## 1. Purpose

This project is a backend-only Spring Boot authentication service designed for an interview-style technical assessment. It exposes a minimal authentication API that accepts a username and password, validates the credentials, issues a JWT, and returns the authenticated user's role.

## 2. Technology Stack

- Java 8
- Spring Boot 2.7.18
- Spring Security
- Spring Web
- JJWT 0.11.5
- Maven

## 3. High-Level Design

The service follows a simple layered architecture:

- Controller layer handles HTTP requests and responses.
- Security layer handles token generation and token-based request authentication.
- User layer provides in-memory user lookup.
- DTO layer defines request and response models.
- Exception layer returns consistent error payloads.

The application is stateless. No session data is stored on the server. Authentication is represented by a JWT passed in the Authorization header.

## 4. Core Components

### 4.1 AuthController

The controller exposes three endpoints:

- POST /api/auth/login
- GET /api/auth/me
- GET /api/auth/health

Responsibilities:

- Accept login requests.
- Validate credentials.
- Generate JWTs.
- Return the authenticated username and role.
- Serve a simple health check endpoint.

### 4.2 DemoUserDetailsService

This component stores demo accounts in memory.

Current demo users:

- admin / Admin123! -> ADMIN
- user / User123! -> USER

Responsibilities:

- Resolve accounts by username.
- Provide a Spring Security UserDetails implementation for protected requests.
- Keep the assignment simple and self-contained.

### 4.3 JwtService

This component is responsible for JWT creation and validation.

Responsibilities:

- Build a signed token after successful login.
- Embed the username and role claims.
- Validate token signature and expiration.
- Extract the username from an incoming token.

### 4.4 JwtAuthenticationFilter

This filter runs before Spring Security authorization.

Responsibilities:

- Read the Bearer token from the Authorization header.
- Parse the token.
- Load the matching user.
- Populate the Spring Security context when the token is valid.

If the request does not include a token, the filter lets the request continue and authorization is handled later by Spring Security.

### 4.5 SecurityConfig

This class defines the security rules for the application.

Responsibilities:

- Disable CSRF for stateless API usage.
- Configure stateless session management.
- Permit anonymous access to login and health endpoints.
- Require authentication for all other requests.
- Register the JWT authentication filter.

### 4.6 DTOs

The API uses dedicated DTOs to separate external payloads from internal security objects.

- LoginRequest: incoming username/password payload.
- AuthResponse: login response containing token and role.
- MeResponse: authenticated user summary.

### 4.7 GlobalExceptionHandler

This class centralizes error responses.

Responsibilities:

- Convert bad credentials into a 401 response.
- Convert validation failures into a 400 response.
- Return a consistent JSON error structure.

## 5. Request Flow

### 5.1 Login Flow

1. Client sends POST /api/auth/login with username and password.
2. AuthController validates and normalizes the input.
3. DemoUserDetailsService resolves the account.
4. Credentials are checked directly against the in-memory account store.
5. JwtService generates a signed JWT.
6. AuthController returns the token, username, role, and expiration.

### 5.2 Protected Request Flow

1. Client sends a request to a protected endpoint with Authorization: Bearer <token>.
2. JwtAuthenticationFilter extracts the token.
3. JwtService validates token signature and expiration.
4. DemoUserDetailsService loads the user associated with the token subject.
5. Spring Security stores the authenticated principal in the security context.
6. The request is authorized and the controller executes.

## 6. Security Model

The service uses JWT-based stateless authentication.

Key properties:

- The server does not store sessions.
- Authentication state is carried in the token.
- The token is signed using an HMAC secret.
- Protected endpoints require a valid Bearer token.

Current design tradeoffs:

- In-memory users keep the project simple and easy to run.
- The shared secret should be replaced for production use.
- Token expiration is intentionally short and configurable.

## 7. Error Handling

The service returns JSON errors for expected authentication failures.

Examples:

- 401 Unauthorized for invalid credentials.
- 400 Bad Request for invalid login payloads.

This keeps the API predictable for testing tools such as Thunder Client and Postman.

## 8. Package Structure

- src/main/java/com/example/authenticationservice/controller
- src/main/java/com/example/authenticationservice/config
- src/main/java/com/example/authenticationservice/security
- src/main/java/com/example/authenticationservice/dto
- src/main/java/com/example/authenticationservice/exception

## 9. Extensibility Points

The current implementation can be extended without changing the public API.

Possible next steps:

- Replace in-memory users with a database-backed repository.
- Add registration and password reset flows.
- Add refresh tokens.
- Move the JWT secret to environment variables.
- Add unit and integration tests.
- Add role-based endpoint restrictions.

## 10. Operational Notes

- Application port: 8080
- Health endpoint: /api/auth/health
- Login endpoint: /api/auth/login
- Protected profile endpoint: /api/auth/me

## 11. Summary

This service is intentionally small, stateless, and easy to run locally. The design uses Spring Security for request protection and JWT for authentication state, while keeping the user store in memory so the assessment can be verified quickly without external dependencies.