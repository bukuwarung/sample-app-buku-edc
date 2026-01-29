# Design: Homepage Screen

## Architecture
The homepage will be implemented as a single Composable function in the `ui` module. It will follow the existing project structure and use Material 3 components. **Implementation MUST reference the Figma design "Homepage-Master" for precise measurements and styling.**

## UI Components
- **Background**: A full-screen `Box` or `Column` with color `#009978`.
- **Title**: A `Text` component "Client Interface" centered at the top.
- **Action Card**: A `Card` (or `Surface` with rounded corners) containing:
    - A header row with "MiniATM" title and icons (Bluetooth, Printer).
    - A `FlowRow` or a `VerticalGrid` for action items.
- **Action Item**: A custom Composable consisting of an `Icon` in a rounded square and a `Text` label.

## Integration
`MainActivity` will be modified to call `HomeScreen` instead of the current `Greeting`.
