# Buku EDC Sample App

Sample Android application demonstrating how to integrate the **Buku EDC SDK** into a partner app. This project serves as a working reference for partner developers building EDC-based transaction features (balance check, transfer, cash withdrawal, transaction history).

## Prerequisites

- **Android Studio** Hedgehog (2023.1.1) or later
- **Android API 23+** (minSdk 23)
- **Kotlin** 1.9.0+
- **Java 11** (compile target)
- **Gradle** with version catalog (`libs.versions.toml`)
- **Buku EDC SDK** `0.1.3-SNAPSHOT` (included as project dependency)

## Project Structure

```
android/
├── app/          # Application entry point (BukuEdcApplication, MainActivity)
├── data/         # SDK integration layer (repositories, SdkInitializer, Hilt modules)
├── domain/       # Business interfaces and models (no SDK dependency)
├── ui/           # Jetpack Compose screens and ViewModels
└── sdk/          # Buku EDC SDK AAR
```

The app follows **Clean Architecture** with a strict dependency rule: `app` → `ui` → `domain` ← `data`. The `domain` module has no dependency on the SDK — all SDK types are mapped in the `data` layer.

## Getting Started

### 1. Clone and Open

```bash
git clone <repository-url>
cd sample-app-buku-edc/android
```

Open the `android/` directory in Android Studio.

### 2. SDK Key Configuration

The SDK key is configured in `data/build.gradle.kts`:

```kotlin
buildConfigField("String", "SDK_KEY", "\"sandbox-test-sdk-key-sample\"")
```

Partners: replace `sandbox-test-sdk-key-sample` with your SDK key provided by Buku.

### 3. Test Environment

The sample app uses **SANDBOX** environment with `testingMock = true`, enabling mock responses without a physical EDC device. See `SdkInitializer.kt` for configuration.

Test credentials are configured in the app's **Developer Settings** screen (accessible from the home screen gear icon):
- **Account ID**: Your sandbox account UUID
- **Access Token**: Obtained from the token exchange API
  `POST https://api-dev.bukuwarung.com/sdk/v1/token/exchange`
  with `partnerId`, `partnerSecret`, and `partnerUserToken`

### 4. Build and Run

```bash
./gradlew :app:assembleDebug
```

Or build directly from Android Studio.

## Integration Guide

For detailed SDK integration documentation covering architecture, transaction flows, error handling, and event monitoring, see **[INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)**.

## SDK Reference

For the SDK's own API documentation, see [`sdk/buku-edc-sdk/SDK_USAGE_GUIDE.md`](../sdk/buku-edc-sdk/SDK_USAGE_GUIDE.md).
