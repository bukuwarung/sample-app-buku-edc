## Context

The app uses a Compose `NavHost` with routes defined in `ui/navigation/Routes.kt`. The Home screen
delegates action clicks to a ViewModel (`home-mvvm`), which emits UI events for navigation and
toast feedback.

The Figma node `33:2198` includes a **flowchart decision** labelled “First Time User”, indicating a
separate first-time path (a prompt to add a destination bank account) before entering the main flow.

## Goals / Non-Goals

- Goals:
    - Provide a first-class navigation path from Home “Tarik Tunai” into the Cash Withdrawal flow.
    - Define the flow entry and expected screen sequence according to Figma node `33:2198`.
    - Model the “First Time User” decision in a simple, testable way using mocked state (e.g.,
      whether
      a saved destination bank account exists).
- Non-Goals:
    - Redesigning the Cash Withdrawal UI beyond what’s specified in the Figma node.
    - Implementing real backend/API integration (use mock data like other flows unless requested).

## Decisions

- **Navigation ownership**: Home ViewModel emits a dedicated navigation event for “Tarik Tunai”,
  and the UI layer performs navigation via the existing callback pattern.
- **Routing**: Add a dedicated `Screen.CashWithdrawal...` route(s) under `ui/navigation/Routes.kt`
  and wire them into the `NavHost`.
- **First-time user gating**: On entering Cash Withdrawal, determine whether the user is “first
  time”
  (no saved destination bank account). If first time, show the prompt screen first; otherwise go
  directly to the entry screen.
- **Flow implementation**: Prefer reusing existing “transfer-like” screen components when they
  already match the Cash Withdrawal design, by extending `FlowVariant` to include a cash
  withdrawal variant. If reuse introduces UI copy mismatches, introduce a minimal variant-specific
  copy override rather than duplicating screens.

## Risks / Trade-offs

- **Design/copy mismatches**: The Figma node uses some “Transfer” labels within the Cash Withdrawal
  section; reuse may expose these inconsistencies. The implementation should follow the spec + the
  design node as the source of truth and only deviate if clarified.

## Open Questions

- Should the confirmation/success labels be “Tarik Tunai” (withdrawal) or remain “Transfer” as
  shown in parts of the Figma node? (Proposal assumes “follow Figma `33:2198`”.)
