# kotlin-design-system-kit

Jetpack Compose design system extracted from the *Caisse Pro* POS designs (Claude Design project).
Atomic-design modules, phone + tablet adaptive layouts, screenshot-tested.

## Modules

```
designsystem/
  core        tokens (DsPalette, DsColorScheme light/dark, DsTypography, DsSpacing, DsShapes, DsMotion), DsTheme, DsWindowSize,
              DsIcons (curated icon set), DsImages (logo, product placeholder, illustrations — vector drawables in res/)
  atoms       DsText, DsButton (loading state) / DsIconButton, DsChip, DsTag, DsBadge, DsAvatar, DsIconTile, DsCard, DsDivider,
              DsTextField, DsErrorText, DsSwitch / DsCheckbox / DsRadioButton, DsProgress, DsSkeleton, DsImage / DsImageSlot / DsIllustration / DsLogo
  molecules   DsQuantityStepper, DsSegmentedControl, DsTabRow, DsDropdown, DsAmountDisplay, DsTotalsRow, DsSearchBar, DsListItem, DsProductCard,
              DsStepIndicator, DsSectionLabel, DsCheckRow, DsKeypadKey, DsDashedActionRow, DsBanner, DsEmptyState, DsExpandableSection, DsAllocationBar
  organisms   DsTopBar, DsNumericKeypad (+ String.applyKey), DsTotalsBlock, DsCartLineItem, DsBottomCartBar, DsChipRow, DsPaymentMethodRow,
              DsBottomSheet, DsDialog, DsSnackbar (+ showError/showSuccess), DsBottomNavBar, DsNavigationRail, DsProcessingState,
              DsNfcPulse / DsNfcWaiting (contactless animation), DsResultMark / DsPaymentResult (success / failure animation)
  templates   DsScreenScaffold, DsTwoPaneLayout, DsCenteredLayout, DsNavigationScaffold (rail on tablet at the start edge — left LTR / right RTL — tab bar on phone)
  catalog     galleries + reference screens (Shell LTR/RTL, Main Sales, Cart, Tips, Discount, Payment, NFC, Result, Onboarding) and the screenshot tests
sample/       app: browse galleries and screens, rotate / use a tablet to see the Expanded layout
```

Dependency direction is enforced by Gradle: `core ← atoms ← molecules ← organisms ← templates ← catalog ← sample`.

## Usage

```kotlin
setContent {
    DsTheme {                          // light/dark from system; bridges to MaterialTheme
        DsScreenScaffold(topBar = { DsTopBar("Panier", onBack = ::back, badge = "5") }) { … }
    }
}

DsTheme.colors.primary                 // #00796B
DsTheme.typography.title
DsTheme.windowSize.isExpanded          // ≥ 600dp → tablet layout
```

Change the brand color in one place: `DsPalette.Teal*` / `DsLightColors.primary`.

## Consuming from another app

```bash
mise run publish        # → ~/.m2 : io.github.amine2233:designsystem-{core,atoms,molecules,organisms,templates}:0.1.0
```
```kotlin
// settings.gradle.kts of the consumer: repositories { mavenLocal(); google(); mavenCentral() }
implementation("io.github.amine2233:designsystem-templates:0.1.0")   // pulls organisms → molecules → atoms → core + Compose BOM
```
Version lives in `gradle/libs.versions.toml` (`designsystem`). Every public composable takes a `modifier`; all public types are `Ds`-prefixed.

## Commands

Toolchain, env vars and tasks are managed by [mise](https://mise.jdx.dev) (`mise.toml`): `mise install` once, then

```bash
mise run app              # build + install + launch the sample app
mise run snapshot:check   # screenshot regression check
mise run snapshot:update  # re-record after an intended change
mise run test             # unit tests
mise run check            # build + test + snapshot:check (CI)
```

Reference PNGs: `designsystem/catalog/src/screenshotTestDebug/reference/` — 22 shots: phone 375×780 + tablet 960×600, light + dark, LTR + RTL (Arabic).
Animated components take a `progress` / `animate` override so snapshots are deterministic.
JDK 17 via mise, AGP 9.4, Gradle 9.7 wrapper.
