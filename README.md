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
- Spring Security (stateless JWT)
- JJWT 0.12.6
- PostgreSQL

**Infrastructure**
- Docker + Docker Compose

## Project Structure

```
devboard/
├── frontend/                  # Vue 3 + Vite app
├── backend/
│   └── src/main/java/com/poojarysaurav/devboard/
│       ├── auth/              # Registration, login, User entity, DTOs
│       ├── task/              # Task entity, CRUD
│       ├── stats/             # Aggregated task stats
│       └── common/
│           ├── config/        # CORS (WebConfig)
│           ├── security/      # JWT filter, SecurityConfig, UserDetailsService
│           └── exception/     # GlobalExceptionHandler
├── api-tests/                 # REST Client .http files
└── docker-compose.yml
```

## Getting Started

### Prerequisites
- Docker and Docker Compose

### Run with Docker

```bash
docker-compose build backend && docker-compose up -d
```

| Service  | URL                   |
|----------|-----------------------|
| Frontend | http://localhost:5173 |
| Backend  | http://localhost:8080 |
| Postgres | localhost:5432        |

**Useful commands**
```bash
docker-compose logs -f backend   # tail backend logs
docker-compose ps                # check container status
docker-compose down              # stop everything
```

### Run Locally (without Docker)

**Backend** — set env vars then run:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/devboard
export DB_USER=dev
export DB_PASS=dev
export JWT_SECRET=devboard-super-secret-key-change-in-production-min32chars
```

```bash
cd backend && ./gradlew bootRun
```

**Frontend**

```bash
cd frontend && npm install && npm run dev
```

## Environment Variables

| Variable     | Description                  | Default (docker-compose)                                          |
|--------------|------------------------------|-------------------------------------------------------------------|
| `DB_URL`     | JDBC connection URL          | `jdbc:postgresql://postgres:5432/devboard`                        |
| `DB_USER`    | Database username            | `dev`                                                             |
| `DB_PASS`    | Database password            | `dev`                                                             |
| `JWT_SECRET` | Secret key for JWT signing   | `devboard-super-secret-key-change-in-production-min32chars`       |

> **Note:** `JWT_SECRET` must be at least 32 characters (256 bits). Change it before any non-local deployment.

## API Reference

Base URL: `http://localhost:8080`

### Auth

| Method | Endpoint              | Auth | Description        |
|--------|-----------------------|------|--------------------|
| POST   | `/api/auth/register`  | No   | Register new user  |
| POST   | `/api/auth/login`     | No   | Login, returns JWT |

**Register** `POST /api/auth/register`
```json
{
  "name": "Saurav",
  "email": "saurav@test.com",
  "password": "password123"
}
```
Response `201`:
```json
{ "token": "eyJ...", "email": "saurav@test.com", "name": "Saurav" }
```

**Login** `POST /api/auth/login`
```json
{ "email": "saurav@test.com", "password": "password123" }
```
Response `200`:
```json
{ "token": "eyJ...", "email": "saurav@test.com", "name": "Saurav" }
```

All other routes require `Authorization: Bearer <token>`.

### Quick curl tests

```bash
# Register
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Saurav\",\"email\":\"saurav@test.com\",\"password\":\"password123\"}"

# Login
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"saurav@test.com\",\"password\":\"password123\"}"

# Protected route (paste token from login)
curl -s http://localhost:8080/api/tasks?userId=1 \
  -H "Authorization: Bearer <token>"
```

`.http` files for the REST Client extension are in `api-tests/`.

## Security

- Passwords hashed with BCrypt
- JWT signed with HS256, 24h expiry
- Stateless — no sessions
- `/api/auth/**` is public; all other routes require a valid token

## Future Improvements

### Auth & Security
- JWT refresh tokens — current tokens expire after 24h with no way to renew without re-login
- Password reset flow — email-based reset link
- Rate limiting on `/api/auth/**` — prevent brute force attacks
- Switch `npm ci` back in Dockerfile once `package-lock.json` is synced locally

### Task Features
- Due dates — add `dueDate` field to `Task`, show overdue indicator in UI
- Task priority — LOW / MEDIUM / HIGH enum, sortable in dashboard
- Task ordering — drag-and-drop reorder within a status column
- Pagination — `GET /api/tasks` currently returns all tasks; add `page`/`size` params
- Task search and filter by title

### Stats
- Completion trend over time — tasks completed per day/week using a line chart
- Productivity score — percentage of tasks moved to DONE
- Date range filter for stats

### Frontend
- Toast notifications — replace silent failures with visible success/error toasts
- Loading skeletons — better UX than plain "Loading…" text
- Dark mode — the black/white theme is already halfway there
- Mobile responsiveness — layout breaks on small screens currently
- Inline form validation feedback (not just on submit)

### Backend
- Task tags/labels — many-to-many relationship
- Soft delete — mark tasks as deleted instead of hard delete, allows undo
- Audit log — track who changed what and when
- Input sanitization — strip HTML from title/description fields
- Proper pagination with `Page<TaskResponse>` from Spring Data

### Infrastructure
- Environment-specific configs — `application-dev.yaml`, `application-prod.yaml`
- Health check endpoint — `/actuator/health` via Spring Boot Actuator
- Nginx SPA fallback — direct URL access (e.g. `/dashboard`) currently returns 404
- CI/CD pipeline — GitHub Actions to build and test on every push
- Production secrets management — move away from plain env vars

## Changelog

### feat: complete fullstack MVP — views, task store, CORS fix
- Implemented `RegisterView` — name/email/password form, error handling, redirects to dashboard
- Implemented `DashboardView` — full task CRUD (create, edit, delete), status filter tabs, loading/empty states
- Implemented `StatsView` — summary stat cards + Chart.js bar chart via `vue-chartjs`
- Created `AppLayout` component — header with nav links, user name, logout button
- Created `src/stores/task.ts` — Pinia store for task CRUD (fetchTasks, createTask, updateTask, deleteTask)
- Updated `src/stores/auth.ts` — now stores `id` from auth response for use in API calls
- Updated `AuthResponse` to include `id` field; `AuthService` passes user ID in both register and login
- Rewrote `TaskController` + `TaskService` — uses `@AuthenticationPrincipal` instead of `?userId` query param; proper DTOs (`TaskRequest`, `TaskResponse`); ownership check on update/delete
- Updated `StatsController` — uses `@AuthenticationPrincipal` instead of query param
- Added `TaskRequest` and `TaskResponse` DTOs under `task/dto/`
- Fixed CORS — `WebConfig` now allows all local origins; `SecurityConfig` enables `.cors()` so preflight passes Spring Security
- Added `vue-chartjs@5.3.2` to `package.json`; switched Dockerfile from `npm ci` to `npm install`
- Fixed TypeScript strict null error in `StatsView`

### feat: frontend routing, auth store, axios client
- Replaced default Vite scaffold in `App.vue` with `<RouterView />`
- Created `src/router/index.ts` with routes for `/login`, `/register`, `/dashboard`, `/stats`
- Protected `/dashboard` and `/stats` with `meta: { requiresAuth: true }` and `beforeEach` guard
- Unauthenticated users are redirected to `/login` automatically
- Created `src/stores/auth.ts` — Pinia store with `login()`, `register()`, `logout()` actions, token + user persisted to `localStorage`, `isLoggedIn` computed
- Created `src/api/axios.ts` — single Axios instance with request interceptor (attaches JWT) and response interceptor (401 → logout + redirect)
- Added placeholder views: `LoginView`, `RegisterView`, `DashboardView`, `StatsView`

### feat: auth module + JWT security
- Implemented `AuthService` — register (BCrypt hash) and login (AuthenticationManager)
- Implemented `AuthController` — `POST /api/auth/register` (201) and `POST /api/auth/login` (200)
- Created `JwtUtil` — `generateToken`, `validateToken`, `extractUsername` using JJWT 0.12.6 HS256
- Created `JwtAuthFilter` extending `OncePerRequestFilter` — reads Bearer token, sets SecurityContext
- Created `SecurityConfig` — stateless, CSRF disabled, registers filter and `AuthenticationManager`
- Created `UserDetailsServiceImpl` — loads user by email for Spring Security
- `User` entity now implements `UserDetails`
- Added `GlobalExceptionHandler` with handlers for validation, bad credentials, illegal args, and generic errors with logging
- Added `api-tests/auth.http` and `api-tests/tasks.http` for REST Client extension
- Enabled debug logging for security and web layers in `application.yaml`

### docs: fill out README with stack, setup, and env var docs

### initial commit
- Scaffolded Vue 3 + TypeScript frontend with Vite, Vue Router, Pinia, Axios, and Chart.js
- Scaffolded Spring Boot 3.5 backend with JPA, Security, Validation, and PostgreSQL driver
- Configured `application.yaml` to read datasource settings from environment variables
- Added Docker Compose setup with `postgres`, `backend`, and `frontend` services on a shared network
