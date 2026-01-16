#!/bin/sh

SLEEP_ENABLED=${SLEEP_ENABLED:-false}
SLEEP_SECONDS=${SLEEP_SECONDS:-5}

if [ "$SLEEP_ENABLED" = "true" ]; then
  echo "Sleeping for $SLEEP_SECONDS seconds to allow dependent services to start..."
  sleep $SLEEP_SECONDS
fi

SPRING_PROFILES_ACTIVE="docker"

if [ -n "$APP_PROFILE" ]; then
  SPRING_PROFILES_ACTIVE="$SPRING_PROFILES_ACTIVE,$APP_PROFILE"
else
  echo "No APP_PROFILE specified, defaulting to 'docker' profile only."
fi

java \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -Xss256k \
  -XX:-HeapDumpOnOutOfMemoryError \
  -XX:+UseStringDeduplication \
  -XX:+ExitOnOutOfMemoryError \
  -XX:MaxMetaspaceSize=128m \
  -Dspring.profiles.active="${SPRING_PROFILES_ACTIVE}" \
  -Djava.io.tmpdir=/temp \
  org.springframework.boot.loader.launch.JarLauncher