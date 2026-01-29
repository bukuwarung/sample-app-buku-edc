## ADDED Requirements
### Requirement: Delegate Home action clicks to view model
The system SHALL route Home action item clicks to the Home view model with the action identifier or label.

#### Scenario: User taps a Home action item
- **WHEN** the user taps a Home action tile
- **THEN** the Home view model receives the corresponding action identifier or label

### Requirement: Show action-specific toast feedback
The system SHALL display a toast message that reflects the specific action tapped, based on a view model UI event.

#### Scenario: View model emits toast event
- **WHEN** the Home view model emits a toast event for a specific action
- **THEN** the UI shows a toast containing that action’s label