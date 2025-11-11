# Tasks: Upgrade to Java 25 and Spring Boot 4

## Change ID

`upgrade-java25`

## Pre-Implementation Research

- [x] Audit critical dependencies for Java 25 compatibility:
    - [x] Lombok
    - [x] MapStruct
    - [x] Keycloak libraries
    - [x] Spring Cloud OpenFeign
    - [x] Springdoc OpenAPI
    - [x] Flyway
    - [x] Testcontainers

## Build Configuration Updates

- [x] Update root `pom.xml`:
    - [x] Set `java.version` property to `25`
- [x] Update Maven plugin versions (if needed):
    - [x] `maven-compiler-plugin` (verify Java 25 support)
    - [x] `maven-surefire-plugin` (verify compatibility)
    - [x] `maven-failsafe-plugin` (verify compatibility)
    - [x] `spotless-maven-plugin` (verify Java 25 syntax support)
    - [x] `lombok-maven-plugin` (verify Java 25 support)
- [x] Run `mvn clean` to clear old build artifacts

## Dependency Updates

- [x] Update `lsadf_dependencies/pom.xml`:
    - [x] Update Keycloak versions if needed for Spring Boot 4 compatibility
    - [x] Update Spring Cloud OpenFeign version if needed
    - [x] Update Springdoc OpenAPI version if needed
    - [x] Update Flyway version if needed
    - [x] Update any other incompatible dependencies identified in research phase
- [x] Verify dependency resolution:
    - [x] Run `mvn dependency:tree` to check for conflicts
    - [x] Run `mvn enforcer:enforce` if enforcer plugin is configured
    - [x] Resolve any version conflicts

## Docker Infrastructure Updates

- [x] Update `docker/Dockerfile`:
    - [x] Change compiler stage base image to `maven:3.9-eclipse-temurin-25-noble` (or latest available)
    - [x] Change builder stage base image to `eclipse-temurin:25-jre-noble` (verify exact tag)
    - [x] Change final stage base image to `eclipse-temurin:25-jre-noble`
- [x] Update `docker/Dockerfile-dev`:
    - [x] Change base image to Java 25 equivalent
- [x] Verify Docker build locally:
    - [x] Run `make build` to build Docker images
    - [x] Check for any Docker-specific errors
    - [x] Verify image size is reasonable

## Code Migration with OpenRewrite

- [x] Run OpenRewrite dry-run (if Java 25 recipe available):
    - [x] `mvn rewrite:dryRun` to see proposed changes
- [x] Run OpenRewrite dry-run (if Spring Boot 4 recipe available):
- [x] Apply OpenRewrite migrations:
    - [x] `mvn rewrite:run` to apply automated fixes
    - [x] Review and commit changes
- [x] If no OpenRewrite recipe available:
    - [x] Document manual migration steps needed
    - [x] Proceed with manual fixes

## Compilation and Build Fixes

- [x] Attempt initial build:
    - [x] Run `mvn clean install -DskipTests`
    - [x] Document all compilation errors
- [x] Fix compilation errors systematically:
    - [x] Fix in `lsadf_core` module first (foundation)
    - [x] Fix in `lsadf_api` module
    - [x] Fix in `lsadf_admin` module
    - [x] Fix in `lsadf_bdd` module
- [x] Address common issues:
    - [x] Update deprecated API usage
    - [x] Fix removed API references
    - [x] Update annotation processor issues (Lombok, MapStruct)
    - [x] Fix Jakarta namespace issues (if any remaining)
- [x] Verify successful build:
    - [x] Run `mvn clean install -DskipTests` to confirm compilation succeeds

## Configuration Updates

- [x] Review and update application configuration files:
    - [x] `lsadf_api/src/main/resources/application*.yml`
    - [x] `lsadf_admin/src/main/resources/application*.yml`
    - [x] Check for any Java 25-specific configuration needs
    - [x] Verify all properties are compatible
- [x] Review and update logging configuration:
    - [x] `logback.xml` files for any needed updates
    - [x] Verify logging works with new versions

## Testing - Unit Tests

- [x] Run unit tests:
    - [x] `mvn test` in root directory
    - [x] Document all test failures
- [x] Fix unit test failures:
    - [x] Fix test framework compatibility issues
    - [x] Update mock configurations if needed
    - [x] Fix assertion issues
    - [x] Update test annotations if changed
- [x] Verify unit test success:
    - [x] Run `make test-unit` to confirm all unit tests pass
    - [x] Review code coverage reports

## Testing - BDD Tests

- [x] Run BDD tests:
    - [x] `mvn verify -Pbdd` or `make test-bdd`
    - [x] Document all BDD test failures
- [x] Fix BDD test failures:
    - [x] Fix Cucumber step definitions
    - [x] Update test containers configuration
    - [x] Fix Keycloak integration test issues
    - [x] Update test data if needed
- [x] Verify BDD test success:
    - [x] Run `make test-bdd` to confirm all BDD tests pass
    - [x] Review test reports

## Testing - Full Suite

- [x] Run complete test suite:
    - [x] `make test` to run all tests
    - [x] Confirm no regressions
- [x] Run code quality checks:
    - [x] `make lint-check` to verify code formatting
    - [x] `make lint` to apply formatting if needed
    - [x] Verify license headers with `mvn license:check`
- [x] Generate test reports:
    - [x] `make report` to generate Surefire reports
    - [x] Review for any anomalies

## Docker Validation

- [x] Build Docker images:
    - [x] `make build` to build production images
    - [x] `make builddev` to build development images
    - [x] Verify both amd64 and arm64 architectures build (if multi-platform)
- [x] Test Docker runtime:
    - [x] `make up` to start services
    - [x] Verify application starts successfully
    - [x] Check application logs for errors
    - [x] Test API endpoints for basic functionality
    - [x] `make down` to stop services

## Local Integration Testing

- [x] Start full local environment:
    - [x] `make dbup` to start PostgreSQL and Redis
    - [x] Start application in Docker
    - [x] Verify database connectivity
    - [x] Verify Redis connectivity
    - [x] Verify Keycloak integration
- [x] Smoke test critical APIs:
    - [x] Test authentication endpoints
    - [x] Test admin endpoints
    - [x] Test game mail endpoints (if applicable)
    - [x] Verify response formats unchanged

## Documentation Updates

- [x] Update `openspec/project.md`:
    - [x] Change "Java 21" to "Java 25"
    - [x] Update any version-specific references
- [x] Update `README.md` (if exists):
    - [x] Update prerequisite versions
    - [x] Update any setup instructions
    - [x] Update build command examples if changed
- [x] Update any other version-specific documentation:
    - [x] Check for hardcoded version references
    - [x] Update developer setup guides
    - [x] Update deployment documentation

## Final Validation

- [x] Run full CI validation locally:
    - [x] `make clean`
    - [x] `make install-ci`
    - [x] `make test-ci`
    - [x] Verify all steps pass
- [x] Create comprehensive test run:
    - [x] Unit tests pass
    - [x] BDD tests pass
    - [x] Docker builds succeed
    - [x] Code quality checks pass
    - [x] No critical warnings in logs
- [x] Review changes:
    - [x] Review all modified files
    - [x] Ensure no unintended changes
    - [x] Verify commit message describes upgrade
    - [x] Prepare PR description with migration notes
