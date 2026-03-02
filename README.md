#  Personal Book Management API

A Spring Boot REST API that integrates with the Google Books API and provides book management capabilities.

Designed with clean architecture principles, layered design, integration testing.

---

##  Features

-  Search books from Google Books API
-  Add books to local database
-  RESTful API design
-  Integration tests using `@SpringBootTest`
-  Clean service-layer separation
-  External API integration using Spring `RestClient`
-  JPA + Hibernate persistence
-  Global exception handling
-  Java 17 Record-based DTOs
-  Comprehensive JavaDoc documentation across controllers, services, entities, and tests
---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Hibernate
- H2
- Maven
- JUnit 5

---

#  Architecture

### Layer Responsibilities

- **Controller** → REST API layer
- **Service** → Business logic
- **Repository** → Persistence layer
- **GoogleBookService** → External API integration

---

# Configuration

Update `application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:h2:mem:bookdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

google.books.base-url=https://www.googleapis.com/books/v1
```


---

# Build and Run

- git clone https://github.com/{your-username>}/{repo-name}.git
- cd {repo-name}
- mvn spring-boot:run
- Application runs at - http://localhost:8080

# Test case

- mvn clean test