# ✅ Todo Backend (Spring Boot + MySQL)

This is the **Spring Boot REST API** backend for the Todo application.  
It provides endpoints for user authentication and managing todo items.

The app connects to a **MySQL database** and is designed to be run using **Docker Compose**.

---

## 🚀 Features

- RESTful API for todos and authentication
- JWT-based authentication
- MySQL persistence
- Swagger API docs at `/swagger-ui`
- CORS-configured for frontend access
- Built with Spring Boot 3+

---

## 🐳 Running the App

### Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- JDK 17+ (only for local builds)

---

### 🧱 Step 1: Build the JAR

Before running Docker, package the application:

```bash
./mvnw clean package
```
✅ This creates the target/todo-backend-X.X.X.jar used by the Docker container.

---

### 🐳 Step 2: Run with Docker Compose
```bash
docker-compose up --build
```

This will:
- Build the backend Docker image
- Launch the Spring Boot app
- Launch a MySQL database
- Expose the API on http://localhost:8080

---

### 🌐 API Documentation
Once the app is running, you can access **Swagger UI** at:

```bash
http://localhost:8080/swagger-ui/index.html
```
Here you can explore and check all of the endpoints.

---

### ⚙️ Configuration

You can adjust database credentials and settings in `docker-compose.yml` and in `src/main/resources/application.yml`

---

### 🧪 Tech Stack
- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- MySQL 8
- Swagger / OpenAPI
- Docker + Docker Compose

---

### ✅ Notes
- Make sure `target/todo-backend-X.X.X.jar` exists before running Docker Compose
- The app will automatically connect to the MySQL container defined in docker-compose.yml