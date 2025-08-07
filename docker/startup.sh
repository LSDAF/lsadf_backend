#!/bin/sh
if [ "$SLEEP_ENABLED" = "true" ]
  then
    echo "SLEEP_ENABLED: true";
    echo "Sleeping 10 seconds...";
    sleep 10;
  else
    echo "SLEEP_ENABLED: false"
    echo "Running application..."
fi

java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Dspring.profiles.active=docker org.springframework.boot.loader.launch.JarLauncher