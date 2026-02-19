# partner-integration-guide Specification

## Purpose
TBD - created by archiving change add-sdk-integration. Update Purpose after archive.
## Requirements
### Requirement: Provide Partner Integration Guide in the Sample App

The system SHALL include a comprehensive `INTEGRATION_GUIDE.md` document in the sample app root
that serves as the primary reference for partner developers integrating the Buku EDC SDK into their
own applications.

#### Scenario: Partner reads guide to start integration

- **WHEN** a partner developer opens the sample app repository
- **THEN** they find `INTEGRATION_GUIDE.md` at the project root
- **AND THEN** the guide covers the full integration journey from setup to production

#### Scenario: Guide covers SDK initialization

- **WHEN** a partner reads the initialization section
- **THEN** they find step-by-step instructions for adding the SDK dependency, creating
  `BukuEdcConfig` (with `sdkKey`, `BukuEdcEnv`, optional `SdkLogListener`), and calling
  `BukuEdcSdk.initialize()` from `Application.onCreate()` on the Main Thread

#### Scenario: Guide covers token provider setup

- **WHEN** a partner reads the authentication section
- **THEN** they find instructions for implementing the `suspend () -> String` token provider
  required by `AtmFeatures`, including the 3-second timeout constraint and how to connect it
  to their backend auth service

#### Scenario: Guide covers transaction flows

- **WHEN** a partner reads the transaction section
- **THEN** they find documented examples for each SDK operation:
  - Balance check via `checkBalance()`
  - Transfer via the two-step `transferInquiry()` → `transferPosting()` flow
  - Cash withdrawal via `transferInquiry(isCashWithdrawal = true)` → `transferPosting()`
  - Transaction history via `getTransactionHistory()` with pagination
  - Card reading via `getCardInfo()`
  - Incomplete transaction recovery via `checkIncompleteTransactions()`

#### Scenario: Guide covers error handling

- **WHEN** a partner reads the error handling section
- **THEN** they find a reference of SDK exception types (`DeviceSdkException`,
  `BackendException`, `TokenExpiredException`, `InvalidTokenException`) with error codes,
  descriptions, and recommended handling strategies

#### Scenario: Guide covers transaction event monitoring

- **WHEN** a partner reads the event monitoring section
- **THEN** they find instructions for observing `transactionEvents: SharedFlow<TransactionEvent>`
  with descriptions of each event type (`WaitingForCard`, `CardDetected`, `EnteringPin`,
  `ProcessingTransaction`, `TransactionComplete`, `TransactionFailed`)

### Requirement: Provide README with Project Setup Instructions

The system SHALL include a `README.md` at the project root that explains how to build and run
the sample app, including prerequisites, SDK dependency configuration, and test credentials.

#### Scenario: Partner sets up the sample app locally

- **WHEN** a partner clones the sample app repository
- **THEN** the `README.md` provides instructions for building and running the app
- **AND THEN** includes prerequisites (Android API level, Kotlin version, Gradle setup)
  and test environment configuration

#### Scenario: README references the integration guide

- **WHEN** a partner reads the README
- **THEN** it links to `INTEGRATION_GUIDE.md` for detailed SDK integration documentation

### Requirement: Provide Inline Code Comments at Key Integration Points

The system SHALL include explanatory inline comments in the sample app source code at every key
SDK integration point, so partners can understand the integration by reading the code directly.

#### Scenario: Partner reads source code for integration reference

- **WHEN** a partner browses the sample app source code
- **THEN** key integration points (SDK initialization, token provider, repository calls,
  error handling, event observation) have inline comments explaining the purpose, SDK method
  being called, and any important constraints (e.g., token timeout, Main Thread requirement)

### Requirement: Provide Architecture Overview for Partners

The system SHALL include an architecture section in the integration guide that explains how the
sample app structures SDK integration using Clean Architecture (data/domain/UI layers), so
partners can understand the recommended patterns.

#### Scenario: Partner reviews architecture section

- **WHEN** a partner reads the architecture overview
- **THEN** they understand how `AtmFeatures` is wrapped by repository interfaces in the domain
  layer, implemented in the data layer, and consumed by ViewModels in the UI layer
- **AND THEN** they can apply the same pattern to their own app architecture

