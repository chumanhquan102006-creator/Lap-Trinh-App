#!/bin/sh
APP_HOME=$(cd "$(dirname "$0")" && pwd)
if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
  echo "Missing gradle-wrapper.jar. Run setup-wrapper.ps1 on Windows or download the official Gradle 8.11.1 wrapper jar." >&2
  exit 1
fi
exec java -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
