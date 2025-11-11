# Tasks: Upgrade to Java 25 and Spring Boot 4

## Change ID

`upgrade-java25`

## Pre-Implementation Research

- [ ] Audit critical dependencies for Java 25 compatibility:
    - [ ] Lombok
    - [ ] MapStruct
    - [ ] Keycloak libraries
    - [ ] Spring Cloud OpenFeign
    - [ ] Springdoc OpenAPI
    - [ ] Flyway
    - [ ] Testcontainers
- [ ] Check OpenRewrite recipe availability for Spring Boot 4 migration

## Build Configuration Updates

- [ ] Update root `pom.xml`:
    - [ ] Set `java.version` property to `25`
    - [ ] Set `spring-boot.version` property to verified Spring Boot 4.x version
    - [ ] Update Spring Boot parent version in `<parent>` section
    - [ ] Update OpenRewrite active recipe to Spring Boot 4 migration (if available)
- [ ] Update Maven plugin versions (if needed):
    - [ ] `maven-compiler-plugin` (verify Java 25 support)
    - [ ] `maven-surefire-plugin` (verify compatibility)
    - [ ] `maven-failsafe-plugin` (verify compatibility)
    - [ ] `spotless-maven-plugin` (verify Java 25 syntax support)
    - [ ] `lombok-maven-plugin` (verify Java 25 support)
- [ ] Run `mvn clean` to clear old build artifacts

## Dependency Updates

- [ ] Update `lsadf_dependencies/pom.xml`:
    - [ ] Update Keycloak versions if needed for Spring Boot 4 compatibility
    - [ ] Update Spring Cloud OpenFeign version if needed
    - [ ] Update Springdoc OpenAPI version if needed
    - [ ] Update Flyway version if needed
    - [ ] Update any other incompatible dependencies identified in research phase
- [ ] Verify dependency resolution:
    - [ ] Run `mvn dependency:tree` to check for conflicts
    - [ ] Run `mvn enforcer:enforce` if enforcer plugin is configured
    - [ ] Resolve any version conflicts

## Docker Infrastructure Updates

- [ ] Update `docker/Dockerfile`:
    - [ ] Change compiler stage base image to `maven:3.9-eclipse-temurin-25-noble` (or latest available)
    - [ ] Change builder stage base image to `eclipse-temurin:25-jre-noble` (verify exact tag)
    - [ ] Change final stage base image to `eclipse-temurin:25-jre-noble`
- [ ] Update `docker/Dockerfile-dev`:
    - [ ] Change base image to Java 25 equivalent
- [ ] Verify Docker build locally:
    - [ ] Run `make build` to build Docker images
    - [ ] Check for any Docker-specific errors
    - [ ] Verify image size is reasonable

## Code Migration with OpenRewrite

- [ ] Run OpenRewrite dry-run (if Java 25 recipe available):
    - [ ] `mvn rewrite:dryRun` to see proposed changes
- [ ] Run OpenRewrite dry-run (if Spring Boot 4 recipe available):
- [ ] Apply OpenRewrite migrations:
    - [ ] `mvn rewrite:run` to apply automated fixes
    - [ ] Review and commit changes
- [ ] If no OpenRewrite recipe available:
    - [ ] Document manual migration steps needed
    - [ ] Proceed with manual fixes

## Compilation and Build Fixes

- [ ] Attempt initial build:
    - [ ] Run `mvn clean install -DskipTests`
    - [ ] Document all compilation errors
- [ ] Fix compilation errors systematically:
    - [ ] Fix in `lsadf_core` module first (foundation)
    - [ ] Fix in `lsadf_api` module
    - [ ] Fix in `lsadf_admin` module
    - [ ] Fix in `lsadf_bdd` module
- [ ] Address common issues:
    - [ ] Update deprecated API usage
    - [ ] Fix removed API references
    - [ ] Update annotation processor issues (Lombok, MapStruct)
    - [ ] Fix Jakarta namespace issues (if any remaining)
- [ ] Verify successful build:
    - [ ] Run `mvn clean install -DskipTests` to confirm compilation succeeds

## Configuration Updates

- [ ] Review and update application configuration files:
    - [ ] `lsadf_api/src/main/resources/application*.yml`
    - [ ] `lsadf_admin/src/main/resources/application*.yml`
    - [ ] Check for any Java 25-specific configuration needs
    - [ ] Verify all properties are compatible
- [ ] Review and update logging configuration:
    - [ ] `logback.xml` files for any needed updates
    - [ ] Verify logging works with new versions

## Testing - Unit Tests

- [ ] Run unit tests:
    - [ ] `mvn test` in root directory
    - [ ] Document all test failures
- [ ] Fix unit test failures:
    - [ ] Fix test framework compatibility issues
    - [ ] Update mock configurations if needed
    - [ ] Fix assertion issues
    - [ ] Update test annotations if changed
- [ ] Verify unit test success:
    - [ ] Run `make test-unit` to confirm all unit tests pass
    - [ ] Review code coverage reports

## Testing - BDD Tests

- [ ] Run BDD tests:
    - [ ] `mvn verify -Pbdd` or `make test-bdd`
    - [ ] Document all BDD test failures
- [ ] Fix BDD test failures:
    - [ ] Fix Cucumber step definitions
    - [ ] Update test containers configuration
    - [ ] Fix Keycloak integration test issues
    - [ ] Update test data if needed
- [ ] Verify BDD test success:
    - [ ] Run `make test-bdd` to confirm all BDD tests pass
    - [ ] Review test reports

## Testing - Full Suite

- [ ] Run complete test suite:
    - [ ] `make test` to run all tests
    - [ ] Confirm no regressions
- [ ] Run code quality checks:
    - [ ] `make lint-check` to verify code formatting
    - [ ] `make lint` to apply formatting if needed
    - [ ] Verify license headers with `mvn license:check`
- [ ] Generate test reports:
    - [ ] `make report` to generate Surefire reports
    - [ ] Review for any anomalies

## Docker Validation

- [ ] Build Docker images:
    - [ ] `make build` to build production images
    - [ ] `make builddev` to build development images
    - [ ] Verify both amd64 and arm64 architectures build (if multi-platform)
- [ ] Test Docker runtime:
    - [ ] `make up` to start services
    - [ ] Verify application starts successfully
    - [ ] Check application logs for errors
    - [ ] Test API endpoints for basic functionality
    - [ ] `make down` to stop services

## Local Integration Testing

- [ ] Start full local environment:
    - [ ] `make dbup` to start PostgreSQL and Redis
    - [ ] Start application in Docker
    - [ ] Verify database connectivity
    - [ ] Verify Redis connectivity
    - [ ] Verify Keycloak integration
- [ ] Smoke test critical APIs:
    - [ ] Test authentication endpoints
    - [ ] Test admin endpoints
    - [ ] Test game mail endpoints (if applicable)
    - [ ] Verify response formats unchanged

## Documentation Updates

- [ ] Update `openspec/project.md`:
    - [ ] Change "Java 21" to "Java 25"
    - [ ] Update any version-specific references
- [ ] Update `README.md` (if exists):
    - [ ] Update prerequisite versions
    - [ ] Update any setup instructions
    - [ ] Update build command examples if changed
- [ ] Update any other version-specific documentation:
    - [ ] Check for hardcoded version references
    - [ ] Update developer setup guides
    - [ ] Update deployment documentation

## Final Validation

- [ ] Run full CI validation locally:
    - [ ] `make clean`
    - [ ] `make install-ci`
    - [ ] `make test-ci`
    - [ ] Verify all steps pass
- [ ] Create comprehensive test run:
    - [ ] Unit tests pass
    - [ ] BDD tests pass
    - [ ] Docker builds succeed
    - [ ] Code quality checks pass
    - [ ] No critical warnings in logs
- [ ] Review changes:
    - [ ] Review all modified files
    - [ ] Ensure no unintended changes
    - [ ] Verify commit message describes upgrade
    - [ ] Prepare PR description with migration notes

## Post-Implementation

- [ ] Monitor for issues:
    - [ ] Watch for any runtime issues
    - [ ] Monitor performance metrics
    - [ ] Check for memory usage changes
- [ ] Document lessons learned:
    - [ ] Note any unexpected issues encountered
    - [ ] Document workarounds applied
    - [ ] Update design.md with actual decisions made
- [ ] Prepare for archive:
    - [ ] Ensure all tasks completed
    - [ ] Update proposal status
    - [ ] Prepare for archiving after deployment

