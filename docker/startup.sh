#!/bin/sh


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