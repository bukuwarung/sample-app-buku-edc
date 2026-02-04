## Context

The sample app demonstrates SDK features for partner developers. Currently, all transaction flows
(Transfer, Balance Check, Cash Withdrawal) render UI with mock data defined in the UI layer. The SDK
dependency (`com.bukuwarung.edc.sdk:core:0.1.0-SNAPSHOT`) exists but has only a stub `SdkInteractor`
class.

**Stakeholders**: Partner developers, SDK team, Android team lead

**Constraints**:
- Must use existing modular architecture: `app` → `ui` → `domain` ← `data`
- SDK is provided as an AAR with predefined APIs
- Test credentials only (no production payments)

## Goals / Non-Goals

**Goals**:
- Demonstrate all SDK features: initialization, activation, transactions, history
- Provide clear, well-commented reference code for partners
- Show proper error handling patterns
- Maintain clean architecture separation

**Non-Goals**:
- Production-ready error recovery / retry logic
- Offline caching or persistence beyond settings
- UI polish beyond functional demonstration
- White-labeling customization (Phase 2)

## Decisions

### Decision 1: Repository Pattern for SDK Access

**What**: Create repository interfaces in `domain/` with implementations in `data/` that wrap SDK
calls.

**Why**: Maintains clean architecture, allows UI/domain to remain SDK-agnostic, enables easy testing
with fake implementations.

**Alternatives considered**:
- Direct SDK calls from ViewModels: Rejected—violates separation of concerns, harder to test
- Single "SdkInteractor" facade: Rejected—would become a god class, harder to maintain

### Decision 2: Coroutine-based Async SDK Calls

**What**: Wrap SDK callbacks in `suspendCancellableCoroutine` to expose suspend functions.

**Why**: Aligns with existing Kotlin coroutines usage in the app, cleaner ViewModel integration.

**Alternatives considered**:
- RxJava: Rejected—adds unnecessary dependency, team prefers coroutines
- Callback propagation: Rejected—leads to callback hell, harder to compose

### Decision 3: Sealed Class Result Types for Error Handling

**What**: Define `Result<T, E>` sealed classes in domain layer for SDK responses.

**Why**: Type-safe error handling, forces callers to handle both success and error cases,
demonstrates best practices to partners.

**Alternatives considered**:
- Exceptions: Rejected—easy to miss handling, not idiomatic Kotlin for expected failures
- Nullable returns: Rejected—loses error context

### Decision 4: SDK Initialization in Application Class

**What**: Initialize SDK in `SampleApplication.onCreate()` with test credentials from BuildConfig.

**Why**: SDK must be initialized before any feature usage, Application is the earliest lifecycle
point.

**Alternatives considered**:
- Lazy initialization on first use: Rejected—complicates first-use flows, potential race conditions
- Activity-level initialization: Rejected—too late, may miss early SDK events

### Decision 5: Hilt Modules for SDK Dependencies

**What**: Create `SdkModule` in `data/di/` providing SDK instance and repositories.

**Why**: Consistent with existing Hilt setup (`DataStoreModule`, `SettingsDataModule`).

## Module Responsibilities

```
app/
├── SampleApplication.kt    # SDK initialization
└── MainActivity.kt         # Navigation including History

data/
├── di/
│   └── SdkModule.kt        # Hilt bindings for SDK and repositories
├── sdk/
│   ├── SdkWrapper.kt       # Coroutine wrappers for SDK callbacks
│   └── SdkConfig.kt        # Test credentials, environment config
├── activation/
│   └── ActivationRepositoryImpl.kt
├── transaction/
│   ├── TransferRepositoryImpl.kt
│   ├── BalanceRepositoryImpl.kt
│   ├── WithdrawalRepositoryImpl.kt
│   └── HistoryRepositoryImpl.kt
└── bank/
    └── BankAccountRepositoryImpl.kt

domain/
├── common/
│   └── Result.kt           # Sealed Result type
├── activation/
│   ├── ActivationRepository.kt
│   └── ActivateDeviceUseCase.kt
├── transaction/
│   ├── TransferRepository.kt
│   ├── BalanceRepository.kt
│   ├── WithdrawalRepository.kt
│   ├── HistoryRepository.kt
│   └── usecases/
└── bank/
    ├── BankAccountRepository.kt
    └── RegisterBankAccountUseCase.kt

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
| SDK API changes break integration | Pin SDK version, add integration tests |
| Test credentials expire | Document credential refresh process in README |
| Complex error states overwhelm partners | Group errors into categories with clear examples |
| Performance overhead from coroutine wrapping | Minimal—SDK calls are already async |

## Migration Plan

1. **Phase 1**: Add SDK initialization and `Result` types (non-breaking)
2. **Phase 2**: Add repositories alongside existing fakes (feature flag switchable)
3. **Phase 3**: Wire existing flows to new repositories (replace mock data)
4. **Phase 4**: Add transaction history feature (new capability)
5. **Phase 5**: Update README and inline comments for partners

**Rollback**: Feature flag can revert to mock data if SDK issues arise during development.

## Open Questions

1. **SDK test credentials**: Need confirmation of test environment URL and credentials from SDK team
2. **Error code mapping**: Need SDK error code documentation to create proper error categories
3. **History pagination**: Does SDK support paginated history retrieval or full list only?
