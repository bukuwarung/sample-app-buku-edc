## Context
The Balance Check flow is defined in the Figma node "Balance Check" and shares early steps with the existing Transfer flow (select account, insert/ swipe card, card info, and PIN). The current implementation only navigates to Transfer from Home; other actions show toasts.

## Goals / Non-Goals
- Goals:
  - Start Balance Check when users tap "Cek Saldo" from Home.
  - Reuse existing Transfer screen composables by adding a Balance Check variant for titles, copy, and CTA behavior.
  - Keep the Transfer flow sequence and UI unchanged.
- Non-Goals:
  - Implement real balance APIs or backend integrations.
  - Add new payment or withdrawal features.

## Decisions
- Decision: Introduce a `FlowVariant` (e.g., `Transfer` vs `BalanceCheck`) shared by Transfer screens used in both flows.
  - Why: Reuses existing screens while allowing text/CTA differences required by the Balance Check design.
- Decision: Reuse existing screens wherever possible, and add Balance Check variants as needed for new copy and layout differences.
  - Why: Keeps the existing Transfer flow intact while allowing future Balance Check variants without duplicating screens.

## Risks / Trade-offs
- Risk: Overloading Transfer screens with too many variant-specific branches.
  - Mitigation: Keep variants limited to text, CTA labels, and navigation targets; avoid structural divergence.

## Migration Plan
1. Add Balance Check routing and Home navigation event.
2. Extend reusable screens with `FlowVariant` and mock data wiring.
3. Verify Transfer flow still matches existing behavior.

## Open Questions
- None at this time.