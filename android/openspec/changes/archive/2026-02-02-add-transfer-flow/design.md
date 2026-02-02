## Context

The Home Transfer action currently emits a toast only. The requested experience is a linear,
multi-screen Transfer flow that matches the Figma sequence and returns to Home at the end.

## Goals / Non-Goals

- Goals:
    - Implement a single-activity Transfer flow that matches the exact Figma screen order (skipping
      the receipt screen).
    - Apply MVVM with Hilt for each screen in the flow.
    - Use mock data only.
- Non-Goals:
    - Real transfer processing, backend integration, or persistence.
    - Receipt printing or rendering.

## Decisions

- Decision: Use a dedicated Transfer navigation graph within the existing single-activity setup.
    - Alternatives considered: Separate activity or multiple flows per feature. Rejected to keep
      navigation consistent and simple.
- Decision: One ViewModel per screen injected with Hilt.
    - Alternatives considered: Single shared ViewModel across the flow. Rejected to keep screen
      logic isolated and minimal.
- Decision: Mock data lives in UI layer for now, owned by each screen ViewModel.
    - Alternatives considered: Shared repository layer. Rejected to avoid premature complexity for
      mock-only scope.

## Risks / Trade-offs

- Mock data can diverge from eventual domain models; mitigate by keeping data structures simple and
  documented.

## Migration Plan

1. Add navigation graph and screens with mock data.
2. Wire Home Transfer action to navigate into the flow.
3. Validate flow sequence and return to Home.

## Open Questions

- None at this stage.