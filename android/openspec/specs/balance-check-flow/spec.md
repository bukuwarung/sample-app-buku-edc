# balance-check-flow Specification

## Purpose
TBD - created by archiving change add-balance-check-flow. Update Purpose after archive.
## Requirements
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

### Requirement: Use mock data for Balance Check content

The system SHALL populate Balance Check summary and receipt details using data retrieved from the
SDK via domain use cases, replacing the previous mock data approach.

#### Scenario: Card info retrieved via SDK

- **WHEN** the user reaches the Informasi Kartu screen in Balance Check flow
- **THEN** the card details (PAN, expiry date) are retrieved via `AtmFeatures.getCardInfo()`

#### Scenario: Balance check execution via SDK

- **WHEN** the user initiates a balance check after PIN entry
- **THEN** the system calls `AtmFeatures.checkBalance()` with `accountId` and `accountType`
  (SDK 0.1.3: `sourceDetails: BankDetails` parameter was removed)

#### Scenario: Balance Summary shows SDK balance

- **WHEN** `checkBalance()` returns successfully
- **THEN** the Balance Summary screen displays `CardReceiptResponse.totalAmount` as the balance
  and `CardReceiptResponse.timestamp` as the inquiry time

#### Scenario: Receipt Preview shows SDK details

- **WHEN** the user views the Receipt Preview screen
- **THEN** the receipt displays `cardNumber`, `cardHolderName`, `bankName`, `rrn`, `totalAmount`,
  and `accountType` from the `CardReceiptResponse`

#### Scenario: Balance check failure

- **WHEN** `checkBalance()` fails with a `DeviceSdkException` or `BackendException`
- **THEN** the UI displays an error state with the mapped error message and retry option

### Requirement: Apply MVVM with Hilt per Balance Check screen
The system SHALL provide a Hilt-injected ViewModel for each Balance Check screen to supply UI state and events.

#### Scenario: ViewModel injection
- **WHEN** a Balance Check screen is composed
- **THEN** the corresponding ViewModel is created via Hilt

