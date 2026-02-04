## MODIFIED Requirements

### Requirement: Use SDK data for Balance Check content

The system SHALL populate Balance Check summary and receipt details using data retrieved from the SDK
via domain use cases.

#### Scenario: Balance Summary shows SDK balance

- **WHEN** the user reaches the Balance Summary screen
- **THEN** the balance amount and timestamp are rendered from SDK response data

#### Scenario: Receipt Preview shows SDK details

- **WHEN** the user views the Receipt Preview screen
- **THEN** the receipt details are rendered from SDK response data

#### Scenario: Balance check execution via SDK

- **WHEN** the user initiates a balance check
- **THEN** the system queries balance via SDK API
- **AND THEN** displays the result or error based on SDK response
