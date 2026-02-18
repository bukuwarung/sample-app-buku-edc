## Figma Designs

- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-2814&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-2712&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-2902&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-2925&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-3025&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=35-3043&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-5231&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-3993&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-3652&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4199&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4504&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4468&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4520&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4511&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-4536&m=dev

## MODIFIED Requirements

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
  `destinationDetails: BankDetails`, `isCashWithdrawal = true`, and `accountType`
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

#### Scenario: Withdrawal failure

- **WHEN** `transferInquiry()` or `transferPosting()` fails with a `DeviceSdkException` or
  `BackendException`
- **THEN** the UI displays an error state with the mapped error message
