## ADDED Requirements

### Requirement: Initialize SDK on Application Start

The system SHALL initialize `BukuEdcSdk` in the Application class using `BukuEdcSdk.initialize()`
with a `BukuEdcConfig` containing the SDK key, `BukuEdcEnv.SANDBOX` environment, and an optional
`SdkLogListener`, before any SDK feature is used.

#### Scenario: App cold start initializes SDK

- **WHEN** the application process starts
- **THEN** `BukuEdcSdk.initialize(application, bukuEdcConfig)` is called from the Main Thread in
  `Application.onCreate()`
- **AND THEN** the `BukuEdcSdk` instance is stored for Hilt injection

#### Scenario: SDK initialization failure

- **WHEN** the application starts and `BukuEdcSdk.initialize()` throws an exception
- **THEN** the system logs the error via `SdkLogListener`
- **AND THEN** the app displays an error state indicating SDK unavailability

### Requirement: Provide Token Provider for AtmFeatures

The system SHALL provide a `suspend () -> String` token provider function when creating
`AtmFeatures` via `BukuEdcSdk.getAtmFeatures()`. The token provider retrieves an access token
for authenticated SDK operations and must complete within 3 seconds.

#### Scenario: AtmFeatures creation with token provider

- **WHEN** the app creates an `AtmFeatures` instance
- **THEN** it calls `sdk.getAtmFeatures { tokenProvider() }` with a suspend function that returns
  an access token string

#### Scenario: Token provider timeout

- **WHEN** the token provider takes longer than 3 seconds
- **THEN** the SDK throws `TimeoutCancellationException`

### Requirement: Map SDK Exceptions to Domain Error Types

The system SHALL map SDK exception types to a domain-friendly `SdkError` sealed class in the domain
layer, categorizing errors for UI consumption without leaking SDK types.

#### Scenario: Device error mapping

- **WHEN** the SDK throws `DeviceSdkException` (codes: E01 card read, E02 card removed, E06 PIN
  cancelled, E21 timeout, E99 unknown)
- **THEN** the repository maps it to `SdkError.Device` with the error code and message

#### Scenario: Backend error mapping

- **WHEN** the SDK throws `BackendException` (codes: 30 format error, 55 invalid PIN, 03 invalid
  merchant)
- **THEN** the repository maps it to `SdkError.Backend` with the error code and message

#### Scenario: Token expired error mapping

- **WHEN** the SDK throws `TokenExpiredException`
- **THEN** the repository maps it to `SdkError.TokenExpired`

#### Scenario: Invalid token error mapping

- **WHEN** the SDK throws `InvalidTokenException`
- **THEN** the repository maps it to `SdkError.InvalidToken`

### Requirement: Provide Repository Implementations in Data Module

The system SHALL implement repository interfaces in the `data` module that delegate to `AtmFeatures`
suspend functions. Since the SDK is coroutine-native, repositories directly call `AtmFeatures`
methods and map results — no callback wrapping is needed.

#### Scenario: Repository delegates to AtmFeatures

- **WHEN** a use case invokes a repository method
- **THEN** the repository calls the corresponding `AtmFeatures` suspend function
- **AND THEN** the `kotlin.Result<T>` response is returned, with SDK exceptions mapped to domain
  error types on failure

### Requirement: Provide Hilt Module for SDK Dependencies

The system SHALL provide a Hilt module in the `data` layer that provides the `BukuEdcSdk` singleton,
`AtmFeatures` instance (with token provider), and all repository implementation bindings.

#### Scenario: Repository injection

- **WHEN** a use case or ViewModel requests a repository via Hilt
- **THEN** the correct implementation backed by `AtmFeatures` is provided

### Requirement: Monitor Transaction Events via SDK

The system SHALL observe `AtmFeatures.transactionEvents: SharedFlow<TransactionEvent>` to provide
real-time transaction progress updates to the UI.

#### Scenario: Card waiting event

- **WHEN** the SDK emits `TransactionEvent.WaitingForCard`
- **THEN** the UI shows a "waiting for card" state

#### Scenario: Card detected event

- **WHEN** the SDK emits `TransactionEvent.CardDetected(cardType)`
- **THEN** the UI updates to show the detected card type

#### Scenario: PIN entry event

- **WHEN** the SDK emits `TransactionEvent.EnteringPin`
- **THEN** the UI shows a PIN entry in-progress state

#### Scenario: Processing event

- **WHEN** the SDK emits `TransactionEvent.ProcessingTransaction(step)`
- **THEN** the UI shows the current processing step

#### Scenario: Transaction complete event

- **WHEN** the SDK emits `TransactionEvent.TransactionComplete(result)`
- **THEN** the UI navigates to the success screen with `CardReceiptResponse` data

#### Scenario: Transaction failed event

- **WHEN** the SDK emits `TransactionEvent.TransactionFailed(error, canRetry)`
- **THEN** the UI shows an error state with retry option if `canRetry` is true

### Requirement: Check Incomplete Transactions on App Start

The system SHALL call `AtmFeatures.checkIncompleteTransactions()` when the app starts to detect
any pending transactions that need to be completed.

#### Scenario: Incomplete transaction found

- **WHEN** the app starts and `checkIncompleteTransactions()` returns a non-null
  `IncompleteTransaction`
- **THEN** the UI prompts the user to resume the pending transaction

#### Scenario: No incomplete transactions

- **WHEN** the app starts and `checkIncompleteTransactions()` returns null
- **THEN** the app proceeds normally without interruption
