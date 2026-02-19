# cash-withdrawal-flow Specification

## Purpose

TBD - created by archiving change add-cash-withdrawal-from-home. Update Purpose after archive.
## Requirements
### Requirement: Provide Cash Withdrawal flow screen sequence

The system SHALL present the Cash Withdrawal flow screens in this order (per Figma “Cash
withdrawal” node `33:2198`):

1. First-time prompt: “Anda Belum Memiliki Rekening Tujuan” (when user is first time)
2. Tarik Tunai (Pilih Akun)
3. Tarik Tunai (Masukkan atau Gesek Kartu)
4. Tarik Tunai (Informasi Kartu)
5. Masukkan PIN
6. Konfirmasi
7. Status Berhasil (print preview)
8. Receipt preview

#### Scenario: User advances through the flow

- **WHEN** the user taps the primary action button on a Cash Withdrawal screen
- **THEN** the next screen in the sequence is shown

#### Scenario: Flow completion

- **WHEN** the user completes the flow and selects the close action on the success screen
- **THEN** the system returns to the Home screen

### Requirement: Handle “First Time User” decision for Cash Withdrawal

The system SHALL follow the “First Time User” decision shown in the Cash Withdrawal Figma flowchart
(`33:2198`) by presenting the first-time prompt screen before the main flow when the user has not
yet added a destination bank account.

#### Scenario: First-time user enters Cash Withdrawal

- **WHEN** the user opens Cash Withdrawal and no destination bank account exists (mocked)
- **THEN** the system shows the prompt “Anda Belum Memiliki Rekening Tujuan”
- **AND THEN** the user can choose “Tambah Rekening” to proceed to adding a bank account

#### Scenario: Returning user enters Cash Withdrawal

- **WHEN** the user opens Cash Withdrawal and a destination bank account exists (mocked)
- **THEN** the system skips the first-time prompt and shows Tarik Tunai (Pilih Akun)

### Requirement: Use mock data for Cash Withdrawal flow content

The system SHALL populate the Cash Withdrawal flow content using data retrieved from the SDK via
domain use cases, replacing the previous mock data approach. Cash withdrawal uses the same SDK
transfer API with `isCashWithdrawal = true`.

#### Scenario: Card info retrieved via SDK

- **WHEN** the user reaches the Informasi Kartu screen in Cash Withdrawal flow
- **THEN** the card details (PAN, expiry date) are retrieved via `AtmFeatures.getCardInfo()`

#### Scenario: Withdrawal inquiry via SDK

- **WHEN** the user confirms withdrawal details (account, amount)
- **THEN** the system calls `AtmFeatures.transferInquiry()` with `accountId`, `amount`,
  `destinationDetails: BankDetails`, `notes` (optional), `isCashWithdrawal = true`, and `accountType`
- **AND THEN** the `CardReceiptResponse` containing `amount`, `adminFee`, `totalAmount`, and
  `transactionToken` is displayed on the Konfirmasi screen

#### Scenario: Withdrawal posting via SDK

- **WHEN** the user confirms the withdrawal on Konfirmasi
- **THEN** the system calls `AtmFeatures.transferPosting()` with the `accountId` and
  `transactionToken` obtained from the inquiry step
- **AND THEN** the success screen displays `CardReceiptResponse` data including `rrn`,
  `approvalCode`, `status`, and `totalAmount`

#### Scenario: Receipt shows SDK transaction details

- **WHEN** the user views the withdrawal receipt
- **THEN** the receipt details (`cardNumber`, `bankName`, `rrn`, `approvalCode`, `totalAmount`,
  `adminFee`) are rendered from the `CardReceiptResponse`

#### Scenario: Withdrawal token expiration

- **WHEN** the `transactionToken` from inquiry has expired (>15 minutes)
- **THEN** `transferPosting()` throws `TokenExpiredException`
- **AND THEN** the UI shows an error and prompts the user to re-do the inquiry step

#### Scenario: Withdrawal failure

- **WHEN** `transferInquiry()` or `transferPosting()` fails with a `DeviceSdkException`,
  `BackendException`, `TokenExpiredException`, or `InvalidTokenException`
- **THEN** the UI displays an error state with the error message

