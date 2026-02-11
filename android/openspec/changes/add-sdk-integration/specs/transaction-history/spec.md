## ADDED Requirements

### Requirement: Provide Transaction History Screen

The system SHALL display a Transaction History screen showing a paginated list of past transactions
retrieved from the SDK via `AtmFeatures.getTransactionHistory()`.

#### Scenario: View transaction history with data

- **WHEN** the user navigates to the Transaction History screen
- **THEN** the system calls `getTransactionHistory()` with a `TransactionFilter(accountId,
  pageNumber = 0, pageSize = 20)`
- **AND THEN** each `HistoryItem` in the response displays `transactionId`, `amount`, `status`,
  `date`, and `type`

#### Scenario: View transaction history when empty

- **WHEN** the user navigates to the Transaction History screen and
  `TransactionHistory.history` is empty
- **THEN** the system displays an empty state message

#### Scenario: Transaction history loading state

- **WHEN** the Transaction History screen is loading data from SDK
- **THEN** the system displays a loading indicator

#### Scenario: Transaction history error state

- **WHEN** `getTransactionHistory()` fails with an SDK exception
- **THEN** the system displays an error state with retry option

### Requirement: Support Pagination for Transaction History

The system SHALL support paginated retrieval of transaction history using `TransactionFilter`
page parameters and `PaginationDetails` from the response.

#### Scenario: Load next page

- **WHEN** the user scrolls to the end of the history list and `PaginationDetails.currentPage`
  is less than `PaginationDetails.totalPage`
- **THEN** the system calls `getTransactionHistory()` with incremented `pageNumber`
- **AND THEN** the new items are appended to the existing list

#### Scenario: Last page reached

- **WHEN** `PaginationDetails.currentPage` equals `PaginationDetails.totalPage`
- **THEN** the system does not request additional pages

### Requirement: Navigate to Transaction History from Home

The system SHALL navigate to the Transaction History screen when the user selects the "Riwayat"
action tile on the Home screen.

#### Scenario: User taps Riwayat action

- **WHEN** the user taps the "Riwayat" action tile on the Home screen
- **THEN** the Transaction History screen is displayed

### Requirement: Retrieve Transaction History via SDK

The system SHALL use `AtmFeatures.getTransactionHistory(filter: TransactionFilter)` through a
repository in the data module to retrieve transaction records.

#### Scenario: Repository fetches history from SDK

- **WHEN** the History use case requests transaction history
- **THEN** the repository calls `AtmFeatures.getTransactionHistory()` with the provided
  `TransactionFilter`
- **AND THEN** returns the `TransactionHistory` containing `ArrayList<HistoryItem>` and
  `PaginationDetails`

### Requirement: Apply MVVM with Hilt for Transaction History Screen

The system SHALL provide a Hilt-injected ViewModel for the Transaction History screen to manage UI
state, pagination, and error handling.

#### Scenario: ViewModel injection

- **WHEN** the Transaction History screen is composed
- **THEN** the HistoryViewModel is created via Hilt
