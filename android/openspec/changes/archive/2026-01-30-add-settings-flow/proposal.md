# Change: Add Settings flow navigation from Home (Figma: Settings)

## Why

The Home action tile **Pengaturan** currently does not open the Settings experience. The product
requirement is to navigate from Home into the Settings screen series defined in the Figma “Settings”
designs.

## What Changes

- Add a new Settings flow reachable from the Home **Pengaturan** tile.
- Implement the Settings screen series per Figma:
    - **Pengaturan** (menu)
    - **Akun** (device/store info)
    - **Ubah Nama Toko** (UI only; save is non-functional for now and shows a toast)
    - **Rekening Anda** (reachable from “Rekening Pencairan Tarik Tunai”)
- Update Home MVVM behavior so **Pengaturan** emits navigation instead of toast.
- Use **mocked values delivered via domain use cases** (no backend calls).

## Out of Scope (for this change)

- Connecting to backend APIs or persistence beyond in-memory/mock state.
- Printer integration for “Tes Cetak Struk” (non-functional; toast only).
- Real device connectivity management.
- Full “Atur Efek Suara” implementation (non-functional; toast only).
- “Atur Biaya Admin” (appears in Figma JSON but is not part of the requested flow
  screenshot/series).

## User Experience / Flow

- **Home → Pengaturan**: tapping the Home action tile opens **Pengaturan**.
- **Pengaturan → Akun**: tapping “Akun” opens the Akun detail screen.
- **Akun → Ubah Nama Toko**: tapping “Ubah” next to “Nama Toko” opens the edit screen.
- **Ubah Nama Toko**: tapping “Simpan” shows a toast and does not persist changes (non-functional
  for now).
- **Pengaturan → Rekening Anda**: tapping “Rekening Pencairan Tarik Tunai” opens the bank account
  list screen.

## Impact

- **Specs**
    - Modified: `home-mvvm`, `homepage-screen`
    - Added: `settings-flow`
- **Code areas (implementation stage)**
    - `HomeViewModel` / Home UI event handling
    - `MainActivity` NavHost & `Screen` routes
    - New Settings UI screens + ViewModels in `ui` module
    - New Settings use cases in `domain` module (backed by mocked implementations)

## Decisions (from follow-up)

- “Tes Cetak Struk” and “Atur Efek Suara” remain **non-functional** and only show a toast when
  tapped.
- “Ubah Nama Toko” remains **non-functional** for now (UI only; no persistence).
