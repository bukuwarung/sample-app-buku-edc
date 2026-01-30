## 1. Specs & validation

- [x] Run `openspec validate add-settings-flow --strict` and resolve all issues.

## 2. Navigation wiring (Home → Settings)

- [x] Extend `HomeUiEvent` and `HomeViewModel` to emit a navigation event for
  `HomeAction.Pengaturan`.
- [x] Update `HomeScreen` to accept an `onNavigateToSettings` callback and route the new event to
  it.
- [x] Add Settings destinations to the app NavHost and route Home → Settings.

## 3. Settings flow screens (UI + MVVM)

- [x] Add **Pengaturan** screen UI with the menu items per Figma.
- [x] Add **Akun** screen UI showing mock device/store fields per Figma.
- [x] Add **Ubah Nama Toko** screen UI (non-functional): tapping “Simpan” shows a toast and does not
  persist changes.
- [x] Add **Rekening Anda** screen UI showing mock account entries and “Edit Rekening” menu
  affordance.
- [x] For non-functional items on Pengaturan (“Tes Cetak Struk”, “Atur Efek Suara”), show a toast on
  tap.

## 4. Mock data and state handling

- [x] Add domain models + use cases for Settings data in the `domain` module (e.g.,
  account/device/store info and bank accounts).
- [x] Provide mocked implementations (no backend calls) wired via DI so Settings ViewModels obtain
  data through these domain use cases.
- [x] Keep settings edits non-persistent for now (no state mutation on save; toast only).

## 5. Strings, previews, and QA

- [x] Add/verify string resources for all new UI labels (Indonesian text as in Figma).
- [x] Add `@Preview` coverage for key screens (light theme).
- [x] Manual test checklist:
    - [x] Home → Pengaturan opens Settings menu
    - [x] Settings → Akun opens account details
    - [x] Akun → Ubah Nama Toko → Simpan returns to Akun + shows success feedback
    - [x] Settings → Rekening Anda opens bank accounts list

## 6. Build validation

- [x] Run `./gradlew :app:assembleDebug` (and unit tests if present) to confirm the build remains
  green.
