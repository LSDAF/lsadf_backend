#!/bin/sh

SLEEP_ENABLED=${SLEEP_ENABLED:-false}
SLEEP_SECONDS=${SLEEP_SECONDS:-5}

if [ "$SLEEP_ENABLED" = "true" ]; then
  echo "Sleeping for 30 seconds to allow dependent services to start..."
  sleep $SLEEP_SECONDS
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
  -Dspring.profiles.active=docker,${APP_PROFILE} \
  org.springframework.boot.loader.launch.JarLauncher