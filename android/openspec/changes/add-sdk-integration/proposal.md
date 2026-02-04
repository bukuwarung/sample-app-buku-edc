# Change: Integrate SDK for Partner Integration Sample App

## Why

The sample app currently uses mock data throughout all transaction flows (Transfer, Balance Check,
Cash Withdrawal). Partners need a reference implementation that demonstrates **real SDK integration**
to reduce integration time and support requests. The tech lead has directed that the data module
should be built out with SDK integration (BUKU-12647 comment).

## What Changes

- **SDK Initialization**: Add proper SDK initialization with test credentials in the Application
  class
- **Data Layer Repositories**: Create repository implementations in the `data` module that wrap SDK
  calls (replacing the current mock-based `FakeSettingsRepository` pattern)
- **Device Activation Flow**: Connect the existing `ActivationScreen` UI to the SDK's device
  activation API
- **Transaction Flows Integration**: Wire Transfer, Balance Check, and Cash Withdrawal flows to use
  SDK transaction APIs instead of mock data
- **Transaction History**: Add new feature to retrieve and display transaction history via SDK
- **Bank Account Registration**: Connect first-time user flow to SDK's bank registration API
- **Error Handling**: Add comprehensive error handling patterns demonstrating SDK error states to
  partners

## Impact

- **Affected specs**: `transfer-flow`, `balance-check-flow`, `cash-withdrawal-flow`, `settings-flow`,
  `homepage-screen` (all will be MODIFIED to use SDK data instead of mocks)
- **New specs**: `sdk-integration` (initialization, error handling), `transaction-history`
- **Affected code**:
  - `data/` module: New repositories, SDK wrappers, DI modules
  - `domain/` module: New use cases, repository interfaces
  - `app/` module: SDK initialization, navigation for history
  - `ui/` module: History screen, error state UI components

## Out of Scope

- White-labeling customization (Phase 2 per PRD)
- Production-ready UI design refinements
- Offline mode / caching strategies
- Real payment processing (test credentials only)
