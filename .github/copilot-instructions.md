# OfflineMind project instructions

- Preserve the strict offline requirement: production merged manifest and APK must not request `android.permission.INTERNET`. No cloud inference, model download, telemetry, analytics, accounts, or remote fallback.
- Do not claim model inference, model/device compatibility, build/test success, or APK availability without actual evidence.
- Keep app layers separated: Compose UI, domain orchestration, local repositories/storage, SAF integration, inference abstraction, and native implementation.
- Model imports are untrusted data. Use SAF URIs and streaming copies to app-private storage; never use user filenames as paths or delete the original source.
- Serialize native model operations, define ownership/release, and test cancellation and repeat-generation behavior before exposing controls.
- Keep GGUF weights out of Git and APKs. Record base-model and conversion/quantization licences separately.
- Run `./gradlew testDebugUnitTest lintDebug assembleDebug` and `python3 scripts/check_no_internet_permission.py --apk app/build/outputs/apk/debug/app-debug.apk` when the Android toolchain is available.
- Keep `README.md`, `ARCHITECTURE.md`, `ROADMAP.md`, `TESTING.md`, `SECURITY.md`, `MODEL_LICENSES.md`, `BUILDING.md`, and `CHANGELOG.md` accurate as implementation changes.
