# homepage-screen Specification

## Purpose
TBD - created by archiving change create-homepage-screen. Update Purpose after archive.
## Requirements
### Requirement: Show Homepage
The application MUST display a homepage screen with the specified design when launched. The design MUST be identical to the "Homepage-Master" Figma node.

#### Scenario: App Launch
- **WHEN** the application is started
- **THEN** the user sees the "Client Interface" title on a green background
- **THEN** the user sees the "MiniATM" card with action icons

### Requirement: Action Grid
The homepage MUST contain a grid of actions for MiniATM features.

#### Scenario: Display Actions
- **WHEN** the homepage is visible
- **THEN** the following actions are displayed: "Transfer", "Cek Saldo", "Tarik Tunai", "Riwayat", "Pengaturan"
- **THEN** each action has its corresponding icon

### Requirement: Open Settings from Homepage action grid

The homepage SHALL open the Settings flow when the user selects the “Pengaturan” action tile.

#### Scenario: User taps Pengaturan

- **WHEN** the user taps the “Pengaturan” action tile
- **THEN** the Settings (Pengaturan) screen is displayed

### Requirement: Open Cash Withdrawal from Homepage action grid

The homepage SHALL open the Cash Withdrawal flow when the user selects the “Tarik Tunai” action tile
and is not a first-time user.

#### Scenario: Returning user taps Tarik Tunai

- **WHEN** the user taps the “Tarik Tunai” action tile AND at least one bank account exists
- **THEN** the Cash Withdrawal entry screen is displayed

### Requirement: First-time User check for MiniATM transactions

The homepage SHALL check if the user is a first-time user when "Transfer", "Cek Saldo", or "Tarik
Tunai" is selected. A first-time user is defined as a user who has not yet added a destination bank
account.

#### Scenario: First-time user taps Transfer

- **WHEN** the user taps the "Transfer" action tile AND no bank account exists
- **THEN** the system shows the first-time prompt "Anda Belum Memiliki Rekening Tujuan"

#### Scenario: First-time user taps Cek Saldo

- **WHEN** the user taps the "Cek Saldo" action tile AND no bank account exists
- **THEN** the system shows the first-time prompt "Anda Belum Memiliki Rekening Tujuan"

#### Scenario: First-time user taps Tarik Tunai

- **WHEN** the user taps the "Tarik Tunai" action tile AND no bank account exists
- **THEN** the system shows the first-time prompt "Anda Belum Memiliki Rekening Tujuan"

### Requirement: Open Transfer from Homepage action grid

The homepage SHALL open the Transfer flow when the user selects the "Transfer" action tile and is
not a first-time user.

#### Scenario: Returning user taps Transfer

- **WHEN** the user taps the "Transfer" action tile AND at least one bank account exists
- **THEN** the Transfer entry screen is displayed

### Requirement: Open Balance Check from Homepage action grid

The homepage SHALL open the Balance Check flow when the user selects the "Cek Saldo" action tile and
is not a first-time user.

#### Scenario: Returning user taps Cek Saldo

- **WHEN** the user taps the "Cek Saldo" action tile AND at least one bank account exists
- **THEN** the Balance Check entry screen is displayed

