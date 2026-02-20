# 🚀 Product Management REST API

A fully robust, scalable, and secure Spring Boot REST API for managing Products and Items. Designed with Clean Architecture principles, Role-Based Access Control (RBAC) via JWTs, and Paginated data structures.

---

## 🛠 Tech Stack & Architecture

- **Java 17+**
- **Spring Boot 3.2.2** (Core Framework)
- **Spring Data JPA & Hibernate** (ORM & Database Abstraction)
- **MySQL 8.0** (Production/Main Database via Docker)
- **H2 In-Memory Database** (Isolated Integration Testing)
- **Spring Security + JJWT** (Stateless Bearer Token Authentication)
- **JUnit 5 + Mockito + MockMvc** (Unit and Integration Testing)
- **Swagger / OpenAPI 3.0** (API Documentation & Testing Interface)
- **Lombok** (Boilerplate Reduction)
- **Docker & Docker Compose** (Containerization)

### Architectural Decisions
- **Stateless JWT Design:** The application uses no session cookies. Both `User` and `Admin` roles are authenticated exclusively through asymmetric cryptographically signed JWT tokens generated via `/api/v1/auth/register` and `/api/v1/auth/authenticate`.
- **Entity Relationships:** Employs a `OneToMany` relationship mapping between `Product` and `Item` entities utilizing cascading saves for streamlined persistency.
- **Pagination:** Uses Spring Data `Pageable` parameters to strictly control data chunking on `GET /products` limiting memory load on large queries.
- **Exception Handling:** Centralized `@RestControllerAdvice` provides clean, standardized `JSON` error payloads for unhandled exceptions (e.g., `ProductNotFoundException`).

---

## 🚀 How to Run the Application

This project is fully dockerized. You do **NOT** need MySQL or Java installed locally to run it, merely Docker Desktop.

### Option 1: Run via Docker Compose (Recommended)
This method spins up a MySQL v8.0 container on port `3307` and executes your API via a Spring Boot instance completely automatically.

1. Ensure **Docker Desktop** is open and running.
2. Open a terminal in the root project directory (`product-api`).
3. Run the following command:
   ```bash
   docker-compose up --build -d
   ```
4. The API is now live! Access the Swagger interface here:
   👉 **http://localhost:8080/swagger-ui.html**

### Option 2: Run Locally (Database strictly in Docker)
Use this option if you want the database containerized, but wish to run the Java code locally via an IDE for live debugging.

1. Start exactly just the MySQL database container:
   ```bash
   docker-compose up mysql -d
   ```
2. Inject the code execution via Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔒 Authentication Flow (How to test the API)

Because the API endpoints are meticulously secured, you **must** generate a Bearer Token before fetching or creating products.

1. Open **[Swagger UI](http://localhost:8080/swagger-ui.html)**
2. Scroll to `auth-controller` -> **`POST /api/v1/auth/register`**.
3. Create a test admin user by executing this payload:
   ```json
   {
     "email": "admin@example.com",
     "password": "securepassword",
     "role": "ADMIN"
   }
   ```
4. Copy the resulting `"accessToken"` string.
5. Scroll to the top of Swagger UI, click the green **`Authorize`** padlock button.
6. Paste your token (no need to type "Bearer", just the token string itself) and click Authenticate.
7. You are now authorized to use `GET`, `POST`, `PUT`, and `DELETE` product endpoints!

---

## 🧪 Testing

The application maintains a highly isolated testing suite ensuring MySQL is never accidentally polluted during pipeline deployments. It defaults to the `application-test.properties` which auto-generates a rapid H2 In-Memory instance.

To run the robust suite of Unit and Integration Tests:
```bash
./mvnw clean test
```

---

## 📄 swagger / cURL Payload Examples

### 📌 Create a Product (POST `/api/v1/products`)
```json
{
  "productName": "Awesome Laptop",
  "createdBy": "Admin_User",
  "items": [
    {
      "quantity": 10
    },
    {
      "quantity": 5
    }
  ]
}
```

### 📌 View Paginated Products (GET `/api/v1/products`)
*Automatically fetches items alongside product metadata.*
```http
GET http://localhost:8080/api/v1/products?page=0&size=5&sort=id,desc
```