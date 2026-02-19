# Change: Fix balance check PIN screen duplication

## Why
The balance check flow currently shows the app's "Masukkan PIN" screen **and** the SDK's own PIN
entry prompt. The SDK's `AtmFeatures.checkBalance()` already handles card reading, PIN entry, and
backend communication internally, so the app-level PIN screen is redundant and confusing to the
user.

## What Changes
- Skip the `BalanceCheckPin` navigation step in the balance check flow so the user goes directly
  from Card Info to the Balance Summary screen.
- The app's PIN screen files, layouts, and composables (`TransferPinScreen`, `TransferPinViewModel`,
  `NumericKeypad`, etc.) are **kept as-is** — they remain used by the Transfer and Cash Withdrawal
  flows, and are preserved for reference.
- The `BalanceCheckPin` route stays defined in `Routes.kt` for backward-compatibility; it is
  simply no longer navigated to during the balance check flow.

## Impact
- Affected specs: `balance-check-flow`
- Affected code:
  - `MainActivity.kt` — `BalanceCheckCardInfo` composable's `onContinue` callback changes
    navigation target from `BalanceCheckPin` to `BalanceCheckSummary`
  - `Routes.kt` — `BalanceCheckPin` route kept but unused in the balance check flow
