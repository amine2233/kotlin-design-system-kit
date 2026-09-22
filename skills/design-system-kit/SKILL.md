---
name: design-system-kit
description: Build and review Jetpack Compose UI with kotlin-design-system-kit — Ds* components, DsTheme tokens, atomic layering (core ← atoms ← molecules ← organisms ← templates). Use when writing or reviewing a Compose screen in an app that depends on io.github.amine2233:designsystem-*, when choosing which Ds component fits a need, or when adding a component to the design system itself.
---

# Kotlin Design System Kit

A Jetpack Compose design system published as `io.github.amine2233:designsystem-{core,atoms,molecules,organisms,templates}`.
Everything public is prefixed `Ds`. Phone and tablet come from one composable, and every component is screenshot-tested.

## Setup

```kotlin
// settings.gradle.kts — GitHub Packages always needs a token, even for public packages
maven {
    url = uri("https://maven.pkg.github.com/amine2233/kotlin-design-system-kit")
    credentials {
        username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
        password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
    }
}

// build.gradle.kts — templates pulls organisms → molecules → atoms → core and the Compose BOM
implementation("io.github.amine2233:designsystem-templates:<version>")
```

The app applies `org.jetbrains.kotlin.plugin.compose` with the same Kotlin version and uses `minSdk ≥ 33`.
Wrap the content once, at the top:

```kotlin
setContent {
    DsTheme {                       // light/dark from the system; bridges to MaterialTheme
        DsScreenScaffold(topBar = { DsTopBar("Panier", onBack = ::back, badge = "5") }) { … }
    }
}
```

## Rules

1. **No raw values.** No literal `Color`, `dp`, `sp` or duration in product code — read `DsTheme.colors`,
   `DsTheme.typography`, `DsTheme.spacing`, `DsTheme.shapes`, `DsMotion`, `DsElevation`, `DsIcons`, `DsImages`.
   A value that has no token means a token is missing: add it in `core`, then use it.
2. **Reach for `Ds*` first.** Material components are an implementation detail of the design system; a screen that
   calls `Button`, `OutlinedTextField` or `Icons.Filled.*` directly has bypassed it. Check the references below
   before building anything by hand.
3. **Layers only point down**: `core ← atoms ← molecules ← organisms ← templates`. Gradle enforces it.
4. **Stateless components.** Value in, lambda out. No ViewModel, no navigation, no domain types, no I/O inside
   the design system — validation and formatting stay with the caller.
5. **Adaptive, not duplicated.** One composable branching on `DsTheme.windowSize.isExpanded`, never a phone copy
   and a tablet copy. `DsNavigationScaffold` and `DsTwoPaneLayout` already do this.
6. **Every component has a preview, a catalog entry and a recorded snapshot.** See
   [contributing.md](references/contributing.md) before adding one.

## Which component

| Need | Go to |
|---|---|
| Colors, type, spacing, shapes, icons, theme, window size | [core.md](references/core.md) |
| Text, buttons, inputs, toggles, chips, cards, images, progress | [atoms.md](references/atoms.md) |
| Selects, pickers (date/time/color/image/file), menus, list items, search | [molecules.md](references/molecules.md) |
| Top bar, navigation bars, sheets, dialogs, snackbars, keypad, payment states | [organisms.md](references/organisms.md) |
| Screen scaffolds, two-pane, centered, navigation shell | [templates.md](references/templates.md) |
| Adding a component, previews, snapshots, release | [contributing.md](references/contributing.md) |

## A screen

```kotlin
@Composable
fun CartScreen(state: CartState, onBack: () -> Unit, onPay: () -> Unit) {
    DsScreenScaffold(
        topBar = { DsTopBar("Panier", onBack = onBack, badge = state.count.toString()) },
        bottomPanel = { DsBottomCartBar(state.count, state.total, onExpand = {}, onPay = onPay) },
    ) {
        state.lines.forEach { line ->
            DsCartLineItem(
                name = line.name,
                quantity = line.quantity,
                onQuantityChange = { … },
                lineTotal = line.total,
                unitPrice = line.unitPrice,
                tvaLabel = line.vat,
            )
        }
        DsTotalsBlock(lines = state.totals, total = state.grandTotal)
    }
}
```

## A form

Every field is a `DsFormField` underneath, so label, `required` marker, supporting text and error treatment are
identical across text, selects and pickers. Pass `isError` plus the message as `supportingText`; the design system
never validates.

```kotlin
DsTextField(name, { name = it }, label = "Nom du client", required = true)
DsPhoneField(phone, { phone = it }, label = "Téléphone")          // value stays raw digits
DsDropdown(vatRates, vatIndex, { vatIndex = it }, label = "TVA")
DsDatePickerField(closing, { closing = it }, label = "Date de clôture")   // UTC millis
DsImagePickerField(painter = photo, onPick = rememberDsImagePicker { uri = it }, label = "Photo")
```

## Checking your work

The sample app browses every component: `mise run app` → *Forms*, *Pickers & menus*, *Atoms*, *Molecules*,
*Organisms*, plus the reference screens. In the design-system repo, `mise run check` runs lint, build, unit tests
and the screenshot regression check.
