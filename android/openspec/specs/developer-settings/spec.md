# developer-settings Specification

## Purpose

This specification defines the developer-only configurations and persistence mechanisms used for
debugging and simulating app states.

## Requirements

### Requirement: Persist "First Time User" status

The system SHALL store the "First Time User" status in a persistent storage that survives app
restarts.

#### Scenario: Default state

- **WHEN** the app is launched for the first time
- **THEN** `CheckIsFirstTimeUserUseCase` SHOULD return `true`

#### Scenario: Updating state

- **WHEN** the developer toggles "First Time User" to `false` in developer settings
- **THEN** subsequent calls to `CheckIsFirstTimeUserUseCase` SHOULD return `false`

### Requirement: Provide Developer Settings Screen

The system SHALL provide a screen accessible only to developers (via shortcut) to modify debug
settings.

#### Scenario: View Developer Settings

- **WHEN** the developer opens the Developer Settings screen
- **THEN** they SHOULD see a toggle for "First Time User" reflecting the current state in storage

### Requirement: Provide App Shortcut for Developer Settings

The system SHALL provide an Android App Shortcut to jump directly to the Developer Settings screen.

#### Scenario: Launch from Shortcut

- **WHEN** the user long-presses the app icon and selects "Developer Settings"
- **THEN** the app SHOULD launch and navigate directly to the Developer Settings screen

