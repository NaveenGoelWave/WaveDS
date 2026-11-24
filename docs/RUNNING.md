# Running WaveDS locally

## 1. Database (PostgreSQL in Docker)

```bash
docker compose up -d        # from the repo root; needs the .env file
docker compose ps           # waveds-db should be "healthy"
```

## 2. Backend (Spring Boot, port 8080)

Requires Java 17+ and Maven (`brew install maven`), or open `backend/` in IntelliJ as a Maven project.

```bash
cd backend
mvn test                    # unit + controller tests (no database needed)
mvn spring-boot:run         # applies Flyway migrations, then starts the API
```

- API docs: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

Database settings default to the values in `.env` / `docker-compose.yml`.
Override with the `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables.

## 3. Frontend (React + Vite, port 5173)

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Requests to `/api` are proxied to the backend.

## Sign up, login and JWT

Every `/api/v1/**` endpoint except `POST /api/v1/auth/signup` and `POST /api/v1/auth/login` needs
an `Authorization: Bearer <token>` header. The frontend adds it automatically after you sign in.

- **First account** created becomes `ADMIN`; every later sign-up is a `USER`.
- Tokens last 60 minutes (`JWT_EXPIRATION_MINUTES`). After that the app sends you back to the login page.
- Passwords: 8 to 72 characters with at least one letter and one number, stored as BCrypt hashes.

Settings (environment variables):

| Variable | Default | Purpose |
|---|---|---|
| `JWT_SECRET` | dev-only value in `application.yml` | Signing key, 32+ characters. **Must be set for any shared or production environment.** |
| `JWT_EXPIRATION_MINUTES` | `60` | Token lifetime |
| `SIGNUP_ENABLED` | `true` | Set `false` to stop new sign-ups once your team has accounts |

Try the API from a terminal:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"you@example.com","password":"YourPassword1"}' | sed 's/.*"token":"\([^"]*\)".*/\1/')
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/customers
```

## Adding dropdown values (Customer Type, Company)

These endpoints now need a token as well. Add `-H "Authorization: Bearer $TOKEN"` to the command below.

These live in the database, not in code:

```bash
curl -X POST http://localhost:8080/api/v1/lookups \
  -H "Content-Type: application/json" \
  -d '{"category":"COMPANY","label":"Your Company Name"}'
```

Categories: `CUSTOMER_TYPE`, `COMPANY`. The seed migration `V2__seed_lookup_values.sql`
contains placeholder companies ("Sample Company A/B") that you should replace.
Zone, Site Type and Brand are fixed lists defined as enums in
`backend/src/main/java/com/wave/ds/customer/entity/`.
