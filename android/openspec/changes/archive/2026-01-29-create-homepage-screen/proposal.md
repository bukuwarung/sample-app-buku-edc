# Change: Create Homepage Screen

## Why
The application currently lacks a homepage screen. A new screen is needed to provide users with access to various MiniATM features as per the Figma design "Homepage-Master". The Figma design is the authoritative source of truth for all layout, spacing, colors, and typography.

## What Changes
- **ADDED** `HomeScreen` Composable in the `ui` module, strictly following the Figma specifications.
- **MODIFIED** `MainActivity` to display the new `HomeScreen` as the primary interface.
- **ADDED** Action grid with features: Transfer, Cek Saldo, Tarik Tunai, Riwayat, and Pengaturan.

## Impact
- Affected specs: `homepage-screen` (new)
- Affected code: `MainActivity.kt`, `HomeScreen.kt` (new)
