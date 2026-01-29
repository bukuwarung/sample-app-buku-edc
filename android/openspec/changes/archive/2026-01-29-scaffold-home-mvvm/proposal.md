# Change: Scaffold Home MVVM action clicks

## Why
Home action tiles currently lack MVVM wiring, which blocks feature development that depends on view model-driven behavior and user feedback.

## What Changes
- Add a Home MVVM scaffold for action clicks so the UI delegates user events to the view model.
- Emit action-specific toast events from the view model for immediate user feedback.

## Impact
- Affected specs: `home-mvvm` (new)
- Affected code: `HomeScreen` UI, new Home view model, UI event handling