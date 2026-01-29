## Context
The Home screen requires MVVM scaffolding to enable future feature development while keeping UI event handling predictable and testable.

## Goals / Non-Goals
- Goals: wire ActionItem clicks through a Home view model, provide action-specific toast feedback.
- Non-Goals: implement business logic beyond click handling, add navigation or data fetching.

## Decisions
- Decision: Introduce a Home view model that exposes a one-shot UI event for action-specific toast messages.
- Alternatives considered: Trigger toasts directly in composables (rejected to preserve MVVM separation).

## Risks / Trade-offs
- Risk: Event handling can be mis-consumed on configuration changes.
  - Mitigation: Use a one-shot event pattern appropriate for Compose (e.g., event stream or state wrapper).

## Migration Plan
- Add view model and UI event pipeline.
- Update Home UI to delegate clicks and display toast events.

## Open Questions
- None.