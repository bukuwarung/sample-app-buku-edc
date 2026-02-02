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

The homepage SHALL open the Cash Withdrawal flow when the user selects the “Tarik Tunai” action
tile.

#### Scenario: User taps Tarik Tunai

- **WHEN** the user taps the “Tarik Tunai” action tile
- **THEN** the Cash Withdrawal entry screen is displayed (per Figma node `33:2198`)

