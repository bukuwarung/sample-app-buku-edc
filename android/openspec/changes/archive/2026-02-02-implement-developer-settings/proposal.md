# Proposal: Implement Developer Settings

## Summary

Implement a developer-only settings screen to manually toggle the first-time user status and other
debug configurations. The status will be persisted using Jetpack DataStore in the `data` module. The
screen will be accessible via an App Shortcut.

## Motivation

Currently, the `CheckIsFirstTimeUserUseCase` returns a hardcoded value. Developers need a way to
test both the first-time user flow and the returning user flow without modifying code.

## Proposed Changes

1. **Domain Layer**:
    - Update `SettingsRepository` to include `isFirstTimeUser` and `setIsFirstTimeUser`.
2. **Data Layer**:
    - Implement `DataStoreSettingsRepository` using Jetpack DataStore.
    - Setup Hilt binding for the new repository.
3. **UI Layer**:
    - Create a `DeveloperSettingsScreen` with a toggle for "First Time User".
    - Create a `DeveloperSettingsViewModel`.
    - Add navigation for the new screen.
4. **App Layer**:
    - Add an App Shortcut to launch the Developer Settings screen directly.

## Constraints

- Follow clean architecture principles (repository interface in `domain`, implementation in `data`).
- Use Jetpack DataStore for persistence.
- Minimal implementation first.
