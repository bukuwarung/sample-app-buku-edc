# Design: Settings flow (Compose Navigation + MVVM)

## Goals

- Match the Figma Settings screen series and navigation behavior.
- Reuse the existing project patterns:
    - Compose `NavHost` routing via `Screen` routes
    - MVVM via ViewModels and UI events
    - Mock data in the UI layer

## Navigation model

- Add new `Screen` routes for:
    - `settings` (Pengaturan)
    - `settings_account` (Akun)
    - `settings_edit_store_name` (Ubah Nama Toko)
    - `settings_bank_accounts` (Rekening Anda)
- Back navigation uses `navController.popBackStack()` and follows the visual back arrow.

## State and events

- **Home** emits navigation events for actions that open flows (Transfer, Cek Saldo, Pengaturan).
- Settings screens use ViewModels for:
    - providing mock state
    - validating form state (e.g., “Simpan” enabled/disabled)
    - emitting one-off UI events (snackbar/toast + navigation)

## Success feedback

- Store-name editing is **non-functional** for this change:
    - Tapping “Simpan” shows a toast indicating the action is not available yet
    - No value is persisted or updated

## Data sources

- All values are mocked (phone, device serial, terminal id, store name/address, bank accounts).
- Values MUST be provided to the UI via **domain-module use cases** (injected into ViewModels), not
  hardcoded directly in Composables.
- No persistence beyond the app process for this change; no backend calls.
