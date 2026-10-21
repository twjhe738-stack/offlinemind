# Roadmap and gates

Status key: **PASS** means evidence exists; **BLOCKED** means a required environment or prerequisite is missing; **UNVERIFIED** means it has not been tested.

| Phase | Scope | Status | Evidence / gate |
|---|---|---|---|
| 0 | Repository/toolchain audit and stack decision | PASS with environment limits | Workspace was empty. Official Android SAF and llama.cpp Android sample were inspected; details and selected versions are in `BUILDING.md` and `ARCHITECTURE.md`. No prior work existed to overwrite. |
| 1 | Minimal Android/Compose foundation, unit test, CI, permission gate | Implemented; gate BLOCKED | Project files and checks are present, but this runner has no Java, Android SDK, Gradle installation, or wrapper execution capability. Build/test/APK results remain unverified. |
| 2 | Real llama.cpp inference, model loading, streaming, stop, unload | BLOCKED / not implemented | No native toolchain or device/model. The inspected upstream interface has no explicit stop method and logs paths; integration needs a reviewed adapter and cancellation/lifecycle tests. |
| 3 | Chat UI and local conversation storage | Not started | Depends on Phase 2 and persistence design. |
| 4 | Safe SAF model import and management | Not started | Requires GGUF parser behavior, app-private import tests, and native model compatibility work. |
| 5 | Database, settings, conversation UX | Not started | Requires repository and migration tests. |
| 6 | Privacy, security, stability, performance audit | Partially scaffolded; gate UNVERIFIED | No network permission in the source manifest; merged-manifest/APK check is implemented but cannot run without a build. |
| 7 | CI, APK, device acceptance | Configured; gate UNVERIFIED | CI workflow is added. No local APK, emulator, physical device, or real-model test evidence is available. |
| 8 | Optional features | Deferred | Do not begin until the real local-chat path passes earlier gates. |

## Next gates

1. Run the wrapper on a JDK 17 Android build host; resolve compile/lint/test issues and verify the debug APK permission gate.
2. Integrate the pinned upstream candidate only after adapting cancellation and path-safe logging; verify `arm64-v8a` build and actual real-model generation on a device.
3. Keep all device/model claims UNVERIFIED until the documented offline acceptance test has actually run.

No APK, real inference, model recommendation, or device compatibility claim is made by this repository state.
