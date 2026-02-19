# transfer-flow Specification

## Purpose
TBD - created by archiving change add-transfer-flow. Update Purpose after archive.
## Requirements
### Requirement: Provide Transfer flow screen sequence
The system SHALL present the Transfer flow screens in this order: Transfer (Choose Account), Pilih Bank, Rekening Tujuan, Transfer (Insert Card), Transfer (Informasi Kartu), Masukkan PIN, Konfirmasi Transfer, Transfer Berhasil.

#### Scenario: User advances through the flow
- **WHEN** the user taps the primary action button on a Transfer screen
- **THEN** the next screen in the sequence is shown

#### Scenario: Flow completion
- **WHEN** the user taps the primary action button on the Transfer Berhasil screen
- **THEN** the system returns to the Home screen

### Requirement: Use mock data for Transfer flow content

The system SHALL populate transfer details using data retrieved from the SDK via domain use cases,
replacing the previous mock data approach.

#### Scenario: Card info retrieved via SDK

- **WHEN** the user reaches the Informasi Kartu screen
- **THEN** the card details (PAN, expiry date) are retrieved via `AtmFeatures.getCardInfo()`

#### Scenario: Transfer inquiry via SDK

- **WHEN** the user confirms transfer details (account, amount, destination bank, notes)
- **THEN** the system calls `AtmFeatures.transferInquiry()` with `accountId`, `amount`,
  `destinationDetails: BankDetails(bankCode, bankName)`, `notes`, `isCashWithdrawal = false`,
  and `accountType`
- **AND THEN** the `CardReceiptResponse` containing `amount`, `adminFee`, `totalAmount`, and
  `transactionToken` is displayed on the Konfirmasi Transfer screen

#### Scenario: Transfer posting via SDK

- **WHEN** the user confirms the transfer on Konfirmasi Transfer
- **THEN** the system calls `AtmFeatures.transferPosting()` with the `accountId` and
  `transactionToken` obtained from the inquiry step
- **AND THEN** the success screen displays `CardReceiptResponse` data including `rrn`,
  `approvalCode`, `status`, and `totalAmount`

#### Scenario: Transfer token expiration

- **WHEN** the `transactionToken` from inquiry has expired (>15 minutes)
- **THEN** `transferPosting()` throws `TokenExpiredException`
- **AND THEN** the UI shows an error and prompts the user to re-do the inquiry step

#### Scenario: Transfer invalid token

- **WHEN** `transferPosting()` is called with an invalid `transactionToken`
- **THEN** `transferPosting()` throws `InvalidTokenException`
- **AND THEN** the UI shows an error and prompts the user to re-do the inquiry step

#### Scenario: Transfer failure

- **WHEN** `transferInquiry()` or `transferPosting()` fails with a `DeviceSdkException`,
  `BackendException`, `TokenExpiredException`, or `InvalidTokenException`
- **THEN** the UI displays an error state with the error message

### Requirement: Apply MVVM with Hilt per Transfer screen
The system SHALL provide a Hilt-injected ViewModel for each Transfer screen to supply UI state and events.

#### Scenario: ViewModel injection
- **WHEN** a Transfer screen is composed
- **THEN** the corresponding ViewModel is created via Hilt

