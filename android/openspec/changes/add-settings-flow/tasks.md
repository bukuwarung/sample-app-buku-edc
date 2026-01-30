## 1. Specs & validation

- [ ] Run `openspec validate add-settings-flow --strict` and resolve all issues.

## 2. Navigation wiring (Home → Settings)

- [ ] Extend `HomeUiEvent` and `HomeViewModel` to emit a navigation event for
  `HomeAction.Pengaturan`.
- [ ] Update `HomeScreen` to accept an `onNavigateToSettings` callback and route the new event to
  it.
- [ ] Add Settings destinations to the app NavHost and route Home → Settings.

## 3. Settings flow screens (UI + MVVM)

- [ ] Add **Pengaturan** screen UI with the menu items per Figma.
- [ ] Add **Akun** screen UI showing mock device/store fields per Figma.
- [ ] Add **Ubah Nama Toko** screen UI (non-functional): tapping “Simpan” shows a toast and does not
  persist changes.
- [ ] Add **Rekening Anda** screen UI showing mock account entries and “Edit Rekening” menu
  affordance.
- [ ] For non-functional items on Pengaturan (“Tes Cetak Struk”, “Atur Efek Suara”), show a toast on
  tap.

## 4. Mock data and state handling

- [ ] Add domain models + use cases for Settings data in the `domain` module (e.g.,
  account/device/store info and bank accounts).
- [ ] Provide mocked implementations (no backend calls) wired via DI so Settings ViewModels obtain
  data through these domain use cases.
- [ ] Keep settings edits non-persistent for now (no state mutation on save; toast only).

## 5. Strings, previews, and QA

- [ ] Add/verify string resources for all new UI labels (Indonesian text as in Figma).
- [ ] Add `@Preview` coverage for key screens (light theme).
- [ ] Manual test checklist:
    - [ ] Home → Pengaturan opens Settings menu
    - [ ] Settings → Akun opens account details
    - [ ] Akun → Ubah Nama Toko → Simpan returns to Akun + shows success feedback
    - [ ] Settings → Rekening Anda opens bank accounts list

## 6. Build validation

- [ ] Run `./gradlew :app:assembleDebug` (and unit tests if present) to confirm the build remains
  green.
