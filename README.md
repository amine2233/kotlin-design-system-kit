# kotlin-design-system-kit

Jetpack Compose design system extracted from the *Caisse Pro* POS designs (Claude Design project).
Atomic-design modules, phone + tablet adaptive layouts, screenshot-tested.

## Modules

```
designsystem/
  core        tokens (DsPalette, DsColorScheme light/dark, DsTypography, DsSpacing, DsShapes, DsMotion), DsTheme, DsWindowSize,
              DsIcons (curated icon set), DsImages (logo, product placeholder, illustrations — vector drawables in res/)
  atoms       DsText, DsFormField (label/supporting/error frame), DsButton (loading state) / DsIconButton / DsOAuthButton, DsChip, DsTag, DsBadge, DsAvatar, DsIconTile, DsCard, DsContainer (DsContainerStyle: Neutral/Info/Success/Warning/Danger/Primary), DsDivider,
              DsTextField / DsPasswordField / DsTextArea / DsPhoneField, DsPickerField, DsErrorText, DsSwitch / DsCheckbox / DsRadioButton, DsSlider, DsProgress, DsSkeleton, DsImage / DsImageSlot / DsIllustration / DsLogo
  molecules   DsQuantityStepper, DsSegmentedControl, DsTabRow (+ DsTabItem: badges, icons; Inline / Grid), DsDatePickerField / DsDateRangeField / DsInlineDatePicker / DsInlineDateRangePicker (+ DsDateBounds) / DsTimePickerField, DsColorPickerField / DsColorGrid, DsImagePickerField / DsFilePickerField (+ rememberDsImagePicker / rememberDsFilePicker), DsDropdown / DsMultiSelectField / DsSearchableDropdown, DsOtpField, DsMenu / DsOverflowMenu, DsAmountDisplay, DsTotalsRow, DsSearchBar, DsListItem, DsProductCard, DsMenuCard (Tile / Row),
              DsStepIndicator, DsSectionLabel, DsCheckRow, DsKeypadKey, DsDashedActionRow, DsBanner, DsEmptyState, DsExpandableSection, DsAllocationBar
  organisms   DsTopBar, DsNumericKeypad (+ String.applyKey), DsTotalsBlock, DsCartLineItem, DsBottomCartBar, DsChipRow, DsPaymentMethodRow,
              DsBottomSheet, DsModal (Half / Full, dialog on tablet), DsDialog, DsSnackbar (+ showError/showSuccess), DsBottomNavBar, DsNavigationRail, DsProcessingState,
              DsNfcPulse / DsNfcWaiting (contactless animation), DsResultMark / DsPaymentResult (success / failure animation)
  templates   DsScreenScaffold, DsTwoPaneLayout, DsCenteredLayout, DsNavigationScaffold (rail on tablet at the start edge — left LTR / right RTL — tab bar on phone)
  catalog     galleries (atoms, molecules, organisms, forms, choices, pickers, overlays, calendars, menu cards) + reference screens (Shell LTR/RTL, Main Sales, Cart, Tips, Discount, Payment, NFC, Result, Onboarding, Login OAuth) and the screenshot tests
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

## Forms

Every field is `DsFormField` underneath — label, `required` marker, supporting text and error
treatment come from one place, so a new field only has to draw its control.

| Need | Component |
|---|---|
| Text, e-mail, amount | `DsTextField` |
| Phone number | `DsPhoneField` (grouped on screen, raw digits in the value) |
| Long text | `DsTextArea` (`maxLength` shows a counter and refuses longer input) |
| Password, cash-register code | `DsPasswordField` (`revealable = false` to forbid showing it) |
| One-time / PIN code | `DsOtpField` (`onFilled` fires on the last digit) |
| Choice in a list | `DsDropdown`, `DsSearchableDropdown` past a dozen options |
| Several choices at once | `DsMultiSelectField` (menu stays open while ticking) |
| Yes/no, multi-choice | `DsSwitch`, `DsCheckbox`, `DsRadioButton`, `DsSegmentedControl` |
| Value in a range | `DsSlider` |
| Date, time | `DsDatePickerField` (UTC millis), `DsTimePickerField` (minutes since midnight) |
| Period | `DsDateRangeField` — both ends in one field, no inverted range possible; `presets = dsDefaultDateRangePresets()` adds Aujourd'hui / Hier / Cette semaine / Ce mois |
| Calendar in the page | `DsInlineDatePicker` / `DsInlineDateRangePicker` — no dialog, reports as you pick |
| Limit which days | `bounds = DsDateBounds.upTo(…)` / `.from(…)` / `.between(…)`, plus `isDayAllowed` |
| Color | `DsColorPickerField`, or `DsColorGrid` on its own |
| Image | `DsImagePickerField` + `rememberDsImagePicker` (Android photo picker, no permission) |
| Document | `DsFilePickerField` + `rememberDsFilePicker` (system document picker) |
| Row actions | `DsMenu` / `DsOverflowMenu` |
| Sign-in | `DsOAuthButton` — see the Login reference screen |

```kotlin
DsTextField(name, { name = it }, label = "Nom du client", required = true)
DsDatePickerField(closing, { closing = it }, label = "Date de clôture")
DsImagePickerField(painter = photo, onPick = rememberDsImagePicker { uri = it }, label = "Photo")
```

Fields are stateless: value in, lambda out. Validation is the caller's — pass `isError` and the
message as `supportingText`. Browse them all in the sample app (*Forms*, *Selects, toggles & sign-in*, *Pickers & menus*, *Overlays & rail*).

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

## Claude Code plugin

The repo doubles as a [Claude Code](https://claude.com/claude-code) plugin: a `design-system-kit` skill that
teaches Claude how to build with these components, with one reference per layer.

```
/plugin marketplace add amine2233/kotlin-design-system-kit
/plugin install kotlin-design-system-kit@amine2233-design-system
```

Then `/kotlin-design-system-kit:design-system-kit`, or just let Claude pick it up when it works on Compose UI in a
project that depends on the library.

```
.claude-plugin/plugin.json          manifest — version tracks the published library
.claude-plugin/marketplace.json     marketplace entry
skills/design-system-kit/SKILL.md   setup, rules, how to pick a component
skills/design-system-kit/references/{core,atoms,molecules,organisms,templates,contributing}.md
```

The plugin version is bumped by the release: semantic-release runs `mise run plugin:version --version X.Y.Z`
during `prepare` and commits `.claude-plugin/plugin.json` alongside `CHANGELOG.md`, so the plugin and the
published artifacts always carry the same version.

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
