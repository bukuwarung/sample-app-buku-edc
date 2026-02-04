## MODIFIED Requirements

### Requirement: Use SDK data for Cash Withdrawal flow content

The system SHALL populate the Cash Withdrawal flow content using data retrieved from the SDK via
domain use cases.

#### Scenario: Entry screen shows SDK accounts

- **WHEN** the user views Tarik Tunai (Pilih Akun)
- **THEN** the account list contains entries retrieved from the SDK

#### Scenario: Withdrawal execution via SDK

- **WHEN** the user confirms the withdrawal
- **THEN** the system executes the withdrawal via SDK API
- **AND THEN** displays success or error based on SDK response

#### Scenario: Receipt shows SDK transaction details

- **WHEN** the user views the withdrawal receipt
- **THEN** the receipt details are rendered from SDK transaction response
