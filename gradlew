#!/bin/sh
set -e
BASE_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if [ ! -f "$BASE_DIR/gradle/wrapper/gradle-wrapper.jar" ]; then
  curl -fL 'https://raw.githubusercontent.com/chumanhquan102006-creator/Lap-Trinh-App/main/gradle/wrapper/gradle-wrapper.jar' -o "$BASE_DIR/gradle/wrapper/gradle-wrapper.jar"
fi
exec java -cp "$BASE_DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
