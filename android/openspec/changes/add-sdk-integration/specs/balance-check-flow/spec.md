## Figma Designs

- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-5577&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-5625&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-5648&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=36-5812&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=37-3737&m=dev
- https://www.figma.com/design/581MJmETk7zbTmHUZT64Ap/SDK-Android?node-id=42-3610&m=dev

## MODIFIED Requirements

### Requirement: Use mock data for Balance Check content

The system SHALL populate Balance Check summary and receipt details using data retrieved from the
SDK via domain use cases, replacing the previous mock data approach.

#### Scenario: Card info retrieved via SDK

- **WHEN** the user reaches the Informasi Kartu screen in Balance Check flow
- **THEN** the card details (PAN, expiry date) are retrieved via `AtmFeatures.getCardInfo()`

#### Scenario: Balance check execution via SDK

- **WHEN** the user initiates a balance check after PIN entry
- **THEN** the system calls `AtmFeatures.checkBalance()` with `accountId`,
  `sourceDetails: BankDetails(bankCode, bankName)`, and `accountType`

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
