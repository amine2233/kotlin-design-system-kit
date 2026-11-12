#!/usr/bin/env bash
set -euo pipefail

export JAVA_HOME="${JAVA_HOME:-$HOME/.local/share/mise/installs/java/temurin-17.0.20+101}"
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
KTLINT="${KTLINT:-$HOME/.local/share/mise/installs/ktlint/1.8.0/ktlint-1.8.0/bin/ktlint}"

"$KTLINT" '**/*.kt' '**/*.kts' '!**/build/**' --reporter=plain
./gradlew assembleDebug testDebugUnitTest :designsystem:catalog:validateDebugScreenshotTest --no-daemon --console=plain
