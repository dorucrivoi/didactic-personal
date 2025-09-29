# ⚙️ Technical Decisions

- **Spring Boot** → fast microservice development
- **Spring Data JPA** → repository abstraction (fits DDD Repositories)
- **Flyway** → DB versioning → ensures domain model consistency across environments
- **Springdoc + Swagger** → explicit API contracts
- **RabbitMQ** → async integration across bounded contexts
- **Config Server** → centralized configuration = single source of truth

## Microservices Structure

The system is designed as a set of microservices, where each service has a single responsibility aligned with its bounded context.  
Please see the `diagram` directory for more details.

### Didactic-Personal
This service handles the core domain of academic structure, specifically the management of professors and classes. Its purpose is to ensure that academic resources (professors, class codes, and class-year assignments) are created, validated, and maintained consistently.

- **Business rules enforced**: avoid duplicate professors, ensure class code uniqueness, validate professor-class assignments.
- **Actors**:
    - Administrator → create/update/delete professors and classes
    - Professors → view assigned classes
    - Students → view professors linked to their class/year
- **Technical role**: authority for academic personnel and class metadata, exposing APIs via OpenAPI contracts and integrating with RabbitMQ for domain events (e.g., `SchoolClassCreatedEvent`)

### Catalogue
This service focuses on the student-facing domain, primarily dealing with students and their grades.

- **Actors**:
    - Students → view grades
    - Professors → submit or update grades
    - Administrators → oversee student performance
- **Technical role**: maintains student assessments, integrates with Didactic-Personal for professor/class metadata, ensures referential integrity through event-driven communication.

**Benefits of this design**:
- Loose coupling between domains (professor/class vs. student/grades)
- High cohesion inside each service (each microservice owns its bounded context)
- Scalability: services can be scaled independently based on load
- Clarity of responsibility: Didactic-Personal = professors/classes, Catalogue = students/grades

## Project Structure

Based on identified use cases and actors, the package structure follows **Domain-Driven Design (DDD)** principles:

- `_administration_`
    - Application layer from DDD
    - Coordinates use cases for actors (Admin, Professor, Student)
    - Keeps domain rules decoupled from HTTP, JSON, or OpenAPI specifics
    - Subfolders:
        - `controller` → REST entrypoints (e.g., `ProfessorController`, `SchoolClassController`)
        - `service` → Application services (e.g., `ManageProfessor`, `ManageSchoolClass`)

- `_audit_`
    - Auditing is cross-cutting, required for compliance and traceability
    - Isolated from core domain

- `_common_`
    - Utilities, exceptions, and error-handling logic
    - Promotes consistency across the bounded context

- `_config_`
    - Configuration of infrastructure
    - Keeps framework details out of domain logic

- `_messaging_`
    - Enables async communication between bounded contexts
    - Loose coupling: Didactic-Personal not aware of consumers

- `_model_`
    - Business rules and domain model
    - Domain service enforces global rules (e.g., class code uniqueness)
    - Independent of technology

## API

- OpenAPI framework adopted for API management
- Single source of definition (`didactic-personal.yaml`)
- APIs mapped to actors:
    - `/admin` → Administrators
    - `/professors` → Professors
    - `/students` → Students
- Ensures consistency, automatic interface generation, and clear communication

## How SOLID Principles Are Applied

- **Single Responsibility**: controller = API, service = workflows, model = state/behavior
- **Open/Closed**: adding a new actor (e.g., "Parent") extends controllers/services without breaking existing code
- **Liskov Substitution**: interfaces (repositories, services) can be replaced by mocks in tests
- **Interface Segregation**: classes kept small and focused
- **Dependency Inversion**: controllers depend on abstractions, not JPA or RabbitMQ directly
