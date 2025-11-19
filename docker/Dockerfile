# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-25-noble AS compiler

# Build argument to select module (default: lsadf_api)
ARG MODULE=lsadf_api

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

# Delete the test jar to reduce the image size and fix docker build duplicate jar issue
RUN mvn clean install -DskipTests --quiet \
    && rm ${MODULE}/target/*-tests.jar \
    && mv ${MODULE}/target/*.jar ${MODULE}/target/app.jar


FROM eclipse-temurin:25-jre-noble AS builder

ARG MODULE=lsadf_api

COPY --from=compiler ${MODULE}/target/app.jar app.jar
COPY --from=compiler ${MODULE}/target/classes/logback-docker.xml logback-docker.xml

RUN java -Djarmode=layertools -jar app.jar extract
COPY --from=compiler startup.sh .

# Stage 3: Create the final image
FROM eclipse-temurin:25-jre-noble

# Copy layers from the build stage
COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./
COPY --from=builder startup.sh ./
COPY --from=builder logback-docker.xml ./

RUN addgroup --system lsadf \
    && adduser --system --ingroup lsadf lsadf \
    && chown -R lsadf:lsadf . \
    && mkdir logs \
    && chmod +x startup.sh

LABEL org.opencontainers.image.source="https://github.com/LSDAF/lsadf_backend"
LABEL org.opencontainers.image.description="LSADF container image."

USER lsadf
EXPOSE 8080

ENTRYPOINT ["./startup.sh"]
