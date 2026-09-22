# kotlin-design-system-kit

Jetpack Compose design system extracted from the *Caisse Pro* POS designs (Claude Design project).
Atomic-design modules, phone + tablet adaptive layouts, screenshot-tested.

## Modules

```
designsystem/
  core        tokens (DsPalette, DsColorScheme light/dark, DsTypography, DsSpacing, DsShapes, DsMotion), DsTheme, DsWindowSize,
              DsIcons (curated icon set), DsImages (logo, product placeholder, illustrations — vector drawables in res/)
  atoms       DsText, DsFormField (label/supporting/error frame), DsButton (loading state) / DsIconButton, DsChip, DsTag, DsBadge, DsAvatar, DsIconTile, DsCard, DsDivider,
              DsTextField / DsPasswordField / DsTextArea, DsPickerField, DsErrorText, DsSwitch / DsCheckbox / DsRadioButton, DsSlider, DsProgress, DsSkeleton, DsImage / DsImageSlot / DsIllustration / DsLogo
  molecules   DsQuantityStepper, DsSegmentedControl, DsTabRow, DsDatePickerField / DsTimePickerField, DsDropdown, DsMenu / DsOverflowMenu, DsAmountDisplay, DsTotalsRow, DsSearchBar, DsListItem, DsProductCard,
              DsStepIndicator, DsSectionLabel, DsCheckRow, DsKeypadKey, DsDashedActionRow, DsBanner, DsEmptyState, DsExpandableSection, DsAllocationBar
  organisms   DsTopBar, DsNumericKeypad (+ String.applyKey), DsTotalsBlock, DsCartLineItem, DsBottomCartBar, DsChipRow, DsPaymentMethodRow,
              DsBottomSheet, DsDialog, DsSnackbar (+ showError/showSuccess), DsBottomNavBar, DsNavigationRail, DsProcessingState,
              DsNfcPulse / DsNfcWaiting (contactless animation), DsResultMark / DsPaymentResult (success / failure animation)
  templates   DsScreenScaffold, DsTwoPaneLayout, DsCenteredLayout, DsNavigationScaffold (rail on tablet at the start edge — left LTR / right RTL — tab bar on phone)
  catalog     galleries (atoms, molecules, organisms, forms) + reference screens (Shell LTR/RTL, Main Sales, Cart, Tips, Discount, Payment, NFC, Result, Onboarding) and the screenshot tests
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

## Integrating the packages in your app

Artifacts are published to **GitHub Packages** on every release:
`io.github.amine2233:designsystem-{core,atoms,molecules,organisms,templates}:<version>` (the `catalog` module is demo-only — don't depend on it).

### 1. Add the repository (consumer `settings.gradle.kts`)

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/amine2233/kotlin-design-system-kit")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

GitHub Packages always needs a token, even for public packages: a classic PAT with `read:packages`,
in `~/.gradle/gradle.properties` (`gpr.user=…`, `gpr.key=…`) or as `GITHUB_ACTOR` / `GITHUB_TOKEN` env vars (CI).

### 2. Add the dependency

```kotlin
dependencies {
    // One line — templates pulls organisms → molecules → atoms → core and the Compose BOM.
    implementation("io.github.amine2233:designsystem-templates:1.0.0")
    // Or pick a layer: designsystem-atoms, designsystem-molecules, designsystem-organisms
}
```

The app must apply `org.jetbrains.kotlin.plugin.compose` with the same Kotlin version and use `minSdk ≥ 33` (Android 13+).

### 3. Use it

```kotlin
setContent {
    DsTheme {                                       // light/dark from the system
        DsNavigationScaffold(topItems = tabs, selectedIndex = i, onSelect = { i = it }) {
            DsScreenScaffold(topBar = { DsTopBar("Panier", onBack = ::back, badge = "5") }) { … }
        }
    }
}
```

Local development against an unpublished version: `mise run publish:local` → `~/.m2`, then add `mavenLocal()` to the consumer's repositories and depend on `0.1.0-SNAPSHOT`.

## CI/CD

CI and releases come from [kotlin-ci-shared](https://github.com/amine2233/kotlin-ci-shared).
All logic lives in `mise.toml`:

- `mise run test` / `mise run lint` — what the `CI` workflow runs on pull requests
  (unit tests + screenshot regression check; ktlint with the `.editorconfig` rules).
- Releases are **manual**: *Actions → Release → Run workflow* on `main`. semantic-release analyses the
  `feat:` / `fix:` commits since the last tag, tags `vX.Y.Z`, updates `CHANGELOG.md`, publishes
  `io.github.amine2233:designsystem-<module>` to GitHub Packages and creates the GitHub release with the AARs attached.
- `mise run release --dry-run` previews the next version locally (needs `GITHUB_TOKEN` in `.env.local`, see `.env.local.example`).

## Commands

Toolchain, env vars and tasks are managed by [mise](https://mise.jdx.dev) (`mise.toml`): `mise install` once, then

```bash
mise run app              # build + install + launch the sample app
mise run lint             # ktlint          (mise run format to auto-fix)
mise run test             # unit tests + screenshot regression check
mise run build            # assemble + tests + snapshots
mise run check            # lint + build   (local CI equivalent)
mise run snapshot:update  # re-record screenshots after an intended change
mise run publish:local    # publish all modules to ~/.m2
mise run release --dry-run
```

Reference PNGs: `designsystem/catalog/src/screenshotTestDebug/reference/` — 22 shots: phone 375×780 + tablet 960×600, light + dark, LTR + RTL (Arabic).
Animated components take a `progress` / `animate` override so snapshots are deterministic.
JDK 17 via mise, AGP 9.4, Gradle 9.7 wrapper.
