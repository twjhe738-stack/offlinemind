# Testing

## Automated checks

From the repository root on a host with JDK 17 and Android SDK platform 36:

```sh
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
python3 scripts/check_no_internet_permission.py \
  --apk app/build/outputs/apk/debug/app-debug.apk
```

The unit tests cover legal, invalid, stopped, failed, and retried generation-state transitions. They do not test a model or infer that the native engine works.

The permission script parses the source and merged manifests and checks packaged APK permissions using Android SDK tooling. CI runs it after building the debug APK. A source-only check can be run before building:

```sh
python3 scripts/check_no_internet_permission.py --source-only
```

## Results in this workspace

Static checks run 2026-10-20:

- **PASS:** source-only permission check; no `INTERNET` declaration found in the source manifest.
- **PASS:** permission parser detects both standard and `uses-permission-sdk-23` INTERNET declarations, and accepts a manifest without them.
- **PASS:** Python checker syntax, manifest/resource XML parsing, version-catalog TOML parsing, Gradle wrapper shell syntax, and wrapper JAR integrity.
- **BLOCKED:** `./gradlew --version` exited 1 because no Java executable/JDK is installed.

The Gradle unit tests, lint, Android build, merged-manifest/APK permission check, emulator/device tests, and real inference remain **UNVERIFIED**. The current runner has no Android SDK, emulator, or attached device. Exact environment evidence is recorded in `BUILDING.md`.

## Device acceptance (required before release)

No model/device has been selected or tested. On each supported physical device, record only non-identifying device model, Android version/API, ABI, available memory observation, app build, exact model repository/file/quantization/licence, date, and outcome. Do not report a passing result unless observed.

1. Install APK; verify the no-model state.
2. Import a legally usable compatible GGUF file from the system picker and verify progress/error handling.
3. Load it, generate a response, verify progressive output, stop a longer response, then generate again.
4. Save, reopen, rename, and delete a conversation; force-stop and relaunch to verify persistence and truthful model-memory state.
5. Enable airplane mode and disable Wi-Fi/mobile data; generate another response and reopen history.
6. Try an invalid/unsupported model, then unload/reload, and confirm deletion prompts and in-use restrictions.
7. Repeat generations and note crashes, thermal behavior, and memory concerns.
8. Verify final merged manifest and packaged APK contain no `android.permission.INTERNET`.

Emulator/UI testing cannot substitute for real native inference on a compatible physical device.
