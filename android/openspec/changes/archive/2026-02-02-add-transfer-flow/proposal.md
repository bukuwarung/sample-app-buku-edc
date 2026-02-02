# Change: Add Transfer Flow from Home

## Why

Users need the Transfer action to open the multi-screen flow defined in Figma, instead of showing a
toast only.

## What Changes

- Add a new Transfer flow with mock data only and a linear multi-screen sequence matching Figma.
- Use single-activity navigation to move through the Transfer screens and return to Home at the end.
- Apply MVVM with Hilt for each Transfer screen.
- Update Home action handling so Transfer navigates to the flow while other actions keep toast
  feedback.

## Impact

- Affected specs: `specs/home-mvvm/spec.md` (modified), new `specs/transfer-flow/spec.md`.
- Affected code: Home UI action handling, navigation setup, new Transfer screens and view models in
  the UI module.