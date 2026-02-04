## ADDED Requirements

### Requirement: Provide Transaction History Screen

The system SHALL display a Transaction History screen showing a list of past transactions retrieved
from the SDK.

#### Scenario: View transaction history with data

- **WHEN** the user navigates to the Transaction History screen
- **THEN** the system displays a list of past transactions
- **AND THEN** each item shows transaction type, amount, date, and status

#### Scenario: View transaction history when empty

- **WHEN** the user navigates to the Transaction History screen and no transactions exist
- **THEN** the system displays an empty state message

#### Scenario: Transaction history loading state

- **WHEN** the Transaction History screen is loading data from SDK
- **THEN** the system displays a loading indicator

#### Scenario: Transaction history error state

- **WHEN** the SDK returns an error while fetching history
- **THEN** the system displays an error state with retry option

### Requirement: Navigate to Transaction History from Home

The system SHALL navigate to the Transaction History screen when the user selects the "Riwayat"
action tile on the Home screen.

#### Scenario: User taps Riwayat action

- **WHEN** the user taps the "Riwayat" action tile on the Home screen
- **THEN** the Transaction History screen is displayed

### Requirement: Display Transaction Details

The system SHALL display transaction details including type (Transfer, Balance Check, Withdrawal),
amount, timestamp, destination/source info, and status.

#### Scenario: Transfer transaction details

- **WHEN** viewing a Transfer transaction in history
- **THEN** the system shows destination bank, account number (masked), amount, and status

#### Scenario: Balance Check transaction details

- **WHEN** viewing a Balance Check transaction in history
- **THEN** the system shows account type, balance amount, and timestamp

#### Scenario: Withdrawal transaction details

- **WHEN** viewing a Withdrawal transaction in history
- **THEN** the system shows withdrawal amount, account, and status

### Requirement: Retrieve Transaction History via SDK

The system SHALL use the SDK's history API to retrieve transaction records through a repository in
the data module.

#### Scenario: Repository fetches history from SDK

- **WHEN** the History use case requests transaction history
- **THEN** the repository calls the SDK history API
- **AND THEN** returns a list of transaction records wrapped in Result type

### Requirement: Apply MVVM with Hilt for Transaction History Screen

The system SHALL provide a Hilt-injected ViewModel for the Transaction History screen to manage UI
state and events.

#### Scenario: ViewModel injection

- **WHEN** the Transaction History screen is composed
- **THEN** the HistoryViewModel is created via Hilt
