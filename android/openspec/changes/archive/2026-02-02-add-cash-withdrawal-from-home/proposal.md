# Change: Add Cash Withdrawal navigation from Home "Tarik Tunai"

## Why

Users need to access the Cash Withdrawal flow directly from the Home action grid by tapping
“Tarik Tunai”, consistent with the intended product navigation and the provided design.

## What Changes

- Add navigation for the Home action tile “Tarik Tunai” to open the Cash Withdrawal flow entry
  screen (Figma “Cash withdrawal” node `33:2198`).
- Define the Cash Withdrawal flow capability and its expected screen sequence (based on the same
  Figma node).
- Capture the **First Time User** decision (shown in the Figma flowchart) by adding a first-time
  user onboarding/prompt screen before proceeding into the main Cash Withdrawal flow.
- Update Home MVVM behavior so “Tarik Tunai” no longer triggers the non-navigation toast feedback.

## Impact

- **Affected specs**: `homepage-screen`, `home-mvvm`, `cash-withdrawal-flow` (new capability)
- **Affected code (expected)**:
    - Home action handling: `HomeViewModel`, `HomeScreen`
    - App navigation: `MainActivity` NavHost and `ui/navigation/Routes.kt`
    - New/updated Cash Withdrawal screens (UI module), potentially by extending `FlowVariant`
