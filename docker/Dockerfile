# Stage 1: Build the application
FROM maven:3.9.11-eclipse-temurin-21-noble AS compiler
COPY env env/
COPY build_parent build_parent/
COPY lsadf_build_tools lsadf_build_tools/
COPY lsadf_bom/ lsadf_bom/
COPY lsadf_dependencies/ lsadf_dependencies/
COPY lsadf_core/ lsadf_core/
COPY lsadf_bdd/ lsadf_bdd/
COPY lsadf_api/ lsadf_api/
COPY lsadf_admin/ lsadf_admin/
COPY lsadf_test_report/ lsadf_test_report/

COPY pom.xml .
COPY ./docker/startup.sh .
# Delete application-local.yml to prevent ambiguous configurations
#RUN rm lsadf_api/src/main/resources/application-local.yml

# Delete the test jar to reduce the image size and fix docker build duplicate jar issue
RUN mvn clean install -DskipTests --quiet \
    && rm lsadf_api/target/*-tests.jar \
    && mv lsadf_api/target/*.jar lsadf_api/target/lsadf_api.jar


FROM eclipse-temurin:21.0.8_9-jre-noble AS builder

COPY --from=compiler lsadf_api/target/lsadf_api.jar lsadf_api.jar
COPY --from=compiler lsadf_api/target/classes/logback-docker.xml logback-docker.xml

RUN java -Djarmode=layertools -jar lsadf_api.jar extract
COPY --from=compiler startup.sh .

# Stage 3: Create the final image
FROM eclipse-temurin:21.0.8_9-jre-noble

EXPOSE 8080
LABEL org.opencontainers.image.source="https://github.com/LSDAF/lsadf_backend"
LABEL org.opencontainers.image.description="LSADF API container image."

# Copy layers from the build stage
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./
COPY --from=builder startup.sh ./
COPY --from=builder logback-docker.xml ./

RUN mkdir logs && chmod +x startup.sh

ENTRYPOINT ["./startup.sh"]