# Tasks

## 1. Foundation - SDK Initialization and Core Types

- [x] 1.1 Add SDK key to `BuildConfig` via `build.gradle.kts` (test environment)
- [x] 1.2 Create `SdkInitializer` wrapper class in `data/sdk/` that wraps
  `BukuEdcSdk.initialize()` with `BukuEdcConfig` (sdkKey, `BukuEdcEnv.SANDBOX`, optional
  `SdkLogListener`) and exposes the `BukuEdcSdk` instance
- [x] 1.3 Call `SdkInitializer.initialize()` from `BukuEdcApplication.onCreate()` on Main Thread
- [x] 1.4 Create `SdkModule.kt` Hilt module in `data/di/` providing `SdkInitializer` singleton,
  `BukuEdcSdk` (via `SdkInitializer`), and `AtmFeatures` instance (with token provider)
- [x] 1.5 Implement token provider `suspend () -> String` function for `AtmFeatures`
  (placeholder returning test token; partners will replace with their auth service)
- [x] 1.6 Remove stub `SdkInteractor.kt` class

## 2. Card Operations Integration

- [x] 2.1 Create `CardRepository` interface in `domain/transaction/` with `getCardInfo()` and
  `checkIncompleteTransactions()` methods
- [x] 2.2 Implement `CardRepositoryImpl` in `data/card/` delegating to `AtmFeatures.getCardInfo()`
  and `AtmFeatures.checkIncompleteTransactions()`
- [x] 2.3 Add `CardRepository` binding to `SdkModule`
- [x] 2.4 Update card info screens (Transfer, Balance, Withdrawal flows) to use `CardRepository`
  instead of mock data

## 3. Transaction Event Monitoring

- [x] 3.1 Create `TransactionEventRepository` interface in `domain/transaction/` exposing
  `transactionEvents: SharedFlow<TransactionEvent>`
- [x] 3.2 Implement in `data/` delegating to `AtmFeatures.transactionEvents`
- [x] 3.3 Update Insert Card screens to observe `WaitingForCard`, `CardDetected` events
- [x] 3.4 Update PIN screens to observe `EnteringPin` event
- [x] 3.5 Update processing/confirmation screens to observe `ProcessingTransaction`,
  `TransactionComplete`, `TransactionFailed` events

## 4. Transfer Flow SDK Integration

- [x] 4.1 Create `TransferRepository` interface in `domain/transaction/` with
  `transferInquiry()` and `transferPosting()` methods
- [x] 4.2 Implement `TransferRepositoryImpl` in `data/transaction/` delegating to
  `AtmFeatures.transferInquiry()` and `AtmFeatures.transferPosting()`
- [x] 4.3 Add `TransferRepository` binding to `SdkModule`
- [x] 4.4 Update Transfer flow ViewModels to call `transferInquiry()` with user-provided
  `accountId`, `amount`, `destinationDetails: BankDetails`, `notes`, and `accountType`
- [x] 4.5 Save `transactionToken` from inquiry response for posting step
- [x] 4.6 Update Transfer confirmation screen to call `transferPosting()` with saved token
- [x] 4.7 Display `CardReceiptResponse` data (amount, adminFee, totalAmount, rrn, approvalCode)
  on success screen
- [x] 4.8 Add error handling for `TokenExpiredException` (token valid 15 min) and
  `InvalidTokenException`
- [x] 4.9 Add inline comments explaining two-step transfer flow for partners

## 5. Balance Check Flow SDK Integration

- [x] 5.1 Create `BalanceRepository` interface in `domain/transaction/` with `checkBalance()` method
- [x] 5.2 Implement `BalanceRepositoryImpl` in `data/transaction/` delegating to
  `AtmFeatures.checkBalance(accountId, accountType)` (SDK 0.1.3: `sourceDetails` removed)
- [x] 5.3 Add `BalanceRepository` binding to `SdkModule`
- [x] 5.4 Update Balance Check ViewModels to call `checkBalance()` with user-provided parameters
- [x] 5.5 Display `CardReceiptResponse` data (totalAmount, cardNumber, rrn) on summary/receipt
  screens
- [x] 5.6 Add error handling UI states for Balance Check screens
- [x] 5.7 Add inline comments explaining balance check integration for partners

## 6. Cash Withdrawal Flow SDK Integration

- [x] 6.1 Update Cash Withdrawal ViewModels to call `transferInquiry()` with
  `isCashWithdrawal = true` (SDK reuses transfer API for withdrawals)
- [x] 6.2 Save `transactionToken` from inquiry response for posting step
- [x] 6.3 Update withdrawal confirmation to call `transferPosting()` with saved token
- [x] 6.4 Display `CardReceiptResponse` data on withdrawal success/receipt screens
- [x] 6.5 Add error handling UI states for Cash Withdrawal screens
- [x] 6.6 Add inline comments explaining that withdrawal uses transfer API with
  `isCashWithdrawal = true`

## 7. Transaction History Feature

- [x] 7.1 Create `HistoryRepository` interface in `domain/transaction/` with
  `getTransactionHistory(filter: TransactionFilter)` method
- [x] 7.2 Create domain models for transaction history (mapped from SDK's `HistoryItem`,
  `PaginationDetails`)
- [x] 7.3 Implement `HistoryRepositoryImpl` in `data/transaction/` delegating to
  `AtmFeatures.getTransactionHistory()`
- [x] 7.4 Add `HistoryRepository` binding to `SdkModule`
- [x] 7.5 Create `HistoryViewModel` with Hilt injection, pagination state management
- [x] 7.6 Create `HistoryScreen.kt` UI with list, empty, loading, and error states
  (each `HistoryItem` shows: transactionId, amount, status, date, type)
- [x] 7.7 Add History navigation route to `MainActivity`
- [x] 7.8 Update `HomeViewModel` to emit navigation event for Riwayat action
- [x] 7.9 Add inline comments explaining history retrieval and pagination for partners

## 8. Incomplete Transaction Handling

- [x] 8.1 Call `checkIncompleteTransactions()` on app start (e.g., in HomeViewModel or
  Application initialization)
- [x] 8.2 Display UI prompt when incomplete transaction is found, allowing user to resume
- [x] 8.3 Add inline comments explaining incomplete transaction recovery for partners

## 9. Error Handling

- [x] 9.1 Create `ErrorStateComposable.kt` reusable error UI component
- [x] 9.2 Add error handling examples to each flow demonstrating SDK error categories:
  - `DeviceSdkException` (E01 card read, E02 card removed, E06 PIN cancelled, E21 timeout)
  - `BackendException` (30 format error, 55 invalid PIN, 03 invalid merchant)
  - `TokenExpiredException` / `InvalidTokenException`

## 10. Partner Integration Documentation

- [ ] 10.1 Create `README.md` at project root with:
  - Prerequisites (Android API 21+, Kotlin 1.9+, Gradle setup)
  - How to build and run the sample app
  - Test credentials and environment configuration
  - Link to `INTEGRATION_GUIDE.md`
- [ ] 10.2 Create `INTEGRATION_GUIDE.md` at project root covering:
  - **Overview**: What the sample app demonstrates and how to use it as reference
  - **Architecture**: Clean Architecture pattern (data/domain/UI layers), how `AtmFeatures`
    is wrapped by repositories, consumed by ViewModels
  - **SDK Setup**: Step-by-step `BukuEdcConfig`, `BukuEdcSdk.initialize()`, Main Thread
    requirement, `SdkLogListener` setup
  - **Authentication**: Token provider `suspend () -> String`, 3-second timeout, connecting
    to partner's auth service
  - **Balance Check**: `getCardInfo()` → `checkBalance(accountId, accountType)` (SDK 0.1.3),
    reading `CardReceiptResponse` fields
  - **Transfer**: Two-step `transferInquiry()` → save `transactionToken` →
    `transferPosting()`, token 15-min expiry, `CardReceiptResponse` fields
  - **Cash Withdrawal**: Same as transfer with `isCashWithdrawal = true`
  - **Transaction History**: `getTransactionHistory(TransactionFilter)`, pagination via
    `PaginationDetails`, `HistoryItem` fields
  - **Incomplete Transactions**: `checkIncompleteTransactions()` on app start, recovery flow
  - **Transaction Events**: Observing `transactionEvents: SharedFlow<TransactionEvent>`,
    each event type and recommended UI handling
  - **Error Handling**: Full reference of SDK exception types with error codes, descriptions,
    and recommended handling strategies
- [ ] 10.3 Add inline code comments at all key integration points:
  - SDK initialization in `BukuEdcApplication`
  - Token provider in `SdkModule`
  - Each repository implementation (explaining SDK method, parameters, return type)
  - ViewModel transaction flows (explaining inquiry → token → posting pattern)
  - Event observation in UI (explaining each `TransactionEvent` subtype)
  - Error handling in ViewModels (explaining raw SDK exception handling patterns)
- [ ] 10.4 Review `INTEGRATION_GUIDE.md` against SDK contract in `SDK_USAGE_GUIDE.md`
  to ensure consistency and accuracy

## 11. Validation and Testing

- [ ] 11.1 Verify SDK initialization works on app startup
- [ ] 11.2 Test Transfer flow with SDK (inquiry → token → posting)
- [ ] 11.3 Test Balance Check flow with SDK
- [ ] 11.4 Test Cash Withdrawal flow with SDK (transferInquiry with isCashWithdrawal=true)
- [ ] 11.5 Test Transaction History retrieval, pagination, and display
- [ ] 11.6 Test incomplete transaction detection and recovery
- [ ] 11.7 Test error handling scenarios for each flow
- [ ] 11.8 Build release APK and verify no ProGuard issues with SDK

## Dependencies

- Tasks 2-8 depend on Task 1 (Foundation)
- Tasks 4-6 (transaction flows) depend on Task 2 (Card Operations) and Task 3 (Event Monitoring)
- Tasks 4-6 can be parallelized with each other
- Task 7 (History) can be parallelized after Task 1
- Task 8 (Incomplete Transactions) depends on Task 2
- Task 9 can begin after Task 1, completed after all flows integrated
- Task 10 (Documentation) should be done after all flows integrated (Tasks 4-8),
  so the guide reflects the actual implementation
- Task 11 should be done incrementally as each flow is completed
