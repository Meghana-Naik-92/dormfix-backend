# DormFix — Backend

> REST API for DormFix Hostel Issue Reporter  
> Built with Java 21 + Spring Boot 3.5 + MySQL

🔗 Frontend Repository: [dormfix-frontend](https://github.com/Meghana-Naik-92/dormfix-frontend)

---

## What is DormFix?

DormFix is a complaint management system for college hostels. Students report maintenance issues and wardens manage and resolve them. This repository contains the complete backend REST API.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Core language |
| Spring Boot 3.5 | Backend framework |
| Spring Security | Auth & authorization |
| JWT (jjwt 0.11.5) | Stateless token auth |
| Spring Data JPA | ORM layer |
| Hibernate | JPA implementation |
| MySQL 8.0 | Database |
| BCrypt | Password hashing |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## Project Structure

```
src/main/java/com/dormfix/
├── config/
│   ├── SecurityConfig.java       ← Spring Security + JWT setup
│   ├── JwtFilter.java            ← Intercepts every HTTP request
│   └── WebConfig.java            ← CORS configuration
├── controller/
│   ├── AuthController.java       ← /auth/register, /auth/login
│   ├── ComplaintController.java  ← Student complaint endpoints
│   └── AdminController.java      ← Admin management endpoints
├── entity/
│   ├── User.java                 ← users table
│   ├── Complaint.java            ← complaints table
│   ├── Role.java                 ← STUDENT / ADMIN
│   └── ComplaintStatus.java      ← PENDING / IN_PROGRESS / RESOLVED
├── repository/
│   ├── UserRepository.java
│   └── ComplaintRepository.java
├── service/
│   ├── AuthService.java          ← Register + login logic
│   ├── UserService.java          ← UserDetailsService impl
│   ├── ComplaintService.java     ← Student complaint logic
│   └── AdminService.java         ← Admin logic
├── dto/
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── ComplaintRequest.java
│   ├── ComplaintResponse.java
│   ├── StatsResponse.java
│   └── ApiResponse.java
├── util/
│   └── JwtUtil.java              ← Token generate + validate
└── exception/
    └── GlobalExceptionHandler.java
```

---

## Database Design

### users
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| name | VARCHAR | Full name |
| email | VARCHAR | Unique |
| password | VARCHAR | BCrypt hashed |
| role | VARCHAR | STUDENT or ADMIN |
| hostel_block | VARCHAR | e.g. Block A |
| room_number | VARCHAR | e.g. 204 |

### complaints
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| title | VARCHAR | Brief title |
| description | TEXT | Full description |
| category | VARCHAR | Electrical, Plumbing etc. |
| status | VARCHAR | PENDING / IN_PROGRESS / RESOLVED |
| student_id | BIGINT | FK → users.id |
| created_at | DATETIME | Auto on insert |
| updated_at | DATETIME | Auto on update |

---

## API Endpoints

### Public
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register student or admin |
| POST | `/auth/login` | Login, receive JWT token |

### Student (JWT + STUDENT role required)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/complaints` | Submit complaint |
| GET | `/complaints/my` | Get my complaints |
| GET | `/complaints/{id}` | Get complaint detail |
| GET | `/complaints/stats` | Get my stats |

### Admin (JWT + ADMIN role required)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/complaints` | All complaints with filters |
| GET | `/admin/complaints/{id}` | Single complaint detail |
| GET | `/admin/stats` | Overall stats |
| PATCH | `/admin/complaints/{id}/status` | Update status |

### Filtering
```
GET /admin/complaints?status=PENDING
GET /admin/complaints?hostelBlock=Block A
GET /admin/complaints?status=PENDING&hostelBlock=Block A
```

---

## How Authentication Works

1. User registers or logs in → server returns a signed JWT token
2. Client sends token in every request: `Authorization: Bearer <token>`
3. `JwtFilter` intercepts every request, validates token, sets Spring Security context
4. Protected routes automatically reject invalid or missing tokens
5. Role-based rules: `/complaints/**` → STUDENT only, `/admin/**` → ADMIN only

---

## How to Run

### Prerequisites
- Java 21
- MySQL 8.0
- Maven

### Steps

1. Clone the repo
```bash
git clone https://github.com/Meghana-Naik-92/dormfix.git
cd dormfix
```

2. Create the database
```sql
CREATE DATABASE dormfix_db;
```

3. Update `src/main/resources/application.yml`
```yaml
spring:
  datasource:
    username: your_mysql_username
    password: your_mysql_password
```

4. Run
```bash
./mvnw spring-boot:run
```

Server starts at `http://localhost:8080`  
Tables are auto-created by JPA on first run.

---

## Test with Postman

### Register a student
```json
POST /auth/register
{
  "name": "Meghana Naik",
  "email": "meghana@college.edu",
  "password": "password123",
  "role": "STUDENT",
  "hostelBlock": "Block A",
  "roomNumber": "204"
}
```

### Register an admin
```json
POST /auth/register
{
  "name": "Admin Warden",
  "email": "admin@college.edu",
  "password": "admin123",
  "role": "ADMIN"
}
```

### Login
```json
POST /auth/login
{
  "email": "meghana@college.edu",
  "password": "password123"
}
```

Copy the token from the response and use it in Postman:  
`Authorization → Bearer Token → paste token`

---

## Key Implementation Details

- **Stateless API** — no sessions, every request carries a JWT
- **BCrypt hashing** — passwords never stored as plain text
- **DTO pattern** — entities never exposed directly, password can never leak
- **Ownership check** — students can only access their own complaints, verified server-side
- **Global exception handler** — all errors return consistent JSON structure
- **Spring Data JPA** — query methods auto-generate SQL from method names

---

## Author

**Meghana M Naik**  
Computer Science Engineering Student  
GitHub: [@Meghana-Naik-92](https://github.com/Meghana-Naik-92)  
LinkedIn: [Meghana Naik](https://www.linkedin.com/in/meghana-naik-832971324)
