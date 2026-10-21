# Building and environment

## Selected baseline

The Android project uses JDK 17, Gradle wrapper 8.14.3, Android Gradle Plugin 8.13.2, Kotlin 2.3.0, compileSdk/targetSdk 36, and minSdk 33. The llama.cpp Android sample at commit `4a45de4f2c68f3d144f6c2431ad7c1da7e53c99e` uses JDK 17, Gradle 8.14.3, AGP 8.13.2, Kotlin 2.3.0, compile/target SDK 36, minSdk 33, NDK `29.0.13113456`, CMake `3.31.6`, and ABIs `arm64-v8a` and `x86_64`. This project currently builds no native code; do not install/configure those native tools until Phase 2.

Jetpack Compose libraries are version-aligned by Compose BOM `2026.09.00`, matching the official Compose BOM documentation inspected 2026-10-20. The wrapper distribution is pinned to Gradle 8.14.3 with SHA-256 `bd71102213493060956ec229d946beee57158dbd89d0e62b91bca0fa2c5f3531`.

## Commands

```sh
./gradlew testDebugUnitTest lintDebug assembleDebug
python3 scripts/check_no_internet_permission.py \
  --apk app/build/outputs/apk/debug/app-debug.apk
```

Debug APK, if successfully built, is expected at `app/build/outputs/apk/debug/app-debug.apk`. No signing key is included; release signing must use external secret management and must never be committed.

## Environment observed for this task

| Item | Evidence |
|---|---|
| OS/architecture | Linux PRoot on `aarch64` (`uname -a`) |
| Java/JDK | `java` not found |
| Gradle | `gradle` not found |
| Android SDK / NDK | `ANDROID_HOME`, `ANDROID_SDK_ROOT`, and `ANDROID_NDK_HOME` unset; `sdkmanager` not found |
| CMake / Ninja / adb | Not found on PATH |
| Repository state | Workspace was empty and not a Git repository; no prior project files existed |
| Network | Public GitHub and Android documentation were reachable during research. Gradle dependency resolution was not tested. |
| Emulator/device | No emulator/device tooling detected; no physical device is attached or verified. |
| APK | Not built; no artifact exists or is claimed. |

## Upstream integration references

- Official Android guide: <https://github.com/ggml-org/llama.cpp/blob/4a45de4f2c68f3d144f6c2431ad7c1da7e53c99e/docs/android.md>
- Official sample: <https://github.com/ggml-org/llama.cpp/tree/4a45de4f2c68f3d144f6c2431ad7c1da7e53c99e/examples/llama.android>
- Official Android SAF guide: <https://developer.android.com/training/data-storage/shared/documents-files>
- Official Compose BOM guide: <https://developer.android.com/develop/ui/compose/bom>

## Release signing

Use a separately managed keystore and CI secrets only for trusted release workflows. Do not put keystores, passwords, or local signing configuration in source control. Debug builds are not signed release artifacts.
