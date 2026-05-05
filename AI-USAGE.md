# AI Usage

## 1. Purpose

This document explains how AI was used while building this authentication service, which prompts were effective, and how corrections were applied during development.

The goal is to make the development process transparent and reproducible.

## 2. How AI Was Used

AI was used as a development assistant for the following tasks:

- Designing the Spring Boot authentication architecture
- Scaffolding the project structure and configuration
- Implementing login, JWT issuance, and token validation
- Creating in-memory demo users for the assessment
- Writing API documentation and architecture documentation
- Troubleshooting environment issues and local execution problems
- Adapting the project to the available local Java and Maven setup

AI was not used as a replacement for verification. The final implementation was checked by running the application and testing the API endpoints locally.

## 3. Prompts Used

The following prompt patterns were useful during development:

### 3.1 Feature Scoping

Prompt pattern:

- Build a backend-only Spring Boot authentication service
- Accept username and password
- Return a token and the user role
- Use either in-memory or database-backed storage
- Keep the design simple and suitable for a technical assessment

Why it worked:

- It established the functional requirements clearly.
- It left room for design decisions where appropriate.
- It encouraged a small, focused implementation.

### 3.2 Implementation Guidance

Prompt pattern:

- Create the Spring Boot project structure
- Add controllers, security configuration, JWT service, DTOs, and exception handling
- Keep the code minimal and readable
- Include a protected endpoint for validation

Why it worked:

- It broke the work into clear layers.
- It encouraged separation of concerns.
- It produced a service that could be tested end to end.

### 3.3 Verification and Debugging

Prompt pattern:

- Check whether the service is running correctly
- Test the login endpoint
- Identify why Thunder Client returns 401
- Fix the underlying authentication issue

Why it worked:

- It focused on observable behavior rather than assumptions.
- It forced the solution to be validated with real requests.
- It helped isolate environment problems from application problems.

### 3.4 Documentation

Prompt pattern:

- Generate a professional architecture document
- Generate an AI usage document
- Describe the request flow, dependencies, and extension points

Why it worked:

- It made the submission more complete.
- It captured the implementation intent for reviewers.
- It clarified how the system is structured.

## 4. Corrections Applied

During development, several corrections were made after validation.

### 4.1 Java and Maven Environment

Issue:

- The local machine did not have `mvn` in the PATH.
- The installed Java runtime was Java 8, which was not compatible with the initial Spring Boot 3 setup.

Correction:

- The project was downgraded to Spring Boot 2.7.18 for Java 8 compatibility.
- A local Maven distribution and a portable JDK were used to run the project.

### 4.2 Security Startup Cycle

Issue:

- The first security configuration introduced a Spring bean cycle at startup.

Correction:

- The JWT filter was injected into the security filter chain method instead of the configuration constructor.

### 4.3 Login Validation Behavior

Issue:

- The login path needed to behave consistently across terminal requests and Thunder Client.
- The initial direct Spring Security authentication path caused confusion during testing.

Correction:

- The login flow was simplified to a direct in-memory credential check.
- JWT generation and protected endpoints remained unchanged.
- The login endpoint now behaves predictably in Thunder Client and terminal tests.

### 4.4 Input Normalization

Issue:

- Copy-paste or formatting differences can introduce whitespace in request payloads.

Correction:

- Username and password input handling was adjusted so the request is parsed more reliably.
- Final testing confirmed the login request works as expected with the documented JSON payload.

## 5. Validation Approach

The project was validated using the following checks:

- Application startup verification
- Health endpoint verification
- Login endpoint verification
- Token issuance verification
- Protected endpoint verification

This ensured the service works as a complete authentication flow, not just as isolated code.

## 6. Recommended Usage Policy for Future AI Assistance

If AI is used again on this project, the following approach should be followed:

- Use AI for scaffolding, explanation, and documentation.
- Confirm all generated code by compiling and running it.
- Prefer small, targeted prompts over vague requests.
- Ask AI to explain tradeoffs when modifying authentication logic.
- Treat AI suggestions as drafts until verified in the editor and runtime.

## 7. Summary

AI was used effectively to accelerate implementation, troubleshooting, and documentation for this authentication service. The final result was corrected through local validation, endpoint testing, and iterative refinement so the project matches the assignment requirements and runs reliably in the available environment.