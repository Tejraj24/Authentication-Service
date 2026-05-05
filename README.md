# Authentication Service

Backend-only Spring Boot authentication service for the assessment.

## What it does

- Authenticates users with username and password
- Returns a JWT token and the user's role
- Uses in-memory users for simplicity
- Protects a sample `/api/auth/me` endpoint with the token

## Default users

- `admin` / `Admin123!` -> `ADMIN`
- `user` / `User123!` -> `USER`

## Run on this machine

```powershell
C:\Users\Lenovo\AppData\Local\Programs\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run
```

This project now targets Java 8, so it can run with the Java version already installed on this machine.

## Deploy on Render

Use the Render Blueprint in `render.yaml`.

Recommended settings:

- Root Directory: leave blank
- Environment: use the repository `render.yaml`
- Branch: `main`
- Health Check Path: `/api/auth/health`

The blueprint generates `APP_JWT_SECRET` automatically.

Render will build the Dockerfile, start the app, and route traffic to the port exposed by Spring Boot.

## Login example

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"Admin123!\"}"
```

## Protected endpoint example

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <token>"
```