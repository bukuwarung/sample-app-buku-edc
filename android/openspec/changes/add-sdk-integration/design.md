## Context

The sample app demonstrates SDK features for partner developers. Currently, all transaction flows
(Transfer, Balance Check, Cash Withdrawal) render UI with mock data defined in the UI layer. The SDK
dependency (`com.bukuwarung.edc.sdk:core:0.1.0-SNAPSHOT`) exists but has only a stub `SdkInteractor`
class.

The SDK exposes two main interfaces: `BukuEdcSdk` (initialization, device ID, ATM features factory)
and `AtmFeatures` (all transaction operations). All SDK methods are coroutine-native — `suspend`
functions returning `kotlin.Result<T>` — so no callback wrapping is needed.

**Stakeholders**: Partner developers, SDK team, Android team lead

**Constraints**:
- Must use existing modular architecture: `app` → `ui` → `domain` ← `data`
- SDK is provided as an AAR with coroutine-native suspend APIs
- Test credentials only (no production payments)
- SDK requires initialization from Main Thread in Application.onCreate()

## Goals / Non-Goals

**Goals**:
- Demonstrate all SDK features: initialization, transactions (transfer, balance, withdrawal),
  history, incomplete transaction recovery, and event monitoring
- Provide clear, well-commented reference code for partners
- Show proper error handling patterns using SDK exception types
- Maintain clean architecture separation

**Non-Goals**:
- Device activation (no SDK API — partner backend responsibility)
- Bank account registration (no SDK API — partner backend responsibility)
- Bank list retrieval (SDK accepts `BankDetails` as input, not a data source)
- Production-ready error recovery / retry logic
- Offline caching or persistence beyond settings
- UI polish beyond functional demonstration
- White-labeling customization (Phase 2)

## SDK API Contract Summary

```kotlin
// Initialization
BukuEdcSdk.initialize(application: Application, bukuEdcConfig: BukuEdcConfig): BukuEdcSdk
BukuEdcSdk.getDeviceId(): Deferred<String>
BukuEdcSdk.getAtmFeatures(tokenProvider: suspend () -> String): AtmFeatures

// Configuration
BukuEdcConfig(sdkKey: String, environment: BukuEdcEnv, logListener: SdkLogListener? = null)

// Transaction Operations (all suspend, return kotlin.Result<T>)
AtmFeatures.checkIncompleteTransactions(): Result<IncompleteTransaction?>
AtmFeatures.getCardInfo(): Result<CardInfo>
AtmFeatures.checkBalance(accountId, sourceDetails: BankDetails, accountType): Result<CardReceiptResponse>
AtmFeatures.transferInquiry(accountId, amount, destinationDetails, notes, isCashWithdrawal, accountType): Result<CardReceiptResponse>
AtmFeatures.transferPosting(accountId, transactionToken): Result<CardReceiptResponse>
AtmFeatures.getTransactionHistory(filter: TransactionFilter): Result<TransactionHistory>

// Real-time events
AtmFeatures.transactionEvents: SharedFlow<TransactionEvent>

// Exceptions
SdkException(message, code)         // Base exception
DeviceSdkException(message, code)   // Device/hardware errors (E01, E02, E06, E21, E99)
BackendException(message, code)     // Backend errors (30, 55, 03)
TokenExpiredException(message)      // Transaction token expired (>15 min)
InvalidTokenException(message)      // Invalid transaction token
```

## Decisions

### Decision 1: Repository Pattern for SDK Access

**What**: Create repository interfaces in `domain/` with implementations in `data/` that delegate
to `AtmFeatures` methods.

**Why**: Maintains clean architecture, allows UI/domain to remain SDK-agnostic, enables easy testing
with fake implementations.

**Alternatives considered**:
- Direct SDK calls from ViewModels: Rejected—violates separation of concerns, harder to test
- Single "SdkInteractor" facade: Rejected—would become a god class, harder to maintain

### Decision 2: Direct Suspend Function Delegation

**What**: Repository implementations directly call `AtmFeatures` suspend functions. No callback
wrapping or bridging is needed.

**Why**: The SDK is already coroutine-native — all operations are `suspend` functions returning
`kotlin.Result<T>`. The data layer simply delegates to these methods and maps the results.

**Alternatives considered**:
- Callback wrapping with `suspendCancellableCoroutine`: Not needed — SDK is already suspend-based
- RxJava: Rejected — adds unnecessary dependency, SDK is already coroutines-native

### Decision 3: Map SDK Exceptions to Domain Error Types

**What**: Catch SDK exception types in repository implementations and map them to domain-friendly
sealed classes for UI consumption.

**Why**: Provides type-safe error handling in the UI layer while keeping domain SDK-agnostic. SDK
exceptions (`DeviceSdkException`, `BackendException`, `TokenExpiredException`,
`InvalidTokenException`) can be mapped to categories meaningful to the UI (device error, server
error, auth error, etc.).

**Alternatives considered**:
- Custom `Result<T, E>` sealed class: Rejected — SDK already returns `kotlin.Result<T>`, adding
  a second result type increases complexity
- Raw exception propagation: Rejected — leaks SDK types into domain/UI layers

### Decision 4: SDK Initialization in Application Class

**What**: Initialize SDK in `BukuEdcApplication.onCreate()` with test credentials.

**Why**: SDK must be initialized before any feature usage. SDK documentation strongly recommends
Main Thread initialization to capture all Activity lifecycle events (required for PIN input UI).

**Alternatives considered**:
- Lazy initialization on first use: Rejected—complicates first-use flows, may miss lifecycle events
- Activity-level initialization: Rejected—too late, may miss early SDK events

### Decision 5: Hilt Modules for SDK Dependencies

**What**: Create `SdkModule` in `data/di/` providing `BukuEdcSdk`, `AtmFeatures`, and repository
bindings.

**Why**: Consistent with existing Hilt setup (`DataStoreModule`, `SettingsDataModule`).

### Decision 6: Dedicated Integration Guide for Partners

**What**: Create `INTEGRATION_GUIDE.md` at the project root as the primary documentation for
partner developers, plus `README.md` for project setup.

**Why**: The sample app's primary purpose is to help partners integrate the SDK. Inline comments
alone are insufficient — partners need a standalone document they can follow step-by-step
without reading the full codebase. The guide complements the SDK's own `SDK_USAGE_GUIDE.md`
by showing integration patterns in a real app context (Clean Architecture, Hilt DI, Compose UI).

**Alternatives considered**:
- Wiki / Confluence only: Rejected — documentation should live with the code it describes
- Inline comments only: Rejected — doesn't provide a cohesive integration narrative
- Copy SDK_USAGE_GUIDE.md: Rejected — different purpose; SDK guide is API reference, integration
  guide shows patterns in the context of a real app architecture

## Module Responsibilities

```
(root)
├── README.md               # Build, run, prerequisites, test credentials
└── INTEGRATION_GUIDE.md    # Full SDK integration guide for partners

app/
├── BukuEdcApplication.kt  # SDK initialization with BukuEdcConfig
└── MainActivity.kt        # Navigation including History

data/
├── di/
│   └── SdkModule.kt       # Hilt bindings: BukuEdcSdk, AtmFeatures, repositories
├── transaction/
│   ├── TransferRepositoryImpl.kt   # Delegates to transferInquiry + transferPosting
│   ├── BalanceRepositoryImpl.kt    # Delegates to checkBalance
│   └── HistoryRepositoryImpl.kt    # Delegates to getTransactionHistory
└── card/
    └── CardRepositoryImpl.kt       # Delegates to getCardInfo, checkIncompleteTransactions

domain/
├── common/
│   └── SdkError.kt         # Sealed class mapping SDK exceptions to domain errors
├── transaction/
│   ├── TransferRepository.kt
│   ├── BalanceRepository.kt
│   ├── HistoryRepository.kt
│   ├── CardRepository.kt
│   └── models/             # Domain models mapped from SDK types
└── transaction/usecases/    # Use cases wrapping repository calls

ui/
├── history/
│   ├── HistoryScreen.kt
│   └── HistoryViewModel.kt
└── common/
    └── ErrorStateComposable.kt
```

## Risks / Trade-offs

| Risk | Mitigation |
|------|------------|
| SDK API changes break integration | Pin SDK version, document version compatibility |
| Test credentials expire | Document credential refresh process in README |
| Transaction token expiry (15 min) | Show clear UI feedback when token expires, prompt re-inquiry |
| Complex error states overwhelm partners | Group errors into categories with clear examples |

## Migration Plan

1. **Phase 1**: SDK initialization, Hilt module, error mapping types (non-breaking)
2. **Phase 2**: Card and transaction repositories (alongside existing mocks)
3. **Phase 3**: Wire existing flows to new repositories (replace mock data)
4. **Phase 4**: Add transaction history feature (new capability)
5. **Phase 5**: Add incomplete transaction handling
6. **Phase 6**: Create `INTEGRATION_GUIDE.md`, `README.md`, and inline comments for partners

**Rollback**: Can revert to mock data by swapping Hilt bindings if SDK issues arise.
