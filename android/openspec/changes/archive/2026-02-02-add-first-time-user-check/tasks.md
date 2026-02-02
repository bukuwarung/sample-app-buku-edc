## 1. Domain Layer

- [x] 1.1 Create `CheckIsFirstTimeUserUseCase` in
  `domain/src/main/java/com/bukuwarung/edc/domain/cash/CheckIsFirstTimeUserUseCase.kt` (or a more
  appropriate package if needed, but the current eligibility check is in `cash`).

## 2. UI Layer

- [x] 2.1 Rename `HomeUiEvent.NavigateToCashWithdrawalFirstTime` to `NavigateToAddBankAccount` in
  `HomeViewModel.kt`.
- [x] 2.2 Add `NavigateToFirstTimeUserPrompt` to `HomeUiEvent` in `HomeViewModel.kt`.
- [x] 2.3 Rename `onNavigateToCashWithdrawalFirstTime` to `onNavigateToAddBankAccount` in
  `HomeScreen.kt`.
- [x] 2.4 Add `onNavigateToFirstTimeUserPrompt` callback to `HomeScreen.kt`.
- [x] 2.5 Update `MainActivity.kt` to:
    - Implement the renamed `onNavigateToAddBankAccount` (navigating to
      `Screen.SettingsBankAccounts`).
    - Implement the new `onNavigateToFirstTimeUserPrompt` (navigating to
      `Screen.CashWithdrawalFirstTimeUser`).
- [x] 2.6 Update `HomeViewModel` to inject `CheckIsFirstTimeUserUseCase`.
- [x] 2.7 Modify `HomeViewModel.onActionClick` to call `CheckIsFirstTimeUserUseCase` for `Transfer`,
  `CekSaldo`, and `TarikTunai`.
- [x] 2.8 Emit `NavigateToFirstTimeUserPrompt` if the user is a first-time user for any of these
  actions.
- [x] 2.9 Keep `CheckCashWithdrawalEligibilityUseCase` as a placeholder (do not remove).

## 3. Validation

- [x] 3.1 Verify navigation logic in `MainActivity.kt` still works as expected.
- [x] 3.2 Update `HomeScreenPreview` to reflect `HomeViewModel` changes and the updated navigation
  callbacks.
