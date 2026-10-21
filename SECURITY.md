# Security and privacy

## Guarantees in the current foundation

- The production source manifest declares no permissions, including `android.permission.INTERNET`.
- The foundation contains no runtime networking code, analytics, telemetry, account flow, or remote inference dependency.
- `android:allowBackup="false"` is configured. No app data persistence exists yet.
- The CI permission check examines the source manifest, merged manifest, and packaged APK; it has not yet run because this workspace cannot build an APK.

This is not a security certification. The absence of a permission in source does not by itself prove that a merged or packaged app has no permission. Build dependencies may use network access at development/build time; this does not grant the installed app network access.

## Planned data/file protections

Model files are untrusted data. Future import must use SAF URIs, bounded streaming copy into app-private storage, validated GGUF metadata, safe generated internal names, partial-file cleanup, and deletion guards for active models. Never execute imported files. Conversation storage will use app-private storage; encryption is not implemented and must not be claimed.

## Residual risks and review

Native inference can terminate the process on memory exhaustion. A model filename or GGUF header cannot guarantee device compatibility or safety. The upstream llama.cpp Android sample inspected for a future integration logs local model paths and does not expose a dedicated stop method in its public `InferenceEngine` interface; an adapter must address both before use. Third-party code and transitive permissions need review after integration.

Do not distribute as a release candidate until merged-manifest/APK checks, dependency review, file-import tests, native lifecycle tests, and real offline device tests pass.
