## 1. Navigation fix
- [x] 1.1 In `MainActivity.kt`, change the `BalanceCheckCardInfo` composable's `onContinue` to navigate to `Screen.BalanceCheckSummary` instead of `Screen.BalanceCheckPin`
- [x] 1.2 Remove the `BalanceCheckPin` composable block from the NavHost (no longer navigated to)

## 2. Verification
- [x] 2.1 Verify the balance check flow sequence is: Select Account → Insert Card → Card Info → Balance Summary → Receipt Preview
- [x] 2.2 Verify Transfer and Cash Withdrawal flows still navigate to their respective PIN screens unchanged
- [x] 2.3 Confirm no compile errors after the change
