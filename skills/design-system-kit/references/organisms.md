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
DsToastHost(state = rememberDsToastHostState(), modifier, animate = DsTheme.animationsEnabled)
DsToastHostState.show(DsToastMessage(message, style = DsToastStyle.Info, title, duration = 4.seconds, id))
DsToastHostState.dismiss(id)
DsToast(message, style = DsToastStyle.Info, title = null)
```

`DsModal` is a bottom sheet on a phone and a centred dialog on a tablet — that switch is the point of it: a
full-width sheet crawling up a 960dp tablet reads as a stretched phone app. `Half` is for a choice the user makes
and leaves, `Full` for a task they work inside (`dsModalHeightFraction` holds the numbers, unit-tested). Its
header is always there, because a sheet without one leaves dragging as the only way out.

`DsToast` is for information the user only needs to read — "Ticket imprimé", "Synchronisation terminée" — and
has no action or close button; anything the user can act on (retry, undo) is a `DsSnackbar`. `DsToastHost` shows
one toast at a time in FIFO order, leaves it up for its `duration` (4 s by default, a non-positive one falls back to
4 s, at least 10 s while TalkBack is on), and dismisses it on tap or sideways swipe. Showing an id already queued
updates that toast in place. `dsToastDismissDelay` and `DsToastQueueState` hold the rules and are unit-tested.

`DsDialog(destructive = true)` turns the confirm button red — use it for anything that deletes or voids.
Overlays are popups: they never appear in screenshot tests, so snapshot the trigger and the state behind it.

## Circle menu

```kotlin
DsCircleMenu(expanded, onExpandedChange, actions: List<DsCircleMenuAction>, triggerContentDescription,
             triggerIcon = DsIcons.Add, startAngle = 90f, sweepAngle = 90f, radius = 96.dp,
             animate = DsTheme.animationsEnabled)
DsCircleMenuAction(icon, contentDescription, onClick, enabled = true)
```

A floating trigger that fans 2 to 8 actions out on an arc; any other count throws. Angles are degrees, 0° points
to the end edge and positive turns upwards, so the default 90°→180° arc sits in a bottom-end corner and fans up
and towards the start; RTL mirrors it. A sweep of 360° spreads the actions evenly around a full circle. It is
drawn inline, not in a popup, and always measures its open footprint so toggling never moves the layout. The
caller owns `expanded` and every dismissal — scrim, outside tap, back press — through `onExpandedChange`;
tapping an action calls its `onClick` then `onExpandedChange(false)`.

## Cart and payment

```kotlin
DsCartLineItem(name, quantity, onQuantityChange, lineTotal, unitPrice, tvaLabel, note,
               originalTotal, discountLabel, onTvaClick, onDiscountClick, onRemove)
DsTotalsBlock(lines: List<DsTotalsLine>, total: DsTotalsLine, rowModifier = Modifier)
DsPaymentMethodRow(icon, title, subtitle, selected, onClick)
DsNumericKeypad(onKey: (DsKey) -> Unit, rowModifier, gap = 7.dp, showComma = true, enabled = true)
String.applyKey(key: DsKey): String
DsChipRow(options, selectedIndex, onSelect, layout = DsChipRowLayout.Inline)
```

`DsChipRow.layout` names what the boolean used to hide: `Inline` keeps one scrolling line for a long category
list, `Grid` divides the width equally for a short fixed set like TVA rates. Its own enum, like every other
component with these two behaviours.

`DsNumericKeypad` emits `DsKey` events; `applyKey` is the pure amount-editing rule (digits fill cents right to
left with French grouping, backspace drops one, comma is a no-op) and is unit-tested — use it instead of
re-implementing amount entry. `showComma = false` leaves the comma slot empty so `0` and backspace keep their
columns; `enabled = false` greys every key (`DsKeypadKey(enabled = false)`) and ignores taps.

### Staff PIN

```kotlin
DsStaffPinLogin(staff: List<DsStaffMember>, selectedStaffId: String?, onSelectStaff, enteredCount: Int, onKey,
                state: DsStaffPinState = DsStaffPinState.Entering, pinLength = DsStaffPinDefaults.PinLength,
                animate = DsTheme.animationsEnabled, emptyStaffLabel = "No staff members",
                pinProgressLabel = { e, t -> "$e of $t digits entered" }, keypadRowModifier = Modifier)
DsStaffMember(id, name, initials)
DsStaffPinState.Entering | Error(message) | Locked(message)
dsApplyPinKey(pin: String, key: DsKey, length = DsStaffPinDefaults.PinLength): String
```

`DsStaffPinLogin` signs a staff member in on a shared terminal: a wrapping row of avatar radio items, masked PIN
dots and a comma-less keypad. It never sees the PIN, only `enteredCount`; keep the PIN in the caller and apply keys
with `dsApplyPinKey` (digits append up to the length, backspace drops one, comma is a no-op). Verification, attempt
counting and the lockout timer belong to the caller, which reports the outcome through `state`: `Error` colours the
dots red, shows its message and shakes the dots when `animate` is on; `Locked` shows its message and disables both
the keypad and staff selection. The keypad is also disabled until `selectedStaffId` names a member of `staff`.
TalkBack reads the dots as `pinProgressLabel(count, total)`, never the digits.

```kotlin
DsReceipt(merchantName, items: List<DsReceiptItem>, totals: List<DsTotalsLine>, total: DsTotalsLine,
          paymentIcon, paymentLabel, timestamp, reference, merchantDetails = emptyList(),
          onPrint = null, onEmail = null, onSms = null, onNoReceipt = null,
          printLabel = "Print", emailLabel = "Email", smsLabel = "SMS", noReceiptLabel = "No receipt")
DsReceiptItem(quantity: String, name: String, total: String, detail = null, originalTotal = null)
```

`DsReceipt` takes preformatted strings only and does no money math: discount, tax and tip lines exist only if the
caller passes them in `totals`. A null callback hides that action; with no callbacks the receipt is read-only and
no action area is drawn. It is not scrollable by itself — put it in the caller's scroll container. Its totals
reuse `DsTotalsBlock` with a merging `rowModifier`, so TalkBack reads each row as one phrase.

```kotlin
DsSplitBill(totalMinor, mode: DsSplitMode, onModeChange, parts: List<DsSplitPart>, onPartCountChange, maxParts,
            onPartClick, onCollect, formatAmount: (Long) -> String, equalModeLabel, customModeLabel, partCountLabel,
            totalLabel, paidLabel, remainingLabel, unallocatedLabel, overAllocatedLabel, collectLabel,
            partLabel = { "Part ${it + 1}" })
DsSplitPart(paid = false, amountMinor = 0L, label = null)
dsEqualSplit(totalMinor, parts): List<Long>
dsSplitAmounts(totalMinor, mode, parts): List<Long>
dsSplitUnallocated(totalMinor, amountsMinor): Long   // > 0 unallocated, < 0 over-allocated
dsSplitPaid(amountsMinor, parts): Long
```

`DsSplitBill` splits a bill into `Equal` shares or `Custom` amounts and shows each part as paid or remaining.
Amounts are minor units and the math is integer-only: `dsEqualSplit` gives every part `total / parts` and hands
the remainder out one unit at a time to the first parts, so the shares always sum to the total. `formatAmount`
comes from the caller, so currency stays the app's. The caller owns `parts`: it resizes the list on
`onPartCountChange` and marks a part paid on `onCollect`. In Custom mode, `onPartClick` is where the caller opens
its own `DsNumericKeypad` + `DsAmountDisplay` and writes the result back into `amountMinor`; the organism embeds
no amount entry. While Custom amounts exceed the total, the over-allocated row shows in the error colour and
every collect action is disabled.

## Product grid

```kotlin
DsProductGrid(products: List<DsProductGridItem>, onProductClick, emptyTitle, categories = emptyList(),
              selectedCategory = null, onCategorySelect = {}, query = null, onQueryChange = {},
              searchPlaceholder = "Rechercher...", onProductLongClick = null, emptyDescription, emptyIcon,
              emptyActionLabel, onEmptyAction, outOfStockLabel = "Rupture de stock",
              windowSize = DsTheme.windowSize)
DsProductGridItem(id, name, price, inCartCount = 0, outOfStock = false, image = null)
dsProductGridColumns(windowSize): Int   // Compact 2, Expanded 4
```

`DsProductGrid` is the register catalogue: an optional `DsSearchBar` (shown when `query` is not null), optional
`DsChipRow` categories (shown when `categories` is not empty), then a lazy grid of `DsProductCard` keyed by `id`.
It is stateless and never filters: the caller applies the query and the category and passes the result as
`products`, rendered in order. The column count comes from `DsWindowSize` through `dsProductGridColumns`. An empty
list shows `DsEmptyState` in place of the grid, under the search bar and chips. Out-of-stock items render muted,
labelled and not clickable. The grid is lazy, so give it a bounded height.

## States

```kotlin
DsProcessingState(icon, title, description, progress, amount, amountLabel)
DsNfcWaiting(title, description, amount, animate = DsTheme.animationsEnabled)
DsNfcPulse(size = 180.dp, animate = …)
DsPaymentResult(outcome, title, amount, description, primaryAction, onPrimary,
                secondaryAction, onSecondary, progress = if (animationsEnabled) null else 1f)
DsResultMark(outcome, size = 120.dp, progress = …, onFinished = {})
DsPaymentOutcome.Success / .Failure
DsPullToRefresh(isRefreshing, onRefresh, modifier, contentDescription = "Chargement",
                animate = DsTheme.animationsEnabled) { content }
```

Every animated organism takes an `animate` or `progress` override so a snapshot can freeze it: leave the default
in the app, pass the override in tests. `DsResultMark.onFinished` fires when the draw-in completes — that is where
an auto-dismiss belongs.

`DsPullToRefresh` is stateless: the caller owns `isRefreshing` and the work started in `onRefresh`, and sets the flag
back to false when it finishes. Snapshots pass `animate = false` to freeze the refreshing frame (indicator at rest,
fixed spinner arc).
