# home-mvvm Specification

## Purpose
TBD - created by archiving change scaffold-home-mvvm. Update Purpose after archive.
## Requirements
### Requirement: Delegate Home action clicks to view model
The system SHALL route Home action item clicks to the Home view model with the action identifier or label.

#### Scenario: User taps a Home action item
- **WHEN** the user taps a Home action tile
- **THEN** the Home view model receives the corresponding action identifier or label

### Requirement: Show action-specific toast feedback

The system SHALL display a toast message for Home actions that do not trigger navigation (excluding
Transfer, Cek Saldo, Tarik Tunai, and Pengaturan) based on a view model UI event.

#### Scenario: View model emits toast event for non-navigation action
- **WHEN** the Home view model emits a toast event for a non-navigation action
- **THEN** the UI shows a toast containing that action’s label

#### Scenario: Transfer action selected
- **WHEN** the user taps the Transfer action tile
- **THEN** no toast is shown for that action

#### Scenario: Cek Saldo action selected
- **WHEN** the user taps the Cek Saldo action tile
- **THEN** no toast is shown for that action

#### Scenario: Tarik Tunai action selected

- **WHEN** the user taps the Tarik Tunai action tile
- **THEN** no toast is shown for that action

#### Scenario: Pengaturan action selected
- **WHEN** the user taps the Pengaturan action tile
- **THEN** no toast is shown for that action

### Requirement: Navigate to Transfer flow from Home
The system SHALL emit a navigation event from the Home view model when the Transfer action is tapped.

#### Scenario: User taps Transfer action item
- **WHEN** the user taps the Transfer action tile
- **THEN** the UI receives a navigation event to open the Transfer flow

### Requirement: Navigate to Balance Check flow from Home
The system SHALL emit a navigation event from the Home view model when the Cek Saldo action is tapped.

#### Scenario: User taps Cek Saldo action item
- **WHEN** the user taps the Cek Saldo action tile
- **THEN** the UI receives a navigation event to open the Balance Check flow

### Requirement: Navigate to Settings flow from Home

The system SHALL emit a navigation event from the Home view model when the Pengaturan action is
tapped.

#### Scenario: User taps Pengaturan action item

- **WHEN** the user taps the Pengaturan action tile
- **THEN** the UI receives a navigation event to open the Settings flow

### Requirement: Navigate to Cash Withdrawal flow from Home

The system SHALL emit a navigation event from the Home view model when the Tarik Tunai action is
tapped.

#### Scenario: User taps Tarik Tunai action item

- **WHEN** the user taps the Tarik Tunai action tile
- **THEN** the UI receives a navigation event to open the Cash Withdrawal flow

