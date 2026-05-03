# Devboard

A developer dashboard application with a Vue 3 frontend and Spring Boot backend, containerized with Docker.

## Tech Stack

**Frontend**
- Vue 3 + TypeScript
- Vite
- Vue Router
- Pinia (state management)
- Axios (HTTP client)
- Chart.js

**Backend**
- Java 17
- Spring Boot 3.5
- Spring Data JPA
- Spring Security
- PostgreSQL

**Infrastructure**
- Docker + Docker Compose

## Project Structure

```
devboard/
├── frontend/        # Vue 3 + Vite app
├── backend/         # Spring Boot app
└── docker-compose.yml
```

## Getting Started

### Prerequisites
- Docker and Docker Compose

### Run with Docker

```bash
docker-compose up --build
```

| Service  | URL                   |
|----------|-----------------------|
| Frontend | http://localhost:5173 |
| Backend  | http://localhost:8080 |
| Postgres | localhost:5432        |

### Run Locally (without Docker)

**Backend**

Set the following environment variables, then run:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/devboard
export DB_USER=dev
export DB_PASS=dev
export JWT_SECRET=change-me-in-prod
```

```bash
cd backend
./gradlew bootRun
```

**Frontend**

```bash
cd frontend
npm install
npm run dev
```

## Environment Variables

| Variable     | Description                        | Default (docker-compose)                      |
|--------------|------------------------------------|-----------------------------------------------|
| `DB_URL`     | JDBC connection URL                | `jdbc:postgresql://postgres:5432/devboard`    |
| `DB_USER`    | Database username                  | `dev`                                         |
| `DB_PASS`    | Database password                  | `dev`                                         |
| `JWT_SECRET` | Secret key for JWT signing         | `change-me-in-prod`                           |

> **Note:** Change `JWT_SECRET` to a strong random value before deploying to any non-local environment.

## Changelog

### Initial commit
- Scaffolded Vue 3 + TypeScript frontend with Vite, Vue Router, Pinia, Axios, and Chart.js
- Scaffolded Spring Boot 3.5 backend with JPA, Security, Validation, and PostgreSQL driver
- Configured `application.yaml` to read datasource settings from environment variables
- Added Docker Compose setup with `postgres`, `backend`, and `frontend` services on a shared network
