## 1. Implementation

- [x] 1.1 Add a new Home navigation event for “Tarik Tunai” and emit it from `HomeViewModel`.
- [x] 1.2 Update `HomeScreen` to accept and handle an `onNavigateToCashWithdrawal` callback (no
  toast shown).
- [x] 1.3 Add a new navigation route in `ui/navigation/Routes.kt` for the Cash Withdrawal entry
  screen.
- [x] 1.4 Wire the new route into the app `NavHost` (start from Home → Cash Withdrawal entry).
- [x] 1.5 Add first-time user gating at Cash Withdrawal entry (per the “First Time User” decision):
    - If no destination bank account exists (mocked), show “Anda Belum Memiliki Rekening Tujuan”
      with “Tambah Rekening” / “Nanti Saja”.
    - Otherwise, proceed directly into Tarik Tunai (Pilih Akun).
- [x] 1.6 Implement the Cash Withdrawal flow screens per Figma node `33:2198`:
    - Entry: “Tarik Tunai” → “Pilih Akun”
    - Insert/Swipe card
    - Card info
    - Enter PIN
    - Confirmation
    - Success / print preview and receipt preview (as designed)
- [x] 1.7 Ensure the Cash Withdrawal flow can return to Home upon completion (per spec).

## 2. Validation

- [x] 2.1 Run unit/build verification for the affected modules (`:app`, `:ui`).
- [x] 2.2 Manually verify: tapping Home “Tarik Tunai” opens the Cash Withdrawal entry screen.
- [x] 2.3 Manually verify: “Tarik Tunai” no longer shows the non-navigation toast.
