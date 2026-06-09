# Jokeur — Backend

![CI Spring Boot](https://github.com/Djymini/jokeur_backend/actions/workflows/ci-spring.yml/badge.svg)

REST API for the Jokeur project, a web application for pet health tracking. Built as a team of 4 as part of a CDA certification (Application Designer Developer) at GRETA.

🌐 [jokeur.ashleydev.fr](https://jokeur.ashleydev.fr)

---

## Tech Stack

| Category | Tool |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT |
| Persistence | JPA + Hibernate + MySQL |
| Excel Export | Apache POI 5.3 |
| PDF Export | Flying Saucer + Thymeleaf |
| API Documentation | springdoc-openapi + Swagger UI |
| Build | Maven |
| Code Quality | Checkstyle + Husky |

## Features

- Stateless JWT authentication (sign up, login, forgot password via Brevo)
- Pet health record management (full CRUD)
- Vital signs tracking: weight, heart rate, respiratory rate, temperature
- Vaccines, treatments and symptoms management
- Vet appointment calendar
- Reminder system for vaccines and treatments
- Data export in PDF (Flying Saucer + Thymeleaf) and Excel (Apache POI)
- Fine-grained data protection: each user can only access their own data

## Requirements

- Java 21
- Maven
- MySQL (or Docker to run the database locally)

## Getting Started

```bash
git clone https://github.com/Djymini/jokeur_backend.git
cd jokeur_backend
cp .env.sample .env
# Fill in the variables in .env
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Environment Variables

Copy `.env.sample` to `.env` and fill in the values:

| Variable | Description |
|---|---|
| `DB_HOST_DEV` | Database host |
| `DB_PORT_DEV` | Database port |
| `DB_NAME_DEV` | Database name |
| `DB_USER_DEV` | MySQL user |
| `DB_PASSWORD_DEV` | MySQL password |
| `CLIENT_URL_DEV` | Frontend URL (for CORS) |
| `JWT_SECRET` | Secret key used to sign JWT tokens |
| `JWT_EXPIRATION_MS` | Token validity duration in milliseconds |
| `BREVO_API_KEY` | Brevo API key for sending emails |
| `APP_UPLOAD_DIR` | Local directory for uploaded files |

## Running Tests

```bash
# Unit tests
mvn test -Punit-tests

# Integration tests (in-memory H2 profile)
mvn test -Pintegration-tests

# E2E tests (TestContainers, requires Docker)
mvn test -Pe2e-tests
```

## API Documentation

Swagger documentation is automatically generated on startup and available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Contributors

Project built as a team of 4 with Mawele, Yolain Sarah and Ashley as part of the CDA certification - GRETA.