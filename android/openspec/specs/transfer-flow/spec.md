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
The system SHALL populate bank lists and transfer details using mock data only.

#### Scenario: Bank list shows mock entries
- **WHEN** the user views the Pilih Bank screen
- **THEN** the list displays mock banks defined in the UI layer

#### Scenario: Confirmation displays mock transfer details
- **WHEN** the user reaches Konfirmasi Transfer
- **THEN** the details are rendered from mock data

### Requirement: Apply MVVM with Hilt per Transfer screen
The system SHALL provide a Hilt-injected ViewModel for each Transfer screen to supply UI state and events.

#### Scenario: ViewModel injection
- **WHEN** a Transfer screen is composed
- **THEN** the corresponding ViewModel is created via Hilt

