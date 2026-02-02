# Design: Developer Settings and DataStore Integration

## Architecture

### Data Layer Persistence

We will use **Jetpack DataStore (Preferences)** for simple key-value persistence. This is preferred
over SharedPreferences as it handles data updates off the UI thread and provides a reactive API.

- **Storage**: `data/src/main/java/com/bukuwarung/edc/data/settings/DataStoreSettingsRepository.kt`
- **Keys**:
    - `is_first_time_user`: Boolean (Default: true)

### Dependency Injection

- `SettingsRepository` will be bound to `DataStoreSettingsRepository` in a Hilt module within the
  `data` module.
- `DataStore` instance will be provided via Hilt.

### Developer Screen

- **Location**: `ui/src/main/java/com/bukuwarung/edc/ui/developer/DeveloperSettingsScreen.kt`
- **UI Components**:
    - A simple list of toggles/inputs.
    - Initial toggle: "Simulate First Time User".

### App Shortcut

- **Shortcut ID**: `developer_settings`
- **Target**: `MainActivity` with a specific intent extra or a dedicated transparent activity that
  handles navigation to the developer screen. Given the current `MainNavigation` structure, we will
  use an intent extra to trigger navigation to the developer screen upon app launch.

## Data Flow

1. User toggles a setting in `DeveloperSettingsScreen`.
2. `DeveloperSettingsViewModel` calls `SettingsRepository.setIsFirstTimeUser()`.
3. `DataStoreSettingsRepository` updates the DataStore.
4. `CheckIsFirstTimeUserUseCase` (used by `HomeScreen`) reads from
   `SettingsRepository.isFirstTimeUser()`.
5. UI updates reactively if using Flows, or upon next invocation.
