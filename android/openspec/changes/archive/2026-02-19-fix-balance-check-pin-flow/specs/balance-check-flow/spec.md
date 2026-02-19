## MODIFIED Requirements
### Requirement: Provide Balance Check flow screen sequence
The system SHALL present the Balance Check flow screens in this order: Select Account, Insert/Swipe Card, Card Info, Balance Summary, Receipt Preview. The Enter PIN step is removed from the app-level flow because the SDK's `AtmFeatures.checkBalance()` handles PIN entry internally via the physical terminal keypad.

#### Scenario: User advances through the flow
- **WHEN** the user taps the primary action button on a Balance Check screen
- **THEN** the next screen in the sequence is shown (skipping the app PIN screen)

#### Scenario: PIN entry handled by SDK
- **WHEN** the user reaches the Balance Summary screen after Card Info
- **THEN** the SDK's `checkBalance()` call handles card reading and PIN entry internally
- **AND THEN** no app-level PIN screen is displayed

#### Scenario: User views receipt preview
- **WHEN** the user taps "Lihat Detail Struk" on the Balance Summary screen
- **THEN** the Receipt Preview screen is shown

#### Scenario: Flow completion
- **WHEN** the user closes the Receipt Preview screen
- **THEN** the system returns to the Home screen
