## ADDED Requirements
### Requirement: Navigate to Balance Check flow from Home
The system SHALL emit a navigation event from the Home view model when the Cek Saldo action is tapped.

#### Scenario: User taps Cek Saldo action item
- **WHEN** the user taps the Cek Saldo action tile
- **THEN** the UI receives a navigation event to open the Balance Check flow

## MODIFIED Requirements
### Requirement: Show action-specific toast feedback
The system SHALL display a toast message for Home actions that do not trigger navigation (excluding Transfer and Cek Saldo) based on a view model UI event.

#### Scenario: View model emits toast event for non-navigation action
- **WHEN** the Home view model emits a toast event for a non-navigation action
- **THEN** the UI shows a toast containing that action’s label

#### Scenario: Transfer action selected
- **WHEN** the user taps the Transfer action tile
- **THEN** no toast is shown for that action

#### Scenario: Cek Saldo action selected
- **WHEN** the user taps the Cek Saldo action tile
- **THEN** no toast is shown for that action