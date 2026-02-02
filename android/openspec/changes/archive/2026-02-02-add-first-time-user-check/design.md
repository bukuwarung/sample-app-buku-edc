# Design: First Time User Check

## Context

The application requires users to have at least one bank account registered before they can
perform "Transfer", "Cek Saldo", or "Tarik Tunai" transactions. A "First Time User" is defined as a
user who has no bank accounts registered.

## Goals

- Provide a unified check for "Transfer", "Cek Saldo", and "Tarik Tunai".
- Redirect first-time users to a prompt screen.

## Decisions

- **New Use Case**: `CheckIsFirstTimeUserUseCase` will be created in the `domain` module. It will
  check if the list of bank accounts is empty using `GetBankAccountsUseCase`.
- **ViewModel Update**: `HomeViewModel` will call this use case when any of the three relevant
  actions are clicked.
- **Navigation Refactoring**:
    - `NavigateToCashWithdrawalFirstTime` will be renamed to `NavigateToAddBankAccount`. This event
      will now specifically represent navigation to the "Add Bank Account" screen (
      `SettingsBankAccounts`).
    - A new event `NavigateToFirstTimeUserPrompt` will be introduced. This event will specifically
      represent navigation to the "First Time User" prompt screen (
      `CashWithdrawalFirstTimeUserScreen`).
- **Callback Updates**:
    - `onNavigateToCashWithdrawalFirstTime` in `HomeScreen` and `MainActivity` will be renamed to
      `onNavigateToAddBankAccount` and will navigate to `Screen.SettingsBankAccounts`.
    - A new callback `onNavigateToFirstTimeUserPrompt` will be added to `HomeScreen` and implemented
      in `MainActivity` to navigate to `Screen.CashWithdrawalFirstTimeUser`.

## Alternatives Considered

- **Action-specific checks**: We could have separate use cases for each action, but the logic (
  checking for registered bank accounts) is currently identical for all three.
- **Direct Navigation**: Navigating directly to "Add Bank Account" without a prompt, but the
  requirements specify a prompt screen ("Anda Belum Memiliki Rekening Tujuan").

## Risks / Trade-offs

- Renaming existing navigation events and adding new ones increases the scope of changes in the UI
  layer, but it aligns with the requirement for distinct "Add Bank Account" and "First Time User
  Prompt" navigation.
