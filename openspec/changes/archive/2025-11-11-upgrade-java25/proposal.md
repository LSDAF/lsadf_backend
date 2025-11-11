# Proposal: Upgrade to Java 25

## Change ID

`upgrade-java25`

## Summary

Upgrade the LSADF Backend project from Java 21 to Java 25 to take advantage of the latest language features, performance
improvements, and security updates while maintaining Spring Boot 3.5.6.

## Motivation

- **Java 25**: Access to latest LTS JDK features, performance improvements, and security updates
- **Performance**: Leverage JVM optimizations and improvements in Java 25
- **Security**: Benefit from latest security patches and best practices
- **Future-proofing**: Stay current with LTS Java versions
- **Compatibility**: Maintain Spring Boot 3.5.6 for stability while upgrading the JDK

## Scope

This change affects the following areas:

### Build Configuration

- Maven parent POM configuration (Java version only)
- All module POMs inheriting the Java version
- Maven compiler plugin settings
- Maven plugin compatibility with Java 25

### Docker Infrastructure

- Dockerfile base images (Eclipse Temurin JRE for Java 25)
- Multi-stage build configurations
- Container runtime compatibility

### Dependencies

- Third-party library compatibility verification with Java 25
- Annotation processors (Lombok, MapStruct) compatibility with Java 25
- Testing frameworks (JUnit, Cucumber, Testcontainers)

### Documentation

- Project documentation updates (project.md)
- Build and deployment instructions
- Version references in README

## Out of Scope

- Spring Boot version changes (staying on Spring Boot 3.5.6)
- Application code changes (unless required for Java 25 compatibility)
- Database schema changes
- API contract changes
- Feature additions or removals
- Configuration changes beyond JDK version upgrade

## Dependencies

None - this is a foundational infrastructure upgrade.

## Risks and Mitigation

### Risks

1. **Dependency incompatibility**: Third-party libraries may not yet support Java 25
2. **Docker image availability**: Eclipse Temurin JRE images for Java 25 may have different versioning
3. **Annotation processor compatibility**: Lombok and MapStruct may need updates for Java 25
4. **Test failures**: Existing tests may fail due to JVM behavior changes
5. **Build tool compatibility**: Maven plugins may need updates for Java 25 support

### Mitigation

1. Comprehensive testing of all existing test suites (unit + BDD)
2. Dependency version auditing and Java 25 compatibility verification
3. Incremental rollout with ability to rollback
4. Documentation of all breaking changes encountered
5. Test in isolated environment before production deployment

## Affected Capabilities

This is an infrastructure/build system change and does not directly modify any existing capability specifications.
However, it affects the underlying platform for all capabilities:

- **Build System**: Complete rebuild of version management
- **Deployment**: Docker images and runtime environment
- **Testing**: Test execution environment

## Success Criteria

- [ ] All Maven builds complete successfully
- [ ] All unit tests pass
- [ ] All BDD tests pass
- [ ] Docker images build successfully for both amd64 and arm64 architectures
- [ ] Application starts successfully in Docker environment
- [ ] No regressions in existing functionality
- [ ] Code formatting and linting checks pass
- [ ] Documentation updated to reflect new versions

