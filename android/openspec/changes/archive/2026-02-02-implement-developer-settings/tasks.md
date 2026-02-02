# Tasks: Implement Developer Settings

## Preparation

- [x] Add DataStore dependency to `data/build.gradle.kts`

## Domain Layer

- [x] Update `SettingsRepository` interface to include `isFirstTimeUser` (Flow/suspend) and
  `setIsFirstTimeUser`
- [x] Update `CheckIsFirstTimeUserUseCase` to use `SettingsRepository`

## Data Layer

- [x] Implement `DataStoreSettingsRepository` in `data` module
- [x] Create Hilt module to provide DataStore and bind `SettingsRepository` to
  `DataStoreSettingsRepository` (replacing or co-existing with `FakeSettingsRepository`)

## UI Layer

- [x] Create `DeveloperSettingsViewModel`
- [x] Create `DeveloperSettingsScreen` in `ui` module
- [x] Add `DeveloperSettings` to `Screen` sealed class in `Routes.kt`
- [x] Add navigation entry in `MainNavigation` in `MainActivity.kt`

## App Layer

- [x] Create `shortcuts.xml` in `app/src/main/res/xml/`
- [x] Register shortcuts in `AndroidManifest.xml`
- [x] Handle shortcut intent in `MainActivity` to navigate to Developer Settings
