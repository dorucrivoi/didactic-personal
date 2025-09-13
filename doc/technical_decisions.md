⚙️ **Technical Decisions**

Spring Boot → fast microservice development.
Spring Data JPA → repository abstraction (fits DDD Repositories).
Flyway → DB versioning → ensures domain model consistency across environments.
Springdoc + Swagger → explicit API contracts 
RabbitMQ → async integration across bounded contexts.
Config Server → centralized configuration = single source of truth.

**Microservices structure**
The system is designed as a set of microservices, where each service has a single responsibility aligned with its bounded context.
Please see, the diagram directory for more details.

_Didactic-Personal:_ This service handles the core domain of academic structure, specifically the management of professors and classes.
Its purpose is to ensure that academic resources (professors, class codes, and class-year assignments) are created, validated, and maintained consistently.
For example, it enforces business rules such as avoiding duplicate professors, ensuring that a class code is unique, and validating assignments of professors
to classes. This isolates the complexity of teaching staff and class structures, preventing duplication and inconsistency across the system.

Actors: Administrator (create/update/delete professors and classes), Professors (view their assigned classes), Students (view professors linked to their class/year).
Technical role: Acts as the authority for academic personnel and class metadata, exposing APIs via OpenAPI contracts and integrating with RabbitMQ 
for publishing domain events (e.g., SchoolClassCreatedEvent).

_Catalogue_: This service focuses on the student-facing domain, primarily dealing with students and their grades. 
By separating it from Didactic-Personal, the catalogue service can evolve independently while consuming authoritative data about professors and classes 
via events or APIs. Its role is to maintain student-specific records (like grades, semesters, and performance) without duplicating 
the logic of class/professor management.

Actors: Students (view their grades), Professors (submit or update grades), Administrators (oversee and validate student academic performance).
Technical role: Maintains a consistent record of student assessments, integrates with Didactic-Personal for context (professor and class metadata), 
and ensures referential integrity through event-driven communication.

By designing the system this way, we achieve:

Loose coupling between domains (professor/class vs. student/grades).
High cohesion inside each service (each microservice owns its bounded context).
Scalability: each service can be scaled independently depending on load (e.g., student queries in Catalogue may be higher than professor updates 
in Didactic-Personal).
Clarity of responsibility: Didactic-Personal is the source of truth for professors and classes, while Catalogue is the source of truth for students
and grades.

**Project structure**
Based on the identified use cases and actors, the package structure was designed in alignment with Domain-Driven Design (DDD) principles. 
This structure ensures a clear separation of concerns by decoupling the domain model from application services, preventing the model 
from being misused across different layers of the application. For each actor, dedicated application services are implemented to orchestrate
and coordinate the execution of the corresponding use cases, thereby maintaining both modularity and adherence to the business domain.

- _administration_
    - application layer  from DDD
    - Coordinates use cases that actors (Admin, Professor, Student) can perform. It doesn’t hold core business rules — only orchestrates.
    - Keeps domain rules decoupled from HTTP, JSON, or OpenAPI specifics.
       # controller → REST entrypoints (ProfessorController, SchoolClassController). They expose use cases via OpenAPI-defined endpoints.
       # service → Application services (ManageProfessor, ManageSchoolClass). These orchestrate repositories + domain services to execute workflows.

- _audit_
    - Auditing is not part of the core domain, but it’s a cross-cutting concern required for compliance and traceability.
    - Doesn’t pollute domain entities with audit details; keeps it isolated but available when needed.
- _common_
    - provides utilities, exceptions, and error-handling logic that multiple layers need.
    - promotes consistency across the whole bounded context (all errors look and behave the same).
- _config_
    - represents and defined the configurations of infrastucture used in the application
        - keeps framework details out of the domain. The model layer doesn’t care if messaging is RabbitMQ or Kafka, or if persistence
          is H2 or PostgreSQL.
- _messaging_
    - Enables asynchronous communication between bounded contexts.
    - Implements loose coupling → Didactic-Personal can notify others without knowing who consumes the event.
- _model_
    - Contains the business rules and domain model. Keeps your business rules independent of controllers, messaging, or databases.
    - Domain Service ensures global business rules (e.g., class code must be unique across system).
    - It is independent of technology — you could switch from REST to gRPC or from H2 to PostgreSQL, and the model doesn’t care.
  

**API**
For the management of the APIs, the OpenAPI framework was adopted. This approach provides a single point of definition through the didactic-personal.yaml 
specification, from which the corresponding interfaces are generated and subsequently integrated with the controllers.
The implementation of each API strictly follows the defined use cases and is aligned with the responsibilities of the corresponding actors.
Specifically, APIs under the /admin path are exposed exclusively to administrators, those under the /professors path are dedicated to professors, 
and the /students path is reserved for students.
The use of OpenAPI was chosen because it ensures consistency across services, enables automatic generation of interfaces and documentation, 
improves communication between developers and stakeholders by providing a clear and human-readable API contract.

**How SOLID Principles Are Applied**

_Single Responsibility_: Each package/class has a clear purpose (controller only exposes API, service runs workflows, 
                       model holds state/behavior).
_Open/Closed_: Adding a new actor (e.g., "Parent") would not break current classes, just extend controllers/services.
_Liskov Substitution_: Interfaces (repositories, services) can be replaced by mocks in tests.
_Interface Segregation_: Classes are kept small and focused, avoiding large all-in-one interfaces.
_Dependency Inversion_: High-level modules (controllers) depend on abstractions (services, repositories), not on JPA or 
                      RabbitMQ directly.


