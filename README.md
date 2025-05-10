# Package Service – SmartLockerHub

The **Package Service** is the central component of the **SmartLockerHub** ecosystem, handling all operations related to package creation, tracking, and delivery workflows. It interacts with both the Locker Service and User Service using Apache Kafka for seamless event-driven communication.

---

## 🔧 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Postgre SQL**
- **Apache Kafka**
- **JWT**
- **Maven**
- **Spring Web**

---

## 📦 Features

- Create and persist package entities with status and metadata.
- Automatically generate alphanumeric access codes.
- Send Kafka event (`package-placed`) after successful creation.
- Central logic for initiating package delivery and locker assignment.

---

## ✅ Endpoints

| Method | Endpoint                        | Description                       |
|--------|---------------------------------|-----------------------------------|
| POST   | `/packages/create-package`      | Create and deliver a new package  |
| POST   | `/packages/receive-package`     | Correct package is received       |
| GET    | `/packages/view-packages`       | List last 10 customer packages    |

---

## 📥 Kafka Integration

- **Producer Topic:** `package-placed`  
  Triggered after a package is created and assigned to a locker unit.

- **Producer Topic:** `package-received`  
  Triggered after a package is received and locker unit to be freed.
