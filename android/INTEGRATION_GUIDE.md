# Buku EDC SDK - Integration Guide

This guide walks through how the sample app integrates the Buku EDC SDK, providing patterns you can apply in your own application.

---

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [SDK Setup](#sdk-setup)
4. [Authentication](#authentication)
5. [Balance Check](#balance-check)
6. [Transfer](#transfer)
7. [Cash Withdrawal](#cash-withdrawal)
8. [Transaction History](#transaction-history)
9. [Incomplete Transactions](#incomplete-transactions)
10. [Transaction Events](#transaction-events)
11. [Error Handling](#error-handling)

---

## Overview

This sample app demonstrates all features of the Buku EDC SDK:

| Feature | SDK Method | Sample Code |
|---------|-----------|-------------|
| SDK Initialization | `BukuEdcSdkFactory.initialize()` | `SdkInitializer.kt` |
| Balance Check | `AtmFeatures.checkBalance()` | `BalanceRepositoryImpl.kt` → `BalanceCheckViewModel.kt` |
| Transfer | `AtmFeatures.transferInquiry()` + `transferPosting()` | `TransferRepositoryImpl.kt` → `TransferConfirmViewModel.kt` |
| Cash Withdrawal | Same as transfer with `isCashWithdrawal = true` | `TransferConfirmViewModel.kt` |
| Transaction History | `AtmFeatures.getTransactionHistory()` | `HistoryRepositoryImpl.kt` → `HistoryViewModel.kt` |
| Card Reading | `AtmFeatures.getCardInfo()` | `CardRepositoryImpl.kt` |
| Incomplete Transactions | `AtmFeatures.checkIncompleteTransactions()` | `CardRepositoryImpl.kt` → `HomeViewModel.kt` |
| Transaction Events | `AtmFeatures.transactionEvents` | `TransactionEventRepositoryImpl.kt` |
| Error Handling | SDK exception types | `ErrorStateComposable.kt`, all ViewModels |

**How to use this guide**: Each section explains the SDK API, shows how the sample app implements it, and highlights what you need to change for your own app.

---

## Architecture

The sample app uses **Clean Architecture** with four modules:

```
app/          → Application entry point, navigation
ui/           → Jetpack Compose screens, ViewModels (handles SDK exceptions directly)
domain/       → Repository interfaces, domain models (no SDK dependency)
data/         → Repository implementations, SDK wrapper, Hilt modules
```

**Dependency rule**: `app` → `ui` → `domain` ← `data`

The `domain` module never imports SDK types. All SDK-to-domain mapping happens in the `data` layer.

### Repository Pattern

Each SDK feature is wrapped by a repository:

```
AtmFeatures.checkBalance()           → BalanceRepository / BalanceRepositoryImpl
AtmFeatures.transferInquiry/Posting() → TransferRepository / TransferRepositoryImpl
AtmFeatures.getTransactionHistory()  → HistoryRepository / HistoryRepositoryImpl
AtmFeatures.getCardInfo()            → CardRepository / CardRepositoryImpl
AtmFeatures.transactionEvents        → TransactionEventRepository / TransactionEventRepositoryImpl
```

Repository implementations use `runCatching` to wrap SDK calls, returning `kotlin.Result<T>`. SDK exceptions propagate directly to ViewModels — this is intentional so you can see exactly which exceptions the SDK throws and how to handle them.

### Hilt Dependency Injection

All SDK dependencies are wired in `SdkModule.kt` (`data/di/`):

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SdkModule {
    @Provides @Singleton
    fun provideBukuEdcSdk(sdkInitializer: SdkInitializer): BukuEdcSdk

    @Provides @Singleton
    fun provideAtmFeatures(sdk: BukuEdcSdk, tokenProvider: AuthTokenProvider): AtmFeatures

    @Provides @Singleton
    fun provideCardRepository(impl: CardRepositoryImpl): CardRepository
    // ... other repository bindings
}
```

---

## SDK Setup

### Step 1: Add SDK Dependency

The SDK is included as a project dependency. In your app, add:

```gradle
dependencies {
    implementation("com.bukuwarung.edc.sdk:buku-edc-sdk:0.1.3-SNAPSHOT")
}
```

### Step 2: Configure SDK Key

In the sample app, the SDK key is stored in `data/build.gradle.kts`:

```kotlin
buildConfigField("String", "SDK_KEY", "\"sandbox-test-sdk-key-sample\"")
```

Replace with your SDK key. For production, use a secure mechanism (e.g., server-provided key).

### Step 3: Create SdkInitializer

The `SdkInitializer` class (`data/sdk/SdkInitializer.kt`) wraps SDK initialization:

```kotlin
@Singleton
class SdkInitializer @Inject constructor() {
    private lateinit var sdk: BukuEdcSdk

    fun initialize(application: Application) {
        val config = BukuEdcConfig(
            sdkKey = BuildConfig.SDK_KEY,
            environment = BukuEdcEnv.SANDBOX,    // Change to PRODUCTION for release
            testingMock = true,                   // Set to false for real EDC device
            logListener = logListener             // Optional: forwards SDK logs to Logcat
        )
        sdk = BukuEdcSdkFactory.initialize(application, config)
    }

    fun getSdk(): BukuEdcSdk = sdk
}
```

### Step 4: Initialize in Application.onCreate()

**Critical**: SDK initialization must happen on the **Main Thread** in `Application.onCreate()`:

```kotlin
@HiltAndroidApp
class BukuEdcApplication : Application() {
    @Inject lateinit var sdkInitializer: SdkInitializer

    override fun onCreate() {
        super.onCreate()  // Hilt injection happens here
        sdkInitializer.initialize(this)
    }
}
```

This ensures the SDK captures all Activity lifecycle events from startup, which is required for PIN input UI to work correctly.

### Optional: SdkLogListener

Implement `SdkLogListener` to receive SDK debug logs:

```kotlin
private val logListener = object : SdkLogListener {
    override fun onLogEmitted(logEvent: SdkLogEvent) {
        Log.d("BukuEdcSdk", "[${logEvent.level}] ${logEvent.tag}: ${logEvent.message}")
    }
}
```

---

## Authentication

The SDK requires an access token for authenticated operations. You provide a **token provider** function when creating `AtmFeatures`.

### Token Provider Setup

In `SdkModule.kt`, `AtmFeatures` is created with the token provider:

```kotlin
@Provides @Singleton
fun provideAtmFeatures(sdk: BukuEdcSdk, tokenProvider: AuthTokenProvider): AtmFeatures {
    return sdk.getAtmFeatures { tokenProvider.getToken() }
}
```

`AuthTokenProvider` (`data/sdk/AuthTokenProvider.kt`) is a functional interface:

```kotlin
fun interface AuthTokenProvider {
    suspend fun getToken(): String
}
```

### Sample Implementation

The sample app reads the token from DataStore (set via Developer Settings):

```kotlin
@Provides @Singleton
fun provideAuthTokenProvider(settingsRepository: SettingsRepository): AuthTokenProvider {
    return AuthTokenProvider {
        val token = settingsRepository.getAccessToken().first()
        token.ifEmpty { "no-token-configured" }
    }
}
```

### Your Implementation

Replace with your real auth service call:

```kotlin
AuthTokenProvider {
    val response = yourAuthApi.getAccessToken()
    response.token
}
```

**Important**: The SDK enforces a **3-second timeout** on token retrieval. If your token provider takes longer, the SDK throws `TimeoutCancellationException`.

### Token Exchange API

For sandbox testing, obtain tokens via:

```
POST https://api-dev.bukuwarung.com/sdk/v1/token/exchange
```

with `partnerId`, `partnerSecret`, and `partnerUserToken` in the request body.

---

## Balance Check

### SDK API

```kotlin
AtmFeatures.checkBalance(
    accountId: String,
    accountType: AccountType = AccountType.SAVINGS
): Result<CardReceiptResponse>
```

The SDK handles card reading, PIN entry, and backend communication automatically.

### Sample Implementation

**Repository** (`BalanceRepositoryImpl.kt`):

```kotlin
override suspend fun checkBalance(accountId: String, accountType: String): Result<BalanceInfo> =
    runCatching {
        val receipt = atmFeatures.checkBalance(
            accountId = accountId,
            accountType = AccountType.fromString(accountType)
        ).getOrThrow()

        BalanceInfo(
            totalAmount = receipt.totalAmount,
            cardNumber = receipt.cardNumber,
            rrn = receipt.rrn,
            timestamp = receipt.timestamp
            // ... other fields
        )
    }
```

**ViewModel** (`BalanceCheckViewModel.kt`):

```kotlin
balanceRepository.checkBalance(accountId, accountType)
    .onSuccess { balanceInfo ->
        _uiState.value = BalanceUiState.Success(
            balanceAmount = formatRupiah(balanceInfo.totalAmount),
            refNo = balanceInfo.rrn,
            // ...
        )
    }
    .onFailure { error ->
        _uiState.value = BalanceUiState.Error(message = getErrorMessage(error))
    }
```

### Response Fields

From `CardReceiptResponse`:
- `totalAmount` — Account balance
- `cardNumber` — Masked card number
- `rrn` — Reference number
- `timestamp` — Transaction timestamp
- `accountType` — SAVINGS or CHECKING

---

## Transfer

Transfers use a **two-step flow**: inquiry (get fees + token) → posting (execute transfer).

### Step 1: Transfer Inquiry

```kotlin
AtmFeatures.transferInquiry(
    accountId: String,
    amount: BigInteger,
    destinationDetails: BankDetails,
    notes: String = "",
    isCashWithdrawal: Boolean,
    accountType: AccountType = AccountType.SAVINGS
): Result<CardReceiptResponse>
```

The inquiry returns a `CardReceiptResponse` containing:
- `amount`, `adminFee`, `totalAmount` — Fee breakdown for user confirmation
- `transactionToken` — **Single-use token** required for the posting step

### Step 2: Transfer Posting

```kotlin
AtmFeatures.transferPosting(
    accountId: String,
    transactionToken: String  // Token from inquiry step
): Result<CardReceiptResponse>
```

### Token Rules

- Valid for **15 minutes** after inquiry
- Can only be used **once**
- If expired → SDK throws `TokenExpiredException`
- If already used or malformed → SDK throws `InvalidTokenException`
- User must redo the inquiry step in both cases

### Sample Implementation

**Repository** (`TransferRepositoryImpl.kt`):

```kotlin
// Step 1: Inquiry
override suspend fun transferInquiry(...): Result<TransferReceiptInfo> = runCatching {
    val destinationDetails = BankDetails(bankCode = bankCode, bankName = bankName)

    val receipt = atmFeatures.transferInquiry(
        accountId = accountId,
        amount = amount,
        destinationDetails = destinationDetails,
        notes = notes,
        isCashWithdrawal = isCashWithdrawal,
        accountType = AccountType.fromString(accountType)
    ).getOrThrow()

    receipt.toDomain()
}

// Step 2: Posting
override suspend fun transferPosting(
    accountId: String,
    transactionToken: String
): Result<TransferReceiptInfo> = runCatching {
    atmFeatures.transferPosting(accountId, transactionToken).getOrThrow().toDomain()
}
```

**ViewModel** (`TransferConfirmViewModel.kt`):

```kotlin
// On screen open → inquiry
transferRepository.transferInquiry(...)
    .onSuccess { receipt ->
        flowState.inquiryReceipt = receipt  // Save token for posting
        _uiState.value = ConfirmUiState.InquirySuccess(/* fee breakdown */)
    }

// On user confirmation → posting
fun confirmTransfer() {
    val token = flowState.inquiryReceipt?.transactionToken ?: return
    transferRepository.transferPosting(accountId, token)
        .onSuccess { /* navigate to success screen */ }
        .onFailure { error ->
            // TokenExpiredException / InvalidTokenException → user must redo inquiry
            // DeviceSdkException / BackendException → can retry
        }
}
```

### Posting Response Fields

From `CardReceiptResponse`:
- `rrn` — Retrieval Reference Number
- `approvalCode` — Bank approval code
- `totalAmount` — Final transaction amount
- `status` — Transaction status
- `receiptData` — Raw receipt data for printing

---

## Cash Withdrawal

Cash withdrawal uses the **same SDK API as transfer** with `isCashWithdrawal = true`.

### How It Works

```kotlin
// Step 1: Inquiry with isCashWithdrawal = true
atmFeatures.transferInquiry(
    accountId = accountId,
    amount = amount,
    destinationDetails = BankDetails(bankCode, bankName),
    isCashWithdrawal = true,  // ← This is the only difference
    accountType = accountType
)

// Step 2: Posting (identical to transfer)
atmFeatures.transferPosting(accountId, transactionToken)
```

The sample app reuses `TransferRepository` and `TransferConfirmViewModel` for both flows. The `TransferFlowStateHolder.isCashWithdrawal` flag controls which API variant is called.

---

## Transaction History

### SDK API

```kotlin
AtmFeatures.getTransactionHistory(
    filter: TransactionFilter
): Result<TransactionHistory>
```

`TransactionFilter` parameters:
- `accountId` — Required
- `pageNumber` — 0-based page number
- `pageSize` — Items per page (default 20)
- `type` — Optional filter (e.g., "TRANSFER", "BALANCE_INQUIRY", "CASH_WITHDRAWAL")
- `status` — Optional filter (e.g., "SUCCESS", "FAILED", "PENDING")

### Response

`TransactionHistory` contains:
- `history: ArrayList<HistoryItem>` — Each item has nullable fields: `transactionId`, `amount`, `status`, `date`, `type`
- `paginationDetails: PaginationDetails?` — `currentPage`, `totalPage`, `totalCount`

### Sample Implementation

**Repository** (`HistoryRepositoryImpl.kt`):

```kotlin
override suspend fun getTransactionHistory(
    accountId: String, pageNumber: Int, pageSize: Int
): Result<TransactionHistoryInfo> = runCatching {
    val filter = TransactionFilter(
        accountId = accountId,
        pageNumber = pageNumber,
        pageSize = pageSize
    )
    val sdkResult = atmFeatures.getTransactionHistory(filter).getOrThrow()

    // Map nullable SDK fields to non-null domain fields with .orEmpty()
    val items = sdkResult.history.map { item ->
        HistoryItemInfo(
            transactionId = item.transactionId.orEmpty(),
            amount = item.amount.orEmpty(),
            status = item.status.orEmpty(),
            date = item.date.orEmpty(),
            type = item.type.orEmpty()
        )
    }
    TransactionHistoryInfo(items = items, pagination = /* mapped PaginationDetails */)
}
```

**ViewModel** (`HistoryViewModel.kt`) — implements infinite-scroll pagination:

```kotlin
// Load first page in init
historyRepository.getTransactionHistory(accountId, pageNumber = 0, pageSize = 20)

// Load next page when user scrolls near bottom
fun loadNextPage() {
    if (hasMorePages && !isLoadingMore) {
        historyRepository.getTransactionHistory(accountId, pageNumber = currentPage + 1)
    }
}
```

### Pagination

Check `PaginationDetails` to determine if more pages exist:
- If `currentPage < totalPage` → more pages available
- Increment `pageNumber` and call `getTransactionHistory()` again

---

## Incomplete Transactions

### Purpose

If the app was killed during an active transaction (e.g., during card reading or PIN entry), the SDK can detect and report the incomplete transaction on the next app start.

### SDK API

```kotlin
AtmFeatures.checkIncompleteTransactions(): Result<IncompleteTransaction?>
```

Returns `null` if no incomplete transactions exist.

### Sample Implementation

**Repository** (`CardRepositoryImpl.kt`):

```kotlin
override suspend fun checkIncompleteTransactions(): Result<IncompleteTransactionInfo?> =
    runCatching {
        atmFeatures.checkIncompleteTransactions().getOrThrow()?.let { incomplete ->
            IncompleteTransactionInfo(
                transactionId = incomplete.transactionId,
                amount = incomplete.amount,
                type = incomplete.type
            )
        }
    }
```

**ViewModel** (`HomeViewModel.kt`) — called in `init`:

```kotlin
init {
    checkIncompleteTransactions()
}

private fun checkIncompleteTransactions() {
    viewModelScope.launch {
        cardRepository.checkIncompleteTransactions()
            .onSuccess { incomplete ->
                _incompleteTransaction.value = incomplete  // null if none found
            }
        // Errors are silently ignored — this check is best-effort
    }
}
```

**UI** (`HomeScreen.kt`) — shows an `AlertDialog` when incomplete transaction is detected.

### Your Implementation

You may want to add a "Resume" action that navigates the user to the appropriate flow to complete the pending transaction, rather than just dismissing the dialog.

---

## Transaction Events

The SDK emits real-time events during transaction processing via a `SharedFlow`.

### SDK API

```kotlin
AtmFeatures.transactionEvents: SharedFlow<TransactionEvent>
```

### Event Types

| Event | Description | Recommended UI |
|-------|-------------|---------------|
| `WaitingForCard` | SDK is waiting for card insertion | "Insert card" prompt |
| `CardDetected(cardType)` | Card was detected and read | Update card status |
| `EnteringPin` | PIN entry screen is active | "Entering PIN" indicator |
| `ProcessingTransaction(step)` | Backend processing in progress | Loading overlay with step text |
| `TransactionComplete(result)` | Transaction succeeded | Navigate to success screen |
| `TransactionFailed(error, canRetry)` | Transaction failed | Error state with optional retry |

### Sample Implementation

**Repository** (`TransactionEventRepositoryImpl.kt`) maps SDK events to domain events:

```kotlin
class TransactionEventRepositoryImpl @Inject constructor(
    private val atmFeatures: AtmFeatures
) : TransactionEventRepository {
    override val transactionEvents: SharedFlow<TransactionEvent> =
        // Maps SDK TransactionEvent to domain TransactionEvent
}
```

**ViewModel** (`TransferConfirmViewModel.kt`) observes events for loading overlay:

```kotlin
private fun observeTransactionEvents() {
    viewModelScope.launch {
        transactionEventRepository.transactionEvents.collect { event ->
            when (event) {
                is TransactionEvent.ProcessingTransaction ->
                    _processingState.value = ProcessingState(isProcessing = true, currentStep = event.step)
                is TransactionEvent.TransactionComplete ->
                    _processingState.value = ProcessingState(isProcessing = false)
                is TransactionEvent.TransactionFailed ->
                    _uiEvent.emit(UiEvent.ShowError(event.message, event.canRetry))
                else -> {}
            }
        }
    }
}
```

---

## Error Handling

The SDK throws four exception types. The sample app propagates these directly to ViewModels (no domain mapping) so you can see exactly how to handle each one.

### Exception Types

#### DeviceSdkException

EDC device and hardware errors.

| Code | Description | Recommended Action |
|------|-------------|-------------------|
| `E01` | Card read error (not inserted properly or unreadable) | Prompt user to reinsert card |
| `E02` | Card removed prematurely during transaction | Prompt user to keep card inserted |
| `E06` | PIN entry cancelled by cardholder | Allow retry or go back |
| `E21` | Transaction timeout (device or backend unresponsive) | Allow retry |
| `E99` | Unknown device error | Allow retry or contact support |

#### BackendException

Backend processing errors.

| Code | Description | Recommended Action |
|------|-------------|-------------------|
| `30` | Format error (malformed request data) | Check input data, retry |
| `55` | Invalid PIN entered by cardholder | Prompt to re-enter PIN |
| `03` | Invalid merchant (not registered or blocked) | Contact Buku support |

#### TokenExpiredException

The single-use transaction token from `transferInquiry()` has exceeded its **15-minute** validity window.

**Action**: Navigate user back to restart the transfer/withdrawal flow. The inquiry must be redone to obtain a new token.

#### InvalidTokenException

The transaction token is invalid (already used or malformed).

**Action**: Same as `TokenExpiredException` — navigate back and restart the flow.

### Error Handling Pattern

All ViewModels follow the same pattern:

```kotlin
private fun getErrorMessage(error: Throwable): String = when (error) {
    is TokenExpiredException ->
        "Token transaksi sudah kedaluwarsa. Silakan ulangi proses transfer."
    is InvalidTokenException ->
        "Token transaksi tidak valid. Silakan ulangi proses transfer."
    is DeviceSdkException ->
        "Kesalahan perangkat (${error.code}): ${error.message}"
    is BackendException ->
        "Kesalahan server (${error.code}): ${error.message}"
    else ->
        error.message ?: "Terjadi kesalahan. Silakan coba lagi."
}
```

### Reusable Error UI

The sample app provides `ErrorStateComposable.kt` (`ui/common/`) — a reusable Compose component:

```kotlin
ErrorState(
    title = "Transaksi Gagal",
    message = getErrorMessage(error),
    onRetry = { viewModel.retry() },      // For transient errors (device/backend)
    onBack = { navController.popBackStack() } // For token errors (must restart flow)
)
```

- Pass `onRetry` for `DeviceSdkException` and `BackendException` (transient, may succeed on retry)
- Pass `onBack` for `TokenExpiredException` and `InvalidTokenException` (must restart flow)

### Error Handling by Flow

| Flow | Possible Exceptions | Token Errors? |
|------|-------------------|---------------|
| Balance Check | `DeviceSdkException`, `BackendException` | No |
| Transfer (inquiry) | `DeviceSdkException`, `BackendException` | No |
| Transfer (posting) | All four types | Yes |
| Cash Withdrawal | Same as transfer | Yes |
| Transaction History | `BackendException` | No |
| Card Read | `DeviceSdkException` | No |

---

## Key Files Reference

| File | Purpose |
|------|---------|
| `data/sdk/SdkInitializer.kt` | SDK initialization wrapper |
| `data/sdk/AuthTokenProvider.kt` | Token provider interface |
| `data/di/SdkModule.kt` | Hilt bindings for all SDK dependencies |
| `data/card/CardRepositoryImpl.kt` | Card info + incomplete transaction detection |
| `data/transaction/BalanceRepositoryImpl.kt` | Balance check via SDK |
| `data/transaction/TransferRepositoryImpl.kt` | Transfer inquiry + posting via SDK |
| `data/transaction/HistoryRepositoryImpl.kt` | Transaction history via SDK |
| `data/transaction/TransactionEventRepositoryImpl.kt` | Real-time event streaming |
| `ui/balance/BalanceCheckViewModel.kt` | Balance check orchestration + error handling |
| `ui/transfer/TransferConfirmViewModel.kt` | Transfer/withdrawal orchestration + error handling |
| `ui/history/HistoryViewModel.kt` | History pagination + error handling |
| `ui/common/ErrorStateComposable.kt` | Reusable error UI component |
| `app/BukuEdcApplication.kt` | SDK initialization entry point |
