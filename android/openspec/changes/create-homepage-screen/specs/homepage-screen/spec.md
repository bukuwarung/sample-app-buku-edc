# Spec: Homepage Screen

## ADDED Requirements

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
