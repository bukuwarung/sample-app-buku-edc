## MODIFIED Requirements

### Requirement: Use SDK data for Transfer flow content

The system SHALL populate bank lists and transfer details using data retrieved from the SDK via
domain use cases.

#### Scenario: Bank list shows SDK entries

- **WHEN** the user views the Pilih Bank screen
- **THEN** the list displays banks retrieved from the SDK

#### Scenario: Confirmation displays SDK transfer details

- **WHEN** the user reaches Konfirmasi Transfer
- **THEN** the details are rendered from SDK response data

#### Scenario: Transfer execution via SDK

- **WHEN** the user confirms the transfer
- **THEN** the system executes the transfer via SDK API
- **AND THEN** displays success or error based on SDK response
