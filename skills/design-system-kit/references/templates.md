# templates — page skeletons

`io.github.amine2233:designsystem-templates`. Depends on `organisms`. One dependency line pulls the whole system:

```kotlin
implementation("io.github.amine2233:designsystem-templates:<version>")
```

Templates own the page layout and the phone/tablet split. A screen should start with one of these rather than a
bare `Column`.

```kotlin
DsScreenScaffold(topBar = {}, bottomPanel = null) { content() }
DsNavigationScaffold(topItems, selectedIndex, onSelect, bottomItems = emptyList(),
                     windowSize = DsTheme.windowSize, railHeader = null) { content() }
DsTwoPaneLayout(windowSize = DsTheme.windowSize, secondaryWidth = 320.dp,
                primary = {}, secondary = {}, compactSecondary = {})
DsCenteredLayout(maxWidth = 520.dp) { content() }
```

| Template | Use for | Adaptive behaviour |
|---|---|---|
| `DsScreenScaffold` | any ordinary screen: top bar, scrolling body, optional sticky bottom panel | — |
| `DsNavigationScaffold` | the app shell around the whole navigation | rail at the start edge on Expanded (left LTR / right RTL), bottom tab bar on Compact |
| `DsTwoPaneLayout` | master + detail, catalogue + cart | both panes side by side on Expanded; `compactSecondary` (a bottom bar, a sheet trigger) on Compact |
| `DsCenteredLayout` | onboarding, sign-in, loading, empty shells | content capped and centered, same composable on both sizes |

`windowSize` defaults to `DsTheme.windowSize`; pass it explicitly only so a preview or a snapshot can force
`DsWindowSize.Compact` / `.Expanded` without resizing the device.

Nesting: `DsNavigationScaffold` wraps the whole app, `DsScreenScaffold` wraps each screen inside it, and
`DsTwoPaneLayout` goes in the scaffold's content when a screen has two panes.

```kotlin
DsNavigationScaffold(topItems = tabs, selectedIndex = tab, onSelect = { tab = it }) {
    DsScreenScaffold(
        topBar = { DsTopBar("Vente", badge = count) },
        bottomPanel = { DsBottomCartBar(count, total, onExpand = {}, onPay = {}) },
    ) {
        DsTwoPaneLayout(
            primary = { ProductGrid() },
            secondary = { CartPane() },
            compactSecondary = { },   // the cart is already in the bottom panel on phone
        )
    }
}
```
