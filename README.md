# OfflineMind

OfflineMind is intended to be a native Android chat app that runs a user-selected language model locally. It will not require an account, API key, or runtime network access.

**Current status:** development foundation only. This build has a Compose shell, a generation-state unit test, and an automated no-`INTERNET` permission check. It does not import models, run inference, save chats, or produce a usable chat experience yet. Do not treat it as a release candidate.

## Build

Requirements: JDK 17, Android SDK platform 36 and Build Tools 36.0.0. The Gradle wrapper downloads Gradle 8.14.3. See [BUILDING.md](BUILDING.md) for the selected toolchain and the environment limitations observed during this task.

```sh
./gradlew testDebugUnitTest lintDebug assembleDebug
python3 scripts/check_no_internet_permission.py \
  --apk app/build/outputs/apk/debug/app-debug.apk
```

The APK output path above is the expected path for this single-module project; it has not been produced or verified in the current environment.

## Intended user journey

The planned release will let a user obtain a GGUF file independently, import it through Android's system file picker, load it into an on-device inference engine, chat and stop generation, then reopen locally saved conversations. Model weights will not be bundled or downloaded by the app. Import, inference, persistence, and offline device behavior are not implemented yet.

See [ROADMAP.md](ROADMAP.md), [ARCHITECTURE.md](ARCHITECTURE.md), and [TESTING.md](TESTING.md) for implementation status and gates.
