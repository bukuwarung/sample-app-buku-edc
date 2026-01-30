# settings-flow Specification

## Purpose

TBD - created by archiving change add-settings-flow. Update Purpose after archive.

## Requirements

### Requirement: Provide Settings flow screen sequence

The system SHALL present the Settings flow screens and navigation paths based on the Figma
“Settings” design:

- From **Pengaturan**:
    - Navigate to **Akun** from the “Akun” menu item.
    - Navigate to **Rekening Anda** from the “Rekening Pencairan Tarik Tunai” menu item.
- From **Akun**:
    - Navigate to **Ubah Nama Toko** from the “Ubah” action on “Nama Toko”.

#### Scenario: User opens Settings and navigates to Akun

- **WHEN** the user selects “Akun” from the Pengaturan menu
- **THEN** the Akun screen is shown

#### Scenario: User opens Settings and navigates to Rekening Anda

- **WHEN** the user selects “Rekening Pencairan Tarik Tunai” from the Pengaturan menu
- **THEN** the Rekening Anda screen is shown

#### Scenario: User edits store name successfully

- **WHEN** the user taps “Ubah” for “Nama Toko” on the Akun screen
- **THEN** the Ubah Nama Toko screen is shown
- **WHEN** the user taps “Simpan”
- **THEN** the system shows a toast indicating the action is not available yet
- **THEN** no stored value is updated (non-functional)

### Requirement: Show toast feedback for non-functional Settings actions

The system SHALL show a toast for Settings actions that are present in the UI but not implemented.

#### Scenario: User taps Tes Cetak Struk

- **WHEN** the user taps “Tes Cetak Struk”
- **THEN** the system shows a toast indicating the action is not available yet

#### Scenario: User taps Atur Efek Suara

- **WHEN** the user taps “Atur Efek Suara”
- **THEN** the system shows a toast indicating the action is not available yet

### Requirement: Use mocked data delivered via domain use cases for Settings content

The system SHALL populate Settings screens with mocked data provided via use cases in the `domain`
module.

#### Scenario: Akun shows mock device/store fields

- **WHEN** the user views the Akun screen
- **THEN** the phone number, device serial, terminal id, store name, and store address are rendered
  from mocked values returned by a domain use case

#### Scenario: Rekening Anda shows mock bank accounts

- **WHEN** the user views the Rekening Anda screen
- **THEN** the list displays mocked bank account entries returned by a domain use case

### Requirement: Apply MVVM with Hilt per Settings screen

The system SHALL provide a Hilt-injected ViewModel for each Settings screen to supply UI state and
events.

#### Scenario: ViewModel injection

- **WHEN** a Settings screen is composed
- **THEN** the corresponding ViewModel is created via Hilt

