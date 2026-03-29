# CLAUDE.md

This file provides guidance for AI assistants working in this repository.

## Project Overview

**Zone** is a Kotlin Multiplatform application targeting Android, iOS, Desktop (JVM), and Web (Wasm/JS), with a Ktor backend server. The app is intended as a task management and reminder tool.

- Group/package: `com.greenmiststudios.zone`
- Kotlin version: 2.2.21
- Compose Multiplatform version: 1.9.3

## Repository Structure

```
zone/
├── composeApp/          # Shared Compose UI for Android, iOS, Desktop, Web
├── server/              # Ktor backend server (JVM-only)
├── shared/              # Shared business logic (pure Kotlin Multiplatform)
├── iosApp/              # iOS SwiftUI wrapper for Compose
├── gradle/              # Gradle wrapper and version catalog (libs.versions.toml)
├── scripts/             # build-number.sh for CI build numbering
└── .github/workflows/   # CI/CD pipelines
```

### Module Responsibilities

| Module | Purpose | Targets |
|--------|---------|---------|
| `shared` | Platform-agnostic business logic; expect/actual platform abstractions | All platforms |
| `composeApp` | Compose Multiplatform UI and platform entry points | Android, iOS, JVM, JS, WasmJS |
| `server` | Ktor HTTP server | JVM only |
| `iosApp` | SwiftUI app entry, embeds ComposeApp framework | iOS only |

## Build System

The project uses **Gradle** (9.2.1) with **Kotlin DSL**. Version catalog is at `gradle/libs.versions.toml`.

### Common Build Commands

```bash
# Run all tests and linting (required before pushing)
./gradlew check

# Android
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:assembleRelease

# Desktop (JVM)
./gradlew :composeApp:run

# Web
./gradlew :composeApp:wasmJsBrowserDevelopmentRun   # Wasm (preferred)
./gradlew :composeApp:jsBrowserDevelopmentRun        # Legacy JS

# Server
./gradlew :server:run

# Code formatting
./gradlew spotlessApply
```

## Code Style & Formatting

- **Formatter:** Spotless with KTLint — run `./gradlew spotlessApply` before committing.
- **Line endings:** LF
- **Indent:** 2 spaces
- **Max line length:** 100 characters
- **Charset:** UTF-8
- **Kotlin code style:** Official (set in `gradle.properties`)

KTLint is applied to all Kotlin source files and Gradle build scripts via Spotless. Always run `./gradlew check` (which includes Spotless verification) before pushing; CI will fail if formatting is incorrect.

## Multiplatform Conventions

### Expect/Actual Pattern

Platform-specific code uses Kotlin's `expect`/`actual` mechanism. The `expect` declaration lives in `commonMain` and each platform provides an `actual` implementation:

```
shared/
  src/
    commonMain/kotlin/.../Platform.kt          # expect fun getPlatform(): Platform
    androidMain/kotlin/.../Platform.android.kt # actual
    iosMain/kotlin/.../Platform.ios.kt         # actual
    jvmMain/kotlin/.../Platform.jvm.kt         # actual
    jsMain/kotlin/.../Platform.js.kt           # actual
    wasmJsMain/kotlin/.../Platform.wasmJs.kt   # actual
```

When adding platform-specific code, always define it in `commonMain` via expect/actual. Never add platform-only logic that belongs in `shared` directly into `composeApp`.

### Source Set Naming

| Source Set | Purpose |
|------------|---------|
| `commonMain` | Shared code for all targets |
| `commonTest` | Tests that run on all targets |
| `androidMain` | Android-specific implementations |
| `iosMain` | iOS-specific implementations |
| `jvmMain` | Desktop JVM-specific implementations |
| `jsMain` | Kotlin/JS web implementations |
| `wasmJsMain` | Kotlin/Wasm web implementations (preferred over jsMain) |

## Testing

Tests use **Kotlin Test** (multiplatform) and **JUnit 4** (server).

- `commonTest` — multiplatform unit tests, run on all targets
- `server/src/test/kotlin/` — Ktor integration tests using `testApplication`

Run all tests:
```bash
./gradlew check
```

### Server Testing Example

Use Ktor's `testApplication` block for integration tests. See `ApplicationTest.kt` as a reference.

## Server

The Ktor server lives in `:server` and shares code from `:shared`.

- **Port:** 8080 (defined in `shared/src/commonMain/kotlin/.../Constants.kt`)
- **Routes:** Currently only `GET /` returning platform info
- **Features:** Configured in `Application.kt`

## CI/CD

Three GitHub Actions workflows in `.github/workflows/`:

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `check.yml` | push to main, PRs | Runs `./gradlew check` (tests + formatting) |
| `android.yml` | push to main | Builds debug and release APKs, uploads artifacts |
| `build-number.yml` | reusable | Tags commits with `build-number-N` for sequential build numbers |

CI uses JDK 21 (Zulu) and an encrypted Gradle cache (`GRADLE_ENCRYPTION_KEY` secret).

## Dependency Management

All library versions are centralized in `gradle/libs.versions.toml`. When adding a new dependency:

1. Add the version to `[versions]`
2. Add the library declaration to `[libraries]`
3. Reference it in the relevant `build.gradle` file as `libs.<alias>`

Do not hardcode version strings in build scripts.

## JVM / Memory Settings

Defined in `gradle.properties`:
- Gradle JVM max heap: 4096 MB
- Kotlin daemon max heap: 4096 MB
- Configuration cache: enabled (with problems as warnings)
- Build cache: enabled

## iOS

The `iosApp/` directory contains a minimal SwiftUI wrapper. The actual UI is rendered via Compose Multiplatform through the `MainViewController` from `composeApp/iosMain`. Avoid adding business logic in Swift; keep it in Kotlin.

## Key Conventions Summary

- All shared logic goes in `:shared` (or `commonMain` of `:composeApp` for UI-level shared code)
- Use expect/actual for platform differences, never `if (platform == ...)` checks
- Format with `./gradlew spotlessApply` before committing
- Run `./gradlew check` before pushing — this is what CI runs
- Versions go in `gradle/libs.versions.toml`, not inline in build files
- Server port and other cross-cutting constants belong in `:shared`
