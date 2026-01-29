## ADDED Requirements
### Requirement: Provide Balance Check flow screen sequence
The system SHALL present the Balance Check flow screens in this order: Select Account, Insert/Swipe Card, Card Info, Enter PIN, Balance Summary, Receipt Preview.

#### Scenario: User advances through the flow
- **WHEN** the user taps the primary action button on a Balance Check screen
- **THEN** the next screen in the sequence is shown

#### Scenario: User views receipt preview
- **WHEN** the user taps "Lihat Detail Struk" on the Balance Summary screen
- **THEN** the Receipt Preview screen is shown

#### Scenario: Flow completion
- **WHEN** the user closes the Receipt Preview screen
- **THEN** the system returns to the Home screen

### Requirement: Use mock data for Balance Check content
The system SHALL populate Balance Check summary and receipt details using mock data in the UI layer.

#### Scenario: Balance Summary shows mock balance
- **WHEN** the user reaches the Balance Summary screen
- **THEN** the balance amount and timestamp are rendered from mock data

#### Scenario: Receipt Preview shows mock details
- **WHEN** the user views the Receipt Preview screen
- **THEN** the receipt details are rendered from mock data

### Requirement: Apply MVVM with Hilt per Balance Check screen
The system SHALL provide a Hilt-injected ViewModel for each Balance Check screen to supply UI state and events.

#### Scenario: ViewModel injection
- **WHEN** a Balance Check screen is composed
- **THEN** the corresponding ViewModel is created via Hilt