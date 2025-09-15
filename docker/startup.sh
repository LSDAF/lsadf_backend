#!/bin/sh

# Set default profile if not provided
echo "APP_PROFILE=$APP_PROFILE";

# Validate profile parameter
case "$APP_PROFILE" in
  api|standalone|worker)
    echo "Starting application with profiles: docker,$APP_PROFILE"
    ;;
  *)
    echo "Invalid profile: $APP_PROFILE. Valid profiles are: api, standalone, worker"
    exit 1
    ;;
esac

if [ "$SLEEP_ENABLED" = "true" ]
  then
    echo "SLEEP_ENABLED: true";
    echo "Sleeping 10 seconds...";
    sleep 10;
  else
    echo "SLEEP_ENABLED: false"
    echo "Running application..."
fi

java \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -Xss256k \
  -XX:-HeapDumpOnOutOfMemoryError \
  -XX:+UseStringDeduplication \
  -XX:MaxMetaspaceSize=128m \
  -Dspring.profiles.active=docker,${APP_PROFILE} \
  org.springframework.boot.loader.launch.JarLauncher