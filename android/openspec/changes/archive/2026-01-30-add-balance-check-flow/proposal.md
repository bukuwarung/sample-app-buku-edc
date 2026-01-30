# Change: Add Balance Check flow from Home action

## Why
Users need the "Cek Saldo" action to open the Balance Check flow instead of showing a toast, matching the provided Figma flow while preserving the existing Transfer flow behavior.

## What Changes
- Add a new Balance Check flow that reuses existing Transfer screens with a Balance Check variant (titles, copy, and CTA differences).
- Emit a Home navigation event for "Cek Saldo" and route it to the Balance Check flow sequence.
- Provide mock balance/receipt data for the Balance Check summary and receipt preview screens.

## Impact
- Affected specs: `specs/home-mvvm/spec.md`, new `specs/balance-check-flow/spec.md`
- Affected code: Home navigation/view model events, navigation routes, Transfer screen composables/view models to support a Balance Check variant.