# Change: Add First Time User Check to Home Page

## Why

Currently, only the "Tarik Tunai" action has a first-time user check. We need to implement a
consistent "First Time User" check for "Transfer", "Cek Saldo", and "Tarik Tunai" to ensure users
have set up their bank accounts before proceeding with these transactions.

## What Changes

- Create `CheckIsFirstTimeUserUseCase` in the domain layer.
- **RENAME** `HomeUiEvent.NavigateToCashWithdrawalFirstTime` to
  `HomeUiEvent.NavigateToAddBankAccount`.
- **ADD** `HomeUiEvent.NavigateToFirstTimeUserPrompt` as a new navigation event.
- **RENAME** `onNavigateToCashWithdrawalFirstTime` callback to `onNavigateToAddBankAccount` in
  `HomeScreen`.
- **ADD** `onNavigateToFirstTimeUserPrompt` callback to `HomeScreen`.
- Update `HomeViewModel` to use `CheckIsFirstTimeUserUseCase` for "Transfer", "Cek Saldo", and "
  Tarik Tunai" actions.
- Update `HomeScreen` and `MainActivity` to use the renamed and new navigation events and callbacks.
- **MODIFIED** `HomeViewModel` logic to trigger `NavigateToFirstTimeUserPrompt` on click of "
  Transfer", "Cek Saldo", and "Tarik Tunai" if the user is a first-time user.

## Impact

- Affected specs: `homepage-screen`
- Affected code: `HomeViewModel.kt`, `HomeScreen.kt`, `CheckIsFirstTimeUserUseCase.kt` (new)
