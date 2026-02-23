# Change: Integrate SDK for Partner Integration Sample App

## Why

The sample app currently uses mock data throughout all transaction flows (Transfer, Balance Check,
Cash Withdrawal). Partners need a reference implementation that demonstrates **real SDK integration**
to reduce integration time and support requests. The tech lead has directed that the data module
should be built out with SDK integration (BUKU-12647 comment).

## What Changes

- **SDK Initialization**: Add proper SDK initialization with `BukuEdcSdk.initialize()` in the
  Application class, providing `BukuEdcConfig` with SDK key and environment
- **Token Provider Setup**: Implement the `suspend () -> String` token provider required by
  `AtmFeatures` for authenticated SDK operations
- **Data Layer Repositories**: Create repository implementations in the `data` module that delegate
  to `AtmFeatures` suspend functions (replacing the current mock-based patterns)
- **Transfer Flow Integration**: Wire Transfer flow to use `getCardInfo()`, `transferInquiry()`, and
  `transferPosting()` two-step transaction API
- **Balance Check Flow Integration**: Wire Balance Check flow to use `getCardInfo()` and
  `checkBalance()` API
- **Cash Withdrawal Flow Integration**: Wire Cash Withdrawal flow to use `transferInquiry()` with
  `isCashWithdrawal = true` and `transferPosting()` API
- **Transaction History**: Add new feature using `getTransactionHistory()` with pagination support
  via `TransactionFilter` and `PaginationDetails`
- **Incomplete Transaction Handling**: Check for pending transactions on app start using
  `checkIncompleteTransactions()`
- **Transaction Event Monitoring**: Observe `transactionEvents: SharedFlow<TransactionEvent>` for
  real-time transaction progress (card detection, PIN entry, processing steps)
- **Error Handling**: Add error handling patterns using SDK exception types (`SdkException`,
  `DeviceSdkException`, `BackendException`, `TokenExpiredException`, `InvalidTokenException`)
- **Partner Integration Guide**: Create comprehensive `INTEGRATION_GUIDE.md` documenting the
  full SDK integration journey (setup, auth, transaction flows, error handling, event monitoring,
  architecture patterns) as the primary reference for partner developers
- **README**: Create project `README.md` with build/run instructions and test credentials

## Impact

- **Affected specs**: `transfer-flow`, `balance-check-flow`, `cash-withdrawal-flow`, `home-mvvm`
  (all will be MODIFIED to use SDK data instead of mocks)
- **New specs**: `sdk-integration` (initialization, error handling), `transaction-history`,
  `partner-integration-guide` (documentation for partners)
- **Affected code**:
  - `data/` module: New repositories, DI modules, direct `AtmFeatures` usage
  - `domain/` module: New repository interfaces, domain models
  - `app/` module: SDK initialization in Application class, navigation for history
  - `ui/` module: History screen, error state UI, transaction event observation

## Out of Scope

- Device activation (not part of SDK API — handled externally by partner backend)
- Bank account registration (not part of SDK API — handled externally by partner backend)
- Bank list retrieval (SDK accepts `BankDetails` as input, does not provide a list)
- White-labeling customization (Phase 2 per PRD)
- Production-ready UI design refinements
- Offline mode / caching strategies
- Real payment processing (test credentials only)
