# WaveDS

A customer management (CRM-style) web application. Sign in, add customers through an induction form, search and filter them, and edit or remove them.

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3, Spring Security (JWT), Spring Data JPA, Flyway |
| Frontend | React 18, TypeScript, Vite, React Router, TanStack Query, React Hook Form + Zod |
| Database | PostgreSQL 16 (runs in Docker) |

## Prerequisites

Install these once on your machine:

| Tool | Version | Check with |
|---|---|---|
| Git | any recent | `git --version` |
| Java (JDK) | 17 or newer | `java -version` |
| Maven | 3.9 or newer | `mvn -v` |
| Node.js | 20 or newer | `node -v` |
| Docker Desktop | any recent | `docker --version` |

Docker Desktop must be **running** (whale icon in the menu bar) before you start the database.

## Quick start

You need three terminal windows: one each for the database, backend and frontend.

### 1. Get the code

```bash
git clone git@github.com:NaveenGoelWave/WaveDS.git
cd WaveDS
```

(Use `https://github.com/NaveenGoelWave/WaveDS.git` if you have not set up an SSH key.)

### 2. Create your local settings file

The database password lives in a `.env` file that is **not** committed to Git. Create it in the project root:

```bash
cat > .env <<'EOF'
POSTGRES_DB=wave_ds
POSTGRES_USER=wave_user
POSTGRES_PASSWORD=ChangeMe_Local123
EOF
```

This password is for local development only. If you choose a different one, also set `DB_PASSWORD` to the same value when starting the backend (see [Configuration](#configuration)).

### 3. Start the database

```bash
docker compose up -d
docker compose ps
```

Wait until `waveds-db` shows `healthy`. The database is published on **port 5433** of your machine (not the usual 5432, so it does not clash with any Postgres you may already have installed). Data is kept in a Docker volume, so it survives restarts.

### 4. Start the backend (port 8080)

```bash
cd backend
mvn spring-boot:run
```

The first run downloads dependencies and takes a few minutes. Wait for `Started WaveDsApplication`. On startup, Flyway creates or updates the database tables automatically.

### 5. Start the frontend (port 5173)

In a new terminal:

```bash
cd frontend
npm install
npm run dev
```

### 6. Open the app

Go to **http://localhost:5173**. The first time, click **Create an account**. **The first account created becomes the administrator**; later sign-ups are regular users.

## Everyday commands

| Task | Command |
|---|---|
| Start the database | `docker compose up -d` (from the project root) |
| Stop the database (keeps data) | `docker compose stop` |
| Delete the database and all its data | `docker compose down -v` |
| Run backend tests | `cd backend && mvn test` |
| Build the frontend for production | `cd frontend && npm run build` |
| Backend API docs (Swagger) | http://localhost:8080/swagger-ui.html |
| Backend health check | http://localhost:8080/actuator/health |

## Configuration

The backend reads these environment variables. The defaults work for the Docker setup above.

| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/wave_ds` | Database connection URL |
| `DB_USERNAME` | `wave_user` | Database user |
| `DB_PASSWORD` | `ChangeMe_Local123` | Database password |
| `JWT_SECRET` | development-only value | Key used to sign login tokens (32+ characters) |
| `JWT_EXPIRATION_MINUTES` | `60` | How long a login lasts |
| `SIGNUP_ENABLED` | `true` | Set to `false` to stop new accounts being created |

**Before any shared or production deployment**, set a strong `JWT_SECRET` and database password, and consider `SIGNUP_ENABLED=false` once your team has accounts. Example:

```bash
JWT_SECRET='a-long-random-string-of-at-least-32-characters' mvn spring-boot:run
```

## Project structure

```
WaveDS/
├── backend/                         Spring Boot API
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/wave/ds/
│       │   │   ├── auth/            sign-up, login, JWT
│       │   │   ├── customer/        customer API (controller, service, repository, entity, dto, mapper)
│       │   │   ├── lookup/          dropdown values stored in the database
│       │   │   └── common/          security config, error handling, shared code
│       │   └── resources/
│       │       └── db/migration/    Flyway SQL migrations (V1, V2, ...)
│       └── test/                    backend tests
├── frontend/                        React app
│   └── src/
│       └── features/                auth/ and customers/ screens, API calls and forms
├── docs/                            extra notes
├── docker-compose.yml               PostgreSQL for local development
└── .env                             local secrets (you create this; never committed)
```

## Managing dropdown values

Zone, Site Type and Brand are fixed lists defined in the backend code (`backend/src/main/java/com/wave/ds/customer/entity/`).
Customer Type and Company are stored in the database. To add a company, sign in, get your token, and call:

```bash
curl -X POST http://localhost:8080/api/v1/lookups \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{"category":"COMPANY","label":"Your Company Name"}'
```

Valid categories are `CUSTOMER_TYPE` and `COMPANY`. See `docs/RUNNING.md` for how to obtain a token from the terminal.

## Troubleshooting

| Problem | Likely cause and fix |
|---|---|
| `Cannot connect to the Docker daemon` / `docker.sock` error | Docker Desktop is not running. Run `open -a Docker` (macOS), wait a minute, try again. |
| `role "wave_user" does not exist` | Your app is talking to a different Postgres. Check `lsof -nP -iTCP:5433 -sTCP:LISTEN` and make sure only Docker is listening there. |
| `Port 8080 was already in use` | An old backend is still running. Find it with `lsof -nP -iTCP:8080 -sTCP:LISTEN` and stop it with `kill <PID>`. |
| `Port 5433 is already allocated` | Another program uses 5433. Change the left number in `docker-compose.yml` (`"5433:5432"`) and set `DB_URL` to match. |
| Browser says "This site can't be reached" on port 5173 | The frontend is not running. Start it with `npm run dev` in `frontend/`. |
| Page says "Could not load form options" | The backend is not running or not reachable on port 8080. |
| Signed out unexpectedly | Logins expire after 60 minutes. Sign in again. |
| `Password authentication failed` | `.env` password differs from `DB_PASSWORD`. If you changed `.env` after the database was first created, run `docker compose down -v` once to recreate it (this deletes the data). |

## Contributing

Work on a branch, keep commits small, and run `mvn test` and `npm run build` before pushing.
