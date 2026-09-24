# organisms — whole regions of a screen

`io.github.amine2233:designsystem-organisms`. Depends on `molecules`. These own a region (a bar, a sheet, a
result state) and usually several molecules; they still take state in and report events out.

## Bars and navigation

```kotlin
DsTopBar(title, onBack = null, badge = null, titleContent = null, actions = {})
DsBottomNavBar(items: List<DsNavItem>, selectedIndex, onSelect)
DsNavigationRail(topItems, selectedIndex, onSelect, bottomItems = emptyList(), header = null)
DsNavItem(label, icon, badge?)
DsBottomCartBar(itemCount, total, onExpand, onPay)
```

Do not choose between the bar and the rail by hand — `DsNavigationScaffold` (templates) picks per window size and
handles the RTL start edge. `DsTopBar.actions` is the slot for `DsIconButton` / `DsOverflowMenu`.

## Overlays

```kotlin
DsBottomSheet(onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) { … }
DsModal(title, onDismiss, size = DsModalSize.Half, windowSize = DsTheme.windowSize, footer = null) { … }
DsSheetHandle()
DsDialog(title, text, confirmLabel, onConfirm, onDismiss, dismissLabel = "Annuler", destructive = false)
DsSnackbarHost(hostState)
DsSnackbar(message, tone = DsSnackbarTone.Neutral, actionLabel, onAction, onDismiss)
SnackbarHostState.showError(message) / showSuccess(message)
```

`DsModal` is a bottom sheet on a phone and a centred dialog on a tablet — that switch is the point of it: a
full-width sheet crawling up a 960dp tablet reads as a stretched phone app. `Half` is for a choice the user makes
and leaves, `Full` for a task they work inside (`dsModalHeightFraction` holds the numbers, unit-tested). Its
header is always there, because a sheet without one leaves dragging as the only way out.

`DsDialog(destructive = true)` turns the confirm button red — use it for anything that deletes or voids.
Overlays are popups: they never appear in screenshot tests, so snapshot the trigger and the state behind it.

## Cart and payment

```kotlin
DsCartLineItem(name, quantity, onQuantityChange, lineTotal, unitPrice, tvaLabel, note,
               originalTotal, discountLabel, onTvaClick, onDiscountClick, onRemove)
DsTotalsBlock(lines: List<DsTotalsLine>, total: DsTotalsLine)
DsPaymentMethodRow(icon, title, subtitle, selected, onClick)
DsNumericKeypad(onKey: (DsKey) -> Unit, rowModifier, gap = 7.dp)
String.applyKey(key: DsKey): String
DsChipRow(options, selectedIndex, onSelect, layout = DsChipRowLayout.Inline)
```

`DsChipRow.layout` names what the boolean used to hide: `Inline` keeps one scrolling line for a long category
list, `Grid` divides the width equally for a short fixed set like TVA rates. Its own enum, like every other
component with these two behaviours.

`DsNumericKeypad` emits `DsKey` events; `applyKey` is the pure amount-editing rule (digits fill cents right to
left with French grouping, backspace drops one, comma is a no-op) and is unit-tested — use it instead of
re-implementing amount entry.

## States

```kotlin
DsProcessingState(icon, title, description, progress, amount, amountLabel)
DsNfcWaiting(title, description, amount, animate = DsTheme.animationsEnabled)
DsNfcPulse(size = 180.dp, animate = …)
DsPaymentResult(outcome, title, amount, description, primaryAction, onPrimary,
                secondaryAction, onSecondary, progress = if (animationsEnabled) null else 1f)
DsResultMark(outcome, size = 120.dp, progress = …, onFinished = {})
DsPaymentOutcome.Success / .Failure
```

Every animated organism takes an `animate` or `progress` override so a snapshot can freeze it: leave the default
in the app, pass the override in tests. `DsResultMark.onFinished` fires when the draw-in completes — that is where
an auto-dismiss belongs.
