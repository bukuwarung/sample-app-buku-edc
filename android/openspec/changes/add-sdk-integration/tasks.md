# Tasks

## 1. Foundation - SDK Setup and Core Types

- [ ] 1.1 Add SDK test credentials to `BuildConfig` via `build.gradle.kts`
- [ ] 1.2 Create `Result<T, E>` sealed class in `domain/common/Result.kt`
- [ ] 1.3 Create `SdkError` sealed class with Network, Authentication, Validation, Unknown variants
- [ ] 1.4 Initialize SDK in `SampleApplication.onCreate()` with test environment config
- [ ] 1.5 Create `SdkWrapper.kt` in `data/sdk/` with coroutine wrappers for SDK callbacks
- [ ] 1.6 Create `SdkModule.kt` Hilt module providing SDK instance

## 2. Device Activation Integration

- [ ] 2.1 Create `ActivationRepository` interface in `domain/activation/`
- [ ] 2.2 Create `ActivateDeviceUseCase` in `domain/activation/`
- [ ] 2.3 Implement `ActivationRepositoryImpl` in `data/activation/` wrapping SDK activation API
- [ ] 2.4 Add repository binding to `SdkModule`
- [ ] 2.5 Create `ActivationViewModel` with activation state management
- [ ] 2.6 Wire `ActivationScreen` to ViewModel with loading/error states
- [ ] 2.7 Add inline comments explaining activation flow for partners

## 3. Transfer Flow SDK Integration

- [ ] 3.1 Create `TransferRepository` interface in `domain/transaction/`
- [ ] 3.2 Create transfer-related use cases (GetBanks, ExecuteTransfer)
- [ ] 3.3 Implement `TransferRepositoryImpl` in `data/transaction/` wrapping SDK transfer API
- [ ] 3.4 Update Transfer flow ViewModels to use SDK repositories instead of mock data
- [ ] 3.5 Add error handling UI states to Transfer screens
- [ ] 3.6 Add inline comments explaining transfer integration for partners

## 4. Balance Check Flow SDK Integration

- [ ] 4.1 Create `BalanceRepository` interface in `domain/transaction/`
- [ ] 4.2 Create balance-related use cases (CheckBalance)
- [ ] 4.3 Implement `BalanceRepositoryImpl` in `data/transaction/` wrapping SDK balance API
- [ ] 4.4 Update Balance Check flow ViewModels to use SDK repositories
- [ ] 4.5 Add error handling UI states to Balance Check screens
- [ ] 4.6 Add inline comments explaining balance check integration for partners

## 5. Cash Withdrawal Flow SDK Integration

- [ ] 5.1 Create `WithdrawalRepository` interface in `domain/transaction/`
- [ ] 5.2 Create withdrawal-related use cases (GetAccounts, ExecuteWithdrawal)
- [ ] 5.3 Implement `WithdrawalRepositoryImpl` in `data/transaction/` wrapping SDK withdrawal API
- [ ] 5.4 Update Cash Withdrawal flow ViewModels to use SDK repositories
- [ ] 5.5 Add error handling UI states to Cash Withdrawal screens
- [ ] 5.6 Add inline comments explaining withdrawal integration for partners

## 6. Bank Account Registration Integration

- [ ] 6.1 Create `BankAccountRepository` interface in `domain/bank/`
- [ ] 6.2 Create `RegisterBankAccountUseCase` in `domain/bank/`
- [ ] 6.3 Implement `BankAccountRepositoryImpl` in `data/bank/` wrapping SDK bank API
- [ ] 6.4 Update first-time user flow to use SDK bank registration
- [ ] 6.5 Add inline comments explaining bank registration for partners

## 7. Transaction History Feature

- [ ] 7.1 Create `HistoryRepository` interface in `domain/transaction/`
- [ ] 7.2 Create `GetTransactionHistoryUseCase` in `domain/transaction/`
- [ ] 7.3 Create transaction history domain models
- [ ] 7.4 Implement `HistoryRepositoryImpl` in `data/transaction/` wrapping SDK history API
- [ ] 7.5 Create `HistoryScreen.kt` UI with list, empty, loading, and error states
- [ ] 7.6 Create `HistoryViewModel` with Hilt injection
- [ ] 7.7 Add History navigation route to `MainActivity`
- [ ] 7.8 Update `HomeViewModel` to emit navigation event for Riwayat action
- [ ] 7.9 Add inline comments explaining history retrieval for partners

## 8. Error Handling and Documentation

- [ ] 8.1 Create `ErrorStateComposable.kt` reusable error UI component
- [ ] 8.2 Add error handling examples to each flow demonstrating SDK error categories
- [ ] 8.3 Update README with SDK setup instructions for partners
- [ ] 8.4 Add inline code comments at key integration points
- [ ] 8.5 Document test credentials and environment configuration

## 9. Validation and Testing

- [ ] 9.1 Verify SDK initialization works on app startup
- [ ] 9.2 Test device activation flow end-to-end
- [ ] 9.3 Test Transfer flow with SDK data
- [ ] 9.4 Test Balance Check flow with SDK data
- [ ] 9.5 Test Cash Withdrawal flow with SDK data
- [ ] 9.6 Test Transaction History retrieval and display
- [ ] 9.7 Test error handling scenarios for each flow
- [ ] 9.8 Build release APK and verify no ProGuard issues with SDK

## Dependencies

- Tasks 2-7 depend on Task 1 (Foundation)
- Tasks 2-6 can be parallelized after Task 1
- Task 7 (History) can be parallelized after Task 1
- Task 8 can begin after Task 1, completed after all flows integrated
- Task 9 should be done incrementally as each flow is completed

## Open Items

- [ ] Confirm SDK test credentials from SDK team
- [ ] Confirm SDK error code documentation for proper categorization
- [ ] Confirm if history API supports pagination
