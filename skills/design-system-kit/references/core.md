# core — tokens and theme

`io.github.amine2233:designsystem-core`. Everything else depends on it and nothing else may hold a raw value.
Changing the brand means editing this layer only.

## DsTheme

```kotlin
DsTheme(                                   // wrap the app once
    darkTheme = isSystemInDarkTheme(),     // override to force a theme
    animationsEnabled = true,              // false in tests/snapshots: no indeterminate animation
) { content() }
```

Read tokens through the `DsTheme` object inside composables:

| Accessor | Gives |
|---|---|
| `DsTheme.colors` | `DsColorScheme` for the active theme |
| `DsTheme.typography` | `DsTypography` |
| `DsTheme.spacing` | `DsSpacing` |
| `DsTheme.shapes` | `DsShapes` |
| `DsTheme.windowSize` | `DsWindowSize.Compact` / `.Expanded` for the current window |
| `DsTheme.animationsEnabled` | whether decorative animation should run |

`DsTheme` also bridges to `MaterialTheme`, so Material primitives used inside the design system (sheets, dialogs,
ripples, the date/time calendars) follow the palette without further configuration.

## DsColorScheme

Semantic roles, never raw hues. `DsLightColors` / `DsDarkColors` are the two instances.

- Brand: `primary`, `primaryPressed`, `onPrimary`, `primaryContainer`, `primaryContainerBorder`, `primaryTint`
- Surfaces: `background`, `surface`, `surfaceSubtle`, `surfaceMuted`, `scrim`
- Text: `textPrimary`, `textSecondary`, `textTertiary`, `textDisabled`
- Lines: `border`, `borderStrong`
- Status: `error` / `errorContainer` / `errorBorder`, `warning` / `warningContainer` / `warningBorder`,
  `success` / `successContainer`, `info` / `infoContainer`

`DsPalette` holds the raw hues (`Teal700`, `Grey500`, `Red500`, …) and is **not** for component code — go through
`DsColorScheme`. Its one public list for UI is `DsPalette.swatches`, the palette offered by the color picker.

## DsTypography

`displayLarge`, `displayMedium`, `headline`, `titleLarge`, `title`, `bodyLarge`, `body`, `bodyStrong`, `label`,
`labelStrong`, `caption`, `overline`. Font family: `DsFontFamily` (sans-serif).

## DsSpacing

4dp scale: `xxs` 2, `xs` 4, `sm` 8, `smd` 12, `md` 16, `mlg` 20, `lg` 24, `xl` 32, `xxl` 48, `xxxl` 64.
Named: `screen` 16, `card` 16, `minTouchTarget` 48 — every tappable row uses at least `minTouchTarget`.

## DsShapes

`xs` 4, `sm` 8, `md` 12, `lg` 16, `xl` 24, `pill`, `sheet` (top corners only).
Named: `button` = pill, `chip` = pill, `card` = md, `input` = lg, `key` = md.

## DsElevation / DsMotion

`DsElevation`: `none`, `card` 2dp, `raised` 4dp, `sheet` 8dp.
`DsMotion`: `FAST` 150ms, `NORMAL` 250ms, `SLOW` 400ms, `standardEasing`, `emphasizedEasing`. No decorative
animation beyond these.

## DsIcons

The curated set — product code uses these names, not `Icons.Filled.*`, so the whole app re-skins from one file.

- Navigation: `Back`, `Close`, `ChevronRight`, `ChevronDown`, `ChevronUp`, `More`, `Home`, `Settings`, `Search`, `Account`
- Actions: `Add`, `Remove`, `Edit`, `Delete`, `Check`, `Backspace`, `Hold`, `Print`
- Forms: `Lock`, `Reveal`, `Hide`, `Calendar`, `Phone`, `Clock`, `Palette`
- POS: `Cart`, `Card`, `Cash`, `Contactless`, `Split`, `Discount`, `Tip`, `Receipt`, `Register`, `Catalogue`, `Store`, `Coffee`, `Image`
- Status: `Info`, `Success`, `Warning`, `Error`, `Offline`

Need an icon that is not here? Add it to `DsIcons` rather than importing a Material icon in a screen.

## DsImages

Vector drawables shipped in `core/res`: `DsImages.Logo`, `DsImages.ProductPlaceholder`,
`DsImages.Illustrations.{EmptyCart, Error, Offline}`.

## DsWindowSize

`Compact` below 600dp, `Expanded` at or above (`DsWindowSize.expandedBreakpoint`). `isExpanded` is the property to
branch on. Screens take it as a parameter defaulting to `DsTheme.windowSize` so previews and snapshots can force a
size.

## DsPreview

`DsPreview { }` wraps preview content in the theme, background and padding.
`@DsComponentPreview` = light + dark at 375dp. `@DsScreenPreview` = phone + tablet devices.
