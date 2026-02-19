# sdk-integration Specification

## Purpose
TBD - created by archiving change add-sdk-integration. Update Purpose after archive.
## Requirements
### Requirement: Initialize SDK via SdkInitializer Wrapper

The system SHALL provide an `SdkInitializer` class in the `data` layer that wraps
`BukuEdcSdk.initialize()` with a `BukuEdcConfig` containing the SDK key, `BukuEdcEnv.SANDBOX`
environment, and an optional `SdkLogListener`. The Application class delegates to `SdkInitializer`
instead of calling the SDK directly, enabling clean Hilt integration.

#### Scenario: App cold start initializes SDK via wrapper

- **WHEN** the application process starts
- **THEN** `SdkInitializer.initialize()` is called from the Main Thread in `Application.onCreate()`
- **AND THEN** the `SdkInitializer` provides the `BukuEdcSdk` instance for Hilt injection

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

### Requirement: Use Raw SDK Exceptions for Error Handling

The system SHALL propagate SDK exception types directly via `kotlin.Result<T>` without mapping
them to domain error types. ViewModels handle raw SDK exceptions (`DeviceSdkException`,
`BackendException`, `TokenExpiredException`, `InvalidTokenException`) so partners can see exactly
how to handle each exception in their own apps.

#### Scenario: Device error handling

- **WHEN** an SDK operation fails with `DeviceSdkException` (codes: E01 card read, E02 card
  removed, E06 PIN cancelled, E21 timeout, E99 unknown)
- **THEN** the ViewModel handles `DeviceSdkException` and displays an error with the code
  and message

#### Scenario: Backend error handling

- **WHEN** an SDK operation fails with `BackendException` (codes: 30 format error, 55 invalid
  PIN, 03 invalid merchant)
- **THEN** the ViewModel handles `BackendException` and displays an error with the code
  and message

#### Scenario: Token expired error handling

- **WHEN** `transferPosting()` fails with `TokenExpiredException`
- **THEN** the ViewModel handles `TokenExpiredException` and prompts the user to re-do the
  inquiry step

#### Scenario: Invalid token error handling

- **WHEN** `transferPosting()` fails with `InvalidTokenException`
- **THEN** the ViewModel handles `InvalidTokenException` and prompts the user to re-do the
  inquiry step

### Requirement: Provide Repository Implementations in Data Module

The system SHALL implement repository interfaces in the `data` module that delegate to `AtmFeatures`
suspend functions. Since the SDK is coroutine-native, repositories directly call `AtmFeatures`
methods and map results — no callback wrapping is needed.

#### Scenario: Repository delegates to AtmFeatures

- **WHEN** a use case invokes a repository method
- **THEN** the repository calls the corresponding `AtmFeatures` suspend function
- **AND THEN** the `kotlin.Result<T>` response is returned directly, with raw SDK exceptions
  preserved on failure for the ViewModel to handle

### Requirement: Provide Hilt Module for SDK Dependencies

The system SHALL provide a Hilt module in the `data` layer that provides the `SdkInitializer`
singleton, `BukuEdcSdk` instance (via `SdkInitializer`), `AtmFeatures` instance (with token
provider), and all repository implementation bindings.

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

