# kotlin-design-system-kit — working rules

Compose-only design system, atomic design, screenshot-tested. `README.md` has the inventory; this file has the rules.

## Strategy

- **Tokens live in `designsystem/core` only.** Colors, spacing, type, shapes, motion, elevation, icons, images. No hardcoded `dp`, `Color`, `sp` or literal duration anywhere else — read `DsTheme.colors / typography / spacing / shapes / motion`. Adding a value means adding a token first.
- **Atomic layers, one direction.** `core ← atoms ← molecules ← organisms ← templates ← catalog ← sample`. Put a component at the lowest layer that can hold it; never import sideways or upwards. A component that needs a peer from its own layer belongs one layer up.
- **Compose only.** No View/XML, no `AndroidView`, no Material components leaking through a public API — wrap them in a `Ds*` composable.
- **Public API is `Ds*`.** Stateless composables: state in, lambdas out. No ViewModel, no navigation, no domain types, no I/O inside the design system.
- **Adaptive over duplicated.** One composable branching on `DsTheme.windowSize`, not a phone copy and a tablet copy.

## Every component ships with

1. The composable in its layer's package.
2. A `@Preview` (light + dark; add RTL / Expanded when the layout changes).
3. A catalog entry in `designsystem/catalog` — that gallery is what the screenshot tests render.
4. Reference screenshots recorded via `mise run snapshot:update`, reviewed before committing.
5. KDoc on the public composable: what it is for, and which layer/variant to use instead when it is the wrong choice.

Animated components take a `progress` / `animate` override so snapshots stay deterministic.

## Definition of done

`mise run check` green (ktlint + build + unit tests + screenshot regression), conventional commit (`feat:` / `fix:` drive the release).

## Don't

- Add a dependency without asking.
- Expose a component with no preview and no snapshot.
- Change reference PNGs without an intended visual change in the same commit.
- Break the module direction to "just reuse" something.
