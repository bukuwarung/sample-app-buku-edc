# Tasks: Restructure and Hilt Setup

## Module Restructuring
- [x] Add `domain` dependency to `data/build.gradle.kts`
- [x] Add `domain` dependency to `ui/build.gradle.kts`
- [x] Add `data`, `domain`, and `ui` dependencies to `app/build.gradle.kts`
- [x] Sync project to verify dependency graph

## Hilt Setup
- [x] Add Hilt to `gradle/libs.versions.toml`
  - [x] Add version `2.51.1` (or latest stable)
  - [x] Add `hilt-android` and `hilt-compiler` libraries
  - [x] Add `dagger-hilt-android` plugin
- [x] Add Hilt plugin to root `build.gradle.kts`
- [x] Apply Hilt plugin to `app`, `data`, `domain`, and `ui` modules
- [x] Add Hilt dependencies to `app`, `data`, `domain`, and `ui` modules
- [x] Create `com.bukuwarung.edc.sample.BukuEdcApplication` in `app` module
- [x] Annotate `BukuEdcApplication` with `@HiltAndroidApp`
- [x] Register `BukuEdcApplication` in `app/src/main/AndroidManifest.xml`
- [x] Sync project and build to verify Hilt configuration
