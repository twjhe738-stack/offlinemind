# Architecture

## Current state

This is a single `:app` Android application using Kotlin and Jetpack Compose. The current UI is a static foundation screen. `GenerationStateMachine` defines and tests legal generation-state transitions; no production inference path calls it yet. `LlmManager` is an unconnected MediaPipe streaming adapter; there is no database, model registry, or importer.

The application manifest deliberately declares no permissions. In particular, no `INTERNET` permission is present in the source manifest. `allowBackup=false` is set because local conversations/models are intended to remain on-device; persistence is not implemented yet.

## Intended boundaries

Keep Compose/UI separate from domain orchestration, local data, Android storage, and the inference implementation. The inference boundary should own model load/unload and single-context serialization, expose failures and incremental output, and define a stop operation whose native behavior has been verified. `LlmManager` serializes collections per instance and closes its native inference task when the stream ends or is cancelled. MediaPipe 0.10.14 exposes no dedicated stop-generation method through this adapter; cancellation behavior and repeated native generations remain unverified. Do not wire it to UI controls until those behaviors are tested. The UI must not call JNI directly.

For model import, use the Storage Access Framework and copy from the selected `content://` URI into app-private storage using bounded buffers. Do not retain a user-controlled filename as a filesystem path. Record the imported model only after a complete, validated copy.

Persist conversations/messages and small settings locally. On restart, the selected model may be present on disk but native model memory is not; model state must be reported as imported/unloaded until explicitly reloaded. Do not claim encryption unless it is implemented and verified.

## Inference integration investigation

The official `ggml-org/llama.cpp` Android sample was inspected at commit `4a45de4f2c68f3d144f6c2431ad7c1da7e53c99e` (2026-10-20). Its documented Android sample uses minSdk 33, compileSdk/targetSdk 36, JDK 17, NDK `29.0.13113456`, CMake `3.31.6`, and ABIs `arm64-v8a` and `x86_64`. Its Gradle files use AGP `8.13.2`, Kotlin `2.3.0`, and Gradle `8.14.3`.

The sample provides `AiChat.getInferenceEngine(context)`, `InferenceEngine.sendUserPrompt(...): Flow<String>`, and `GgufMetadataReader`. It sends a raw prompt for the engine's template formatting. The inspected interface has no dedicated stop-generation API; its implementation uses an internal cancellation flag and serialized coroutine dispatcher. It also logs model paths and exceptions. These behaviors require a small reviewed adapter/redaction layer and explicit cancellation tests before reuse. The sample is a candidate, not yet an app dependency or verified production integration.

## Compatibility scope

The configured Android baseline is API 33+, compile/target API 36. This follows the inspected candidate's minimum SDK, not a verified target device. Planned native ABI support is not yet included or built. No physical device, model, or runtime inference has been tested.

| Target | API / ABI | Evidence status |
|---|---|---|
| OfflineMind app shell | min API 33; compile/target API 36; ABI not restricted while app contains no native code | Configuration exists; APK/build is UNVERIFIED. |
| llama.cpp Android sample candidate | min API 33; compile/target API 36; `arm64-v8a` and `x86_64` in its inspected Gradle config | Source configuration inspected at the pinned commit; not built or tested in this project. |
| OfflineMind with native inference | Intended to start with `arm64-v8a`; x86_64 only after independent build/device checks | UNVERIFIED; no app ABI support is claimed. |

There is no verified target phone or compatibility guarantee. Do not interpret the sample's configured ABIs as tested OfflineMind support.
