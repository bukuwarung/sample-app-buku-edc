# setup-hilt Specification

## Purpose
TBD - created by archiving change restructure-clean-arch-hilt. Update Purpose after archive.
## Requirements
### Requirement: Hilt Dependencies in Versions Catalog
Hilt versions, libraries, and plugins SHALL be defined in `gradle/libs.versions.toml`.

#### Scenario: Add Hilt to libs.versions.toml
- **Given** `gradle/libs.versions.toml`
- **When** updated
- **Then** it should include `hilt` version, `com.google.dagger:hilt-android` library, and `com.google.dagger.hilt.android` plugin.

### Requirement: Apply Hilt Plugin to Modules
The Hilt Android plugin SHALL be applied to all relevant modules.

#### Scenario: Apply plugin to app, data, ui, and domain
- **Given** `build.gradle.kts` files for each module
- **When** updated
- **Then** they should include `alias(libs.plugins.dagger.hilt.android)`.

### Requirement: Initialize Hilt in Application Class
A custom `Application` class SHALL be created and annotated with `@HiltAndroidApp`.

#### Scenario: Create Hilt Application
- **Given** the `app` module source code
- **When** a new `BukuEdcApplication` class is created
- **Then** it must be annotated with `@HiltAndroidApp` and registered in `AndroidManifest.xml`.

