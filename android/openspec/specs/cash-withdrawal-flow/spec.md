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

The system SHALL populate the Cash Withdrawal flow content using mock data only.

#### Scenario: Entry screen shows mock accounts

- **WHEN** the user views Tarik Tunai (Pilih Akun)
- **THEN** the account list contains mock entries (e.g., “Rekening Tabungan”, “Rekening Giro”)

