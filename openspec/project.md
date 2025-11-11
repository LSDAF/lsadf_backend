# Project Context

## Purpose

LSADF Backend is a multi-module Spring Boot application providing REST APIs for the LSADF Game. The project is organized
as a Maven multi-module structure with distinct API, Core, Admin, BDD testing, and build configuration modules.

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven (multi-module project)
- **Database**: PostgreSQL
- **Cache**: Redis
- **Authentication**: Keycloak (OAuth2/OIDC)
- **API Documentation**: Swagger/OpenAPI (Springdoc)
- **Testing**: JUnit, Cucumber (BDD), AssertJ, Testcontainers
- **Data Access**: Spring Data JPA, Flyway (migrations)
- **HTTP Client**: Spring Cloud OpenFeign
- **Object Mapping**: MapStruct
- **Utilities**: Lombok, Apache Commons (IO, Lang3, FileUpload)
- **Containerization**: Docker (multi-stage builds with Eclipse Temurin JRE)

## Project Conventions

### Code Style

This project strictly follows the **Google Java Style Guide** (https://google.github.io/styleguide/javaguide.html).

- **Formatting**: Enforced via Spotless Maven Plugin with Google Java Format
- **Linting Commands**:
    - `make lint-check`: Dry run to check code formatting
    - `make lint`: Auto-format code to comply with style guide
- **License Headers**: All source files must include Apache 2.0 license headers (enforced via license-maven-plugin)
- **Pre-commit Hooks**: Automatic code formatting on Java files before commits (see `.pre-commit-script.sh`)

### Architecture Patterns

- **Multi-module Maven Structure**:
    - `lsadf_api`: Main API application module
    - `lsadf_core`: Core domain logic and shared components
    - `lsadf_admin`: Admin functionality
    - `lsadf_bdd`: BDD/Integration test module
    - `lsadf_bom`: Bill of Materials for dependency management
    - `lsadf_dependencies`: Centralized dependency management
    - `lsadf_build_tools`: Build configuration and tools
    - `build_parent`: Parent POM for common build configuration
- **Layered Architecture**: Separation of concerns with distinct layers (API, Service, Repository)
- **Docker Layering**: Optimized Docker images using Spring Boot layertools for efficient caching

### Testing Strategy

- **Unit Tests**: JUnit-based unit tests in each module
- **BDD Tests**: Cucumber-based behavior-driven tests in `lsadf_bdd` module
- **Test Execution**:
    - `make test`: Run all tests (unit + BDD)
    - `make test-unit`: Run only unit tests
    - `make test-bdd`: Run only BDD tests
    - `make test-ci`: CI-optimized test execution with minimal logging
    - `make report`: Generate Surefire test reports with Cucumber integration
- **Test Containers**: Used for integration testing with PostgreSQL, Redis, and Keycloak
- **Code Coverage**: JaCoCo integration for coverage reports

### Build & Development Workflow

- **Installation**:
    - `make install`: Build project locally (skips tests, includes env copy)
    - `make install-ci`: CI-optimized build with batch mode
    - `make clean`: Clean build artifacts
- **Documentation**:
    - `make javadoc`: Generate aggregated JavaDoc for all modules
- **Pre-commit Hooks**:
    - `make install-pre-commit`: Install pre-commit hooks
    - Python virtual environment required for pre-commit (see `requirements.txt`)
    - Hooks automatically format Java files and validate before commits

### Git Workflow

- **Branch Naming**: Branch names are normalized (slashes replaced with dashes) for Docker image tagging
- **Pre-commit Validation**: Automatic code formatting and validation on staged Java files
- **Commit Standards**: Code must pass Spotless formatting and license checks before commits
- **CI/CD Integration**: Automated builds, tests, and linting in CI pipeline

## Domain Context

The LSADF platform provides API services with authentication/authorization via Keycloak integration. The system uses
PostgreSQL for persistent storage and Redis for caching, with comprehensive BDD test coverage to ensure behavioral
correctness.

## Important Constraints

- **Java Version**: Must use Java 21 (LTS)
- **License**: Apache License 2.0 - all source files must include proper headers
- **Code Style**: Non-negotiable adherence to Google Java Style Guide
- **Character Encoding**: UTF-8 for all source and resource files
- **Docker Base Images**: Eclipse Temurin JRE for production images
- **Multi-platform Support**: Docker images built for linux/amd64 and linux/arm64

## External Dependencies

- **Keycloak**: Authentication and authorization server (realm configurations in `keycloak_realm/`)
- **PostgreSQL**: Primary database (embedded Zonky for tests)
- **Redis**: Caching layer (embedded Redis for tests)
- **Maven Central**: Dependency repository
- **GitHub Container Registry (GHCR)**: Docker image registry for deployment
