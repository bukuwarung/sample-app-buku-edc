## ADDED Requirements

### Requirement: Navigate to Transaction History flow from Home

The system SHALL emit a navigation event from the Home view model when the Riwayat action is tapped.

#### Scenario: User taps Riwayat action item

- **WHEN** the user taps the "Riwayat" action tile
- **THEN** the UI receives a navigation event to open the Transaction History flow

## MODIFIED Requirements

### Requirement: Show action-specific toast feedback

The system SHALL display a toast message for Home actions that do not trigger navigation (excluding
Transfer, Cek Saldo, Tarik Tunai, Pengaturan, and Riwayat) based on a view model UI event.

#### Scenario: View model emits toast event for non-navigation action

- **WHEN** the Home view model emits a toast event for a non-navigation action
- **THEN** the UI shows a toast containing that action's label

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

#### Scenario: Riwayat action selected

- **WHEN** the user taps the Riwayat action tile
- **THEN** no toast is shown for that action
