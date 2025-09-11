⚙️ Prerequisites
To run the applications it needs to have installed the following tools:
Java 17+ : https://www.oracle.com/java/technologies/downloads/?utm_source=chatgpt.com#jdk24-windows
Docker Desktop for Windows : https://docs.docker.com/desktop/setup/install/windows-install/
IntelliJ IDEA with: https://www.jetbrains.com/idea/download/?section=windows
PlantUML integration : install plugin in IntellJ : Settings->Plugins-> find and select plantuml4idea
Postman - https://learning.postman.com/docs/getting-started/installation/installation-and-updates/

# 🎓 Didactic Personal & Catalogue Microservices

This project is a **Spring Boot microservices system** for managing classes, professors,catalogues,
students, disciplines, and grades.

It includes:
- _Didactic-Personal Service_ (`8080`) → manages professors and classes
- _Catalogue Service_ (`8081`) → manages students, grades, and disciplines
- _Config Server_ → provides configuration from a Git repository
- _RabbitMQ_ (via Docker) → asynchronous communication between services
- _H2 Database_ → embedded storage for easy testing
- _PlantUML Plugin in IntelliJ_ → visualize diagrams from `.puml` files

## 📦 Project Repositories
Clone the repositories:
git clone https://github.com/dorucrivoi/didactic-personal.git
git clone https://github.com/dorucrivoi/catalogue.git
git clone https://github.com/dorucrivoi/didactic-config-server.git

**🐳 Setup RabbitMQ with Docker**
RabbitMQ must be running before starting the microservices.From command prompt run the command, after you have installed docker:
# latest RabbitMQ 4.x
**docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management**
Management Console → http://localhost:15672
Username: guest
Password: guest

🗂️ **Config Server**
Start the Config Server :

cd didactic-config-server
gradlew clean build
gradlew bootRun

🚀 **Running the Microservices**
_Build all projects_
From each root folder:

gradlew clean build

**Run** Didactic Personal
cd didactic-personal
gradlew bootRun
Runs on port 8080.

**Run** Catalogue
cd catalogue
gradlew bootRun
Runs on port 8081.

📖 **API Documentation (Swagger UI)**
This project provides interactive API documentation using Swagger UI.
Accessing Swagger UI, once the application is running, you can explore the REST API using Swagger UI:

👉 http://localhost:8081/swagger-ui/index.html#/
👉 http://localhost:8080/swagger-ui/index.html

📂 **Preloaded Test Data**
Both microservices use a data.sql file to automatically insert test data into the H2 database when the application starts.
🗃️ **Database (H2)**
Each service uses H2 embedded database.

_H2 Console:_
Didactic Personal → http://localhost:8080/h2-console
Catalogue → http://localhost:8081/h2-console
Default credentials:
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (empty)

**🔔 Example Workflows**
1. Create a Class (Async with RabbitMQ)
   POST http://localhost:8080/api/admin/classes
   Content-Type: application/json
{
"name": "Class 12C",
"classCode": "12C",
"classYear": 2025
}
_Sends event → RabbitMQ
Catalogue consumes → creates catalogue + semesters_

2. Remove a Class
   DELETE http://localhost:8080/api/admin/classes/{id}

Sends event → RabbitMQ
Catalogue consumes → deletes catalogue + semesters

3. Student Requests Professors (Sync Call)
   GET http://localhost:8081/api/students/{studentCode}/professors?year=2025

Catalogue → REST call to Didactic Personal
Returns professors list

In the doc directory I added  a collection created on postman to test the application. 

