# Stage 1: Build the application
FROM dhi.io/maven:3-jdk25-debian13-dev AS compiler

# Build argument to select module (default: lsadf_api)
ARG MODULE=lsadf_api

WORKDIR /build

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


FROM dhi.io/maven:3-jdk25-debian13-dev AS builder

WORKDIR /build

ARG MODULE=lsadf_api

COPY --from=compiler /build/${MODULE}/target/app.jar app.jar
COPY --from=compiler /build/${MODULE}/target/classes/logback-docker.xml logback-docker.xml

RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted
COPY --from=compiler /build/startup.sh .


# Stage 3: Create the final image
FROM eclipse-temurin:25-jre-noble AS app-image

WORKDIR /app
RUN mkdir logs && \
    addgroup --system lsadf \
    && adduser --system --ingroup lsadf lsadf \
    && chown -R lsadf:lsadf . \
    && chown -R lsadf:lsadf "$JAVA_HOME"

# Copy layers from the build stage
COPY --chown=lsadf:lsadf --from=builder /build/extracted/dependencies/ ./
COPY --chown=lsadf:lsadf --from=builder /build/extracted/spring-boot-loader/ ./
COPY --chown=lsadf:lsadf --from=builder /build/extracted/snapshot-dependencies/ ./
COPY --chown=lsadf:lsadf --from=builder /build/extracted/application/ ./
COPY --chown=lsadf:lsadf --from=builder /build/startup.sh ./
COPY --chown=lsadf:lsadf --from=builder /build/logback-docker.xml ./

RUN chmod +x startup.sh

LABEL org.opencontainers.image.source="https://github.com/LSDAF/lsadf_backend"
LABEL org.opencontainers.image.description="LSADF container image."

USER lsadf
EXPOSE 8080

ENTRYPOINT ["./startup.sh"]
