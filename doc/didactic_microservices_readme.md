# 🎓 Didactic Personal & Catalogue Microservices

This project is a **Spring Boot microservices system** for managing classes, professors, catalogues, students, disciplines, and grades.

It includes:

- _Didactic-Personal Service_ (`8080`) → manages professors and classes  
- _Catalogue Service_ (`8081`) → manages students, grades, and disciplines  
- _Config Server_ (`8888`) → provides configuration from a Git repository  
- _RabbitMQ_ (via Docker) → asynchronous communication between services  
- _H2 Database_ → embedded storage for easy testing  
- _PlantUML Plugin in IntelliJ_ → visualize diagrams from `.puml` files  

---

## ⚙️ Prerequisites

To run the applications, the following tools must be installed:

- **Java 17+**  
  [Download here](https://www.oracle.com/java/technologies/downloads/?utm_source=chatgpt.com#jdk24-windows)

- **Docker Desktop for Windows**  
  [Installation guide](https://docs.docker.com/desktop/setup/install/windows-install/)

- **IntelliJ IDEA**  
  [Download here](https://www.jetbrains.com/idea/download/?section=windows)

- **PlantUML integration**  
  Install plugin in IntelliJ: `Settings -> Plugins -> find and select plantuml4idea`

- **Postman**  
  [Installation guide](https://learning.postman.com/docs/getting-started/installation/installation-and-updates/)

---

## **Logging Configuration**

This project uses **Log4j2** for logging. Logging is configured to work safely on **Windows, macOS, and Linux**, with both console output and rolling file support.

- **Features**

  - **Console Logging**  
    Logs are printed to the console for easy monitoring during development.

  - **Rolling File Logging**  
    Logs are written to a file `logs/application.log` and automatically rotated:
    - Rotates daily or when the file exceeds 10 MB  
    - Keeps the last 10 archived logs; old logs are deleted automatically  
    - Archived logs are stored as `logs/archive/application-YYYY-MM-DD-HH-MM.log.gz`

  - **Cross-Platform Compatible**  
    - File paths use forward slashes (`/`) to work on Windows, macOS, and Linux  
    - Uses `RollingRandomAccessFile` appender to prevent file locking issues on Windows  

  - **Hibernate SQL Logging**  
    - SQL statements executed by JPA/Hibernate are logged  
    - Parameter values are logged separately for debugging  
    - Example output:

    ```text
    [2025-09-13 12:34:56] [DEBUG] SchoolRepository - insert
    into school_class (class_code, class_year, name, id) values (?, ?, ?, default)
    ```

- **File Locations**
  - Active log file: `logs/application.log`  
  - Archived log files: `logs/archive/application-YYYY-MM-DD-HH-MM.log.gz`

---

## 📦 Project Repositories

Clone the repositories:

```bash
git clone https://github.com/dorucrivoi/didactic-personal.git
git clone https://github.com/dorucrivoi/catalogue.git
git clone https://github.com/dorucrivoi/didactic-config-server.git
```

---

## 🐳 Setup RabbitMQ with Docker

RabbitMQ must be running before starting the microservices. Run:

```bash
# latest RabbitMQ 4.x
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
```

- Management Console → [http://localhost:15672](http://localhost:15672)  
- Username: `guest`  
- Password: `guest`  

---

## 🗂️ Config Server

Start the Config Server:

```bash
cd didactic-config-server
./gradlew clean build
./gradlew bootRun
```

- Starts on port `8888`  
- Serves configuration from Git: [https://github.com/dorucrivoi/didactic-config-repo](https://github.com/dorucrivoi/didactic-config-repo)  
- Searches subdirectories: `catalogue` and `didactic-personal`  
- Uses the `main` branch by default  

Test the server URLs:

- [http://localhost:8888/actuator/health](http://localhost:8888/actuator/health)  
- [http://localhost:8888/catalogue/default/main](http://localhost:8888/catalogue/default/main)  
- [http://localhost:8888/didactic-personal/default](http://localhost:8888/didactic-personal/default)  

---

## 🚀 Running the Microservices

_Build all projects from each root folder:_

```bash
./gradlew clean build
```

**Run Didactic Personal:**

```bash
cd didactic-personal
./gradlew bootRun
```

- Runs on port `8080`

**Run Catalogue:**

```bash
cd catalogue
./gradlew bootRun
```

- Runs on port `8081`

---

## 📖 API Documentation (Swagger UI)

Access Swagger UI once the applications are running:

- [Catalogue Service](http://localhost:8081/swagger-ui/index.html#/)  
- [Didactic Personal Service](http://localhost:8080/swagger-ui/index.html#/)  

---

## 📂 Preloaded Test Data

Both microservices use a `data.sql` file to insert test data into H2 automatically.

---

## 🗃️ Database (H2)

Embedded H2 database for each service:

**H2 Console:**

- Didactic Personal → [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
- Catalogue → [http://localhost:8081/h2-console](http://localhost:8081/h2-console)  

**Default credentials:**

- JDBC URL: `jdbc:h2:mem:testdb`  
- User: `sa`  
- Password: *(empty)*  

---

## 🔔 Example Workflows

1. **Create a Class (Async with RabbitMQ)**

```http
POST http://localhost:8080/api/admin/classes
Content-Type: application/json

{
  "name": "Class 9C",
  "classCode": "9C",
  "classYear": 2025
}
```

* Sends event → RabbitMQ
* Catalogue consumes → creates catalogue + semesters

Verify the catalogue:

```http
GET http://localhost:8081/api/admin/catalogue/9C?year=2025
```

---

2. **Remove a Class**

```http
DELETE http://localhost:8080/api/admin/classes/{id}
```

* Sends event → RabbitMQ
* Catalogue consumes → deletes catalogue + semesters

---

3. **Student Requests Professors (Sync Call)**

```http
GET http://localhost:8081/api/students/{studentCode}/professors?year=2025
```

Example:

```http
GET http://localhost:8081/api/students/S002/professors?year=2025
```

* Catalogue → REST call to Didactic Personal
* Returns professors list

---

4. **Students Requests**

```http
GET http://localhost:8081/api/admin/students
```

* Catalogue → REST call
* Returns all students

---

5. **Student Grades**

```http
GET http://localhost:8081/api/students/{studentCode}/grades
```

Example:

```http
GET http://localhost:8081/api/students/S002/grades
```

* Catalogue → REST call
* Returns grades list for the student


In the `doc` directory, a **Postman collection** is provided to test the application.

