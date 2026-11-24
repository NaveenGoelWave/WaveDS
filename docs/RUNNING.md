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

## Adding dropdown values (Customer Type, Company)

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
