# Design: Upgrade to Java 25

## Change ID

`upgrade-java25`

## Overview

This document outlines the technical approach for upgrading the LSADF Backend from Java 21 to Java 25 while maintaining
Spring Boot 3.5.6.

## Architecture Impact

### Component Changes

```
┌─────────────────────────────────────────────────────────────┐
│                     Build System                             │
│  - Maven Parent POM (Java 25, Spring Boot 3.5.6)            │
│  - Module POMs (inherit versions)                            │
│  - Build plugins (compiler, surefire, failsafe)             │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                  Docker Infrastructure                       │
│  - Base images: eclipse-temurin:25-jre                      │
│  - Multi-stage builds (compiler + runtime)                   │
│  - Layer extraction for optimization                         │
└─────────────────────────────────────────────────────────────┘
                           ↓
┌─────────────────────────────────────────────────────────────┐
│                 Application Modules                          │
│  - lsadf_api (Main API)                                     │
│  - lsadf_core (Core logic)                                  │
│  - lsadf_admin (Admin functionality)                        │
│  - lsadf_bdd (Integration tests)                            │
└─────────────────────────────────────────────────────────────┘
```

### Version Selection

**Java 25**

- Latest LTS release
- Provides access to latest JVM optimizations
- Preview features available for experimentation
- Well-supported by Eclipse Temurin

## Technical Decisions

### 1. Version Pinning Strategy

**Decision**: Use explicit version properties in root POM

```xml

<java.version>25</java.version>
```

**Rationale**:

- Central version management
- Consistent across all modules
- Easy to update in one place

### 2. Docker Base Image Strategy

**Decision**: Use Eclipse Temurin JRE 25

```dockerfile
FROM eclipse-temurin:25-jre
```

**Rationale**:

- Eclipse Temurin is the official OpenJDK distribution
- Well-maintained and widely adopted
- JRE-only images for smaller production footprint
- Multi-architecture support (amd64/arm64)

**Investigation Required**:

- Verify exact Eclipse Temurin 25 JRE image tag availability
- Check for any breaking changes in image structure

### 3. Maven Plugin Compatibility

**Critical Plugins to Verify**:

- `maven-compiler-plugin`: Must support Java 25
- `spring-boot-maven-plugin`: Must work with Spring Boot 3.5.6 and Java 25
- `maven-surefire-plugin`: Test execution compatibility with Java 25
- `spotless-maven-plugin`: Java 25 syntax support
- `lombok-maven-plugin`: Java 25 compatibility
- `mapstruct`: Java 25 annotation processing

**Decision**: Update plugins proactively if needed for Java 25 compatibility

### 4. Dependency Compatibility Matrix

**Spring Ecosystem**:

- Spring Boot 3.5.6 (maintained) → Spring Framework 6.x
- Spring Cloud OpenFeign: Should remain compatible with Java 25
- Springdoc OpenAPI: Should remain compatible with Java 25

**Security & Auth**:

- Keycloak Spring Boot Starter: Verify Java 25 support
- Keycloak Admin Client: Should remain compatible

**Testing**:

- JUnit 5: Already compatible
- Cucumber: Verify latest version compatibility with Java 25
- Testcontainers: Should remain compatible
- AssertJ: Should remain compatible

**Persistence**:

- Spring Data JPA: Included in Spring Boot BOM
- Flyway: Verify Java 25 compatibility
- PostgreSQL Driver: Should remain compatible

**Utilities**:

- Lombok: Must support Java 25
- MapStruct: Must support Java 25
- Apache Commons: Should remain compatible

### 5. OpenRewrite Migration Support

**Decision**: Leverage OpenRewrite for automated migration

The project already has OpenRewrite configured:

```xml

<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>6.21.0</version>
</plugin>
```

**Actions**:

1. Check for `org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0` recipe
2. Run OpenRewrite dry-run to identify necessary changes
3. Apply automated migrations
4. Review and test changes

### 6. Breaking Changes Handling

**Expected Breaking Changes**:

**Java 21 → 25**:

- Preview features from Java 22-24 may be finalized
- Some deprecated APIs may be removed
- Module system enhancements

**Spring Boot 3.5 → 4.0**:

- Property name changes
- Deprecated APIs removed
- Jakarta EE namespace updates (if not already done)
- Auto-configuration changes

**Strategy**:

1. Document all breaking changes encountered
2. Fix compilation errors first
3. Fix runtime errors second
4. Fix tests last
5. Update documentation throughout

### 7. Testing Strategy

**Multi-Phase Testing**:

**Phase 1: Build Validation**

- Maven clean install succeeds
- No compilation errors
- Spotless formatting passes
- License checks pass

**Phase 2: Unit Tests**

- All unit tests pass
- No test framework compatibility issues
- Code coverage maintained

**Phase 3: BDD Tests**

- All Cucumber tests pass
- Testcontainers work correctly
- Keycloak integration functional

**Phase 4: Docker Validation**

- Images build successfully
- Multi-architecture support verified
- Application starts in container
- Health checks pass

**Phase 5: Integration Testing**

- Docker Compose stack runs
- APIs respond correctly
- Database migrations work
- Cache integration functional

## Implementation Approach

### Phased Rollout

**Phase 1: Research & Preparation**

1. Check Eclipse Temurin 25 JRE image availability
2. Audit all dependencies for Java 25 compatibility
3. Document known breaking changes in Java 25

**Phase 2: Build Configuration**

1. Update root POM Java version
2. Update Docker base images to Eclipse Temurin 25
3. Update plugin versions if needed for Java 25 compatibility
4. Run OpenRewrite migration recipes if available

**Phase 3: Dependency Updates**

1. Update incompatible dependencies for Java 25
2. Resolve version conflicts
3. Test dependency resolution

**Phase 4: Code Fixes**

1. Fix compilation errors
2. Update deprecated API usage
3. Address runtime issues
4. Test with Java 25 JVM

**Phase 5: Testing & Validation**

1. Run full test suite
2. Fix failing tests
3. Validate Docker builds
4. Test in local Docker environment

**Phase 6: Documentation**

1. Update project.md with Java 25 version
2. Update README.md
3. Update any version-specific documentation

## Rollback Plan

If critical issues are discovered:

1. **Immediate**: Revert commits via Git
2. **Build**: Maven builds from previous commit still work
3. **Docker**: Previous images remain available
4. **Testing**: Test suite validates rollback success

## Open Questions

1. **Dependency Compatibility**: Are there any known blockers for critical dependencies with Java 25?
2. **Migration Tools**: Does OpenRewrite have Java 25 migration recipes ready?

## References

- [JDK 25 Release Notes](https://jdk.java.net/25/)
- [Eclipse Temurin Releases](https://adoptium.net/temurin/releases/)
- [OpenRewrite Java Migrations](https://docs.openrewrite.org/recipes/java/migrate)
- [Spring Boot 3.5.6 Documentation](https://docs.spring.io/spring-boot/docs/3.5.6/reference/html/)

