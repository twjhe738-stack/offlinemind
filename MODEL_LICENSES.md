# Model, engine, and dependency licences

No model weights, converted model, or quantization are bundled, downloaded, or recommended in the current foundation. The app does not contain GGUF assets. Users will need to obtain a model separately after import support is implemented; its licence and any conversion/quantization terms must be assessed independently.

| Component | Purpose / version | Licence and review status |
|---|---|---|
| Model weights | None selected or included | Not applicable yet; record exact source, file, quantization, and terms per model before recommendation/distribution. |
| GGUF conversion/quantization | None selected or included | Not applicable yet; conversion terms can differ from base model terms. |
| llama.cpp Android binding | Investigated, not vendored or linked; candidate `ggml-org/llama.cpp` commit `4a45de4f2c68f3d144f6c2431ad7c1da7e53c99e` | Upstream project is MIT-licensed. Recheck exact notices and bundled third-party licences before integration. |
| AndroidX Compose and Activity Compose | UI/activity APIs; Compose BOM `2026.09.00`, Activity Compose `1.12.2` | AndroidX libraries are Apache-2.0 licensed. Resolved dependency graph/transitives are not yet available and require release review. |
| Kotlin / Compose plugin | Kotlin and Compose compilation; `2.3.0` | Build-time tooling; verify resolved notices as part of release dependency review. |
| Android Gradle Plugin | Android build; `8.13.2` | Build-time tooling; verify resolved notices as part of release dependency review. |
| Gradle wrapper | Gradle `8.14.3` | Wrapper scripts and wrapper runtime are Apache-2.0 licensed. |
| JUnit | Unit tests; `4.13.2` | JUnit 4 is EPL 1.0; test-only, not packaged in the production app. |

This is a preliminary register, not a completed release dependency audit. Dependency resolution and transitive permission/licence review have not run in this environment.
