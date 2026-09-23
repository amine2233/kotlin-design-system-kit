# molecules — a few atoms doing one job

`io.github.amine2233:designsystem-molecules`. Depends on `atoms` (and therefore `core`). Also depends on
`androidx.activity:activity-compose` for the two picker launchers below.

## Selects

```kotlin
DsDropdown(options: List<String>, selectedIndex: Int?, onSelect, label, placeholder,
           supportingText, isError, required, enabled)
DsMultiSelectField(options, selected: Set<Int>, onSelectedChange, label, placeholder,
                   supportingText, isError, required, enabled, summary = { dsSummarizeSelection(it) })
DsSearchableDropdown(options, selectedIndex, onSelect, label, placeholder, searchPlaceholder,
                     emptyText, supportingText, isError, required, enabled, filter = ::dsMatchesQuery)
DsSegmentedControl(options, selectedIndex, onSelect, shape = DsShapes.md, height = 40.dp)
DsTabRow(tabs, selectedIndex, onSelect)
```

- Selection crosses the boundary as indices into `options`, so the caller keeps its own option type.
- `DsMultiSelectField` keeps the menu open while ticking; the closed box shows `dsSummarizeSelection` —
  up to two labels, then "n sélectionnés".
- `DsSearchableDropdown` past ~a dozen options only. `dsMatchesQuery` folds case and accents ("creme" finds
  "Crème brûlée"); pass your own `filter` for fuzzy or field-specific matching.
- `DsSegmentedControl` for 2–3 mutually exclusive options that must all stay visible; `DsTabRow` for switching
  content panes.

## Pickers

```kotlin
DsDatePickerField(value: Long?, onValueChange, label, placeholder, supportingText, isError,
                  required, enabled, confirmText, dismissText, format = ::dsFormatDate)
DsDateRangeField(startUtcMillis, endUtcMillis, onRangeChange: (Long?, Long?) -> Unit, …)
DsTimePickerField(value: Int?, onValueChange, …, is24Hour = true, format = ::dsFormatTime)
DsColorPickerField(value: Color?, onValueChange, colors = DsPalette.swatches, …, format = ::dsFormatColor)
DsColorGrid(selected: Color?, onSelect, colors = DsPalette.swatches, perRow = 5, swatchSize = 44.dp)
DsImagePickerField(painter: Painter?, onPick, label, pickText, replaceText, onRemove, …, previewSize = 72.dp)
DsFilePickerField(fileName: String?, onPick, label, pickText, caption, icon, onRemove, …)
DsInlineDatePicker(value: Long?, onValueChange, initialDisplayedMonthUtcMillis, title, headline, showModeToggle)
DsInlineDateRangePicker(startUtcMillis, endUtcMillis, onRangeChange: (Long, Long) -> Unit, …)
```

The inline pair is the calendar with no dialog — a tablet side pane, a report filter panel. They report selection
as it happens (the range one only once both ends exist), so there is no confirm button.
`DsInlineDateRangePicker` scrolls its own month list: give it a bounded height and never put it inside a
`verticalScroll` column, or measuring fails.

Every date component takes `bounds: DsDateBounds` — a design-system value, so callers never touch Material's
experimental `SelectableDates`:

```kotlin
DsDatePickerField(value, onValueChange, bounds = DsDateBounds.upTo(todayUtcMillis))        // no future
DsDateRangeField(start, end, onRangeChange, bounds = DsDateBounds.from(todayUtcMillis))    // no past
DsInlineDatePicker(value, onValueChange, bounds = DsDateBounds.between(open, close).copy(
    isDayAllowed = { millis -> … },                                                        // closed weekdays
))
```

`allows(utcMillis)` / `allowsYear(year)` are the plain predicates behind it — unit-tested, and usable to validate
a value the user typed elsewhere.

Units crossing the boundary — no date or time type enters the design system:

| Component | Value | Formatter |
|---|---|---|
| `DsDatePickerField` | UTC epoch millis, day precision | `dsFormatDate(utcMillis)` → `28/02/2026` |
| `DsDateRangeField` | two of the same | `dsFormatDateRange(start, end)` → `28/02/2026 → 06/03/2026`, open end `…` |
| `DsTimePickerField` | minutes since midnight (`Int`) | `dsFormatTime(minutes)` → `08:30` |
| `DsColorPickerField` | `Color` | `dsFormatColor(color)` → `#00796B` |

- `DsDateRangeField` rather than two date fields: the range calendar cannot produce an inverted period, and the
  field reports a change only once both ends exist.
- The color picker offers a fixed palette on purpose — picked colors must stay legible on the surface and carry
  white text, which an arbitrary HSV value cannot promise. `DsColorGrid` is the grid without the dialog.

Launchers (they need an activity context, so they live here rather than in `atoms`):

```kotlin
val pickImage = rememberDsImagePicker(mediaType = PickVisualMedia.ImageOnly) { uri -> … }
val pickFile  = rememberDsFilePicker(mimeTypes = arrayOf("application/pdf")) { uri -> … }
```

`rememberDsImagePicker` uses the Android photo picker: no storage permission, only what the user selects.
`rememberDsFilePicker` uses `OpenDocument`, so the URI can be persisted for an attachment kept across restarts.
Both fields take what to display (a `Painter`, a file name and caption) — resolving a name or size from a content
URI is the app's job.

## Codes, quantities, search

```kotlin
DsOtpField(value, onValueChange, length = 6, label, supportingText, isError, enabled,
           obscure = false, onFilled = {})
DsQuantityStepper(quantity, onQuantityChange, min = 0, max = Int.MAX_VALUE)
DsSearchBar(value, onValueChange, placeholder = "Rechercher...")
DsKeypadKey(label, onClick, tone = DsKeyTone.Default)     // Default, Secondary, Destructive
```

`DsOtpField` drives its boxes from one hidden text field, so keyboard, paste and one-time-code autofill keep
working; non-digits are dropped and `onFilled` fires on the last digit — submit there.

## Rows, cards, sections

```kotlin
DsListItem(headline, supporting, selected = false, onClick, leading, trailing)
DsProductCard(name, price, onClick, inCartCount = 0, onLongClick, image)
DsMenuCard(name, price, variant = DsMenuCardVariant.Tile, description, tag, tagTone, selected,
           onClick, media, footer)                       // header/body built for you
DsMenuCard(variant, selected, onClick, media, footer, header = { … }, body = { … })   // slots
DsCheckRow(text, filled = true)                       // static "included" line
DsDashedActionRow(text, onClick)                      // dashed "add something" row
DsSectionLabel(text)
DsExpandableSection(title, expanded, onToggle, summary, trailing) { content() }
DsStepIndicator(steps: List<String>, currentStep: Int)
DsAmountDisplay(amount, unit, label = null, prefix = null)
DsTotalsRow(label, value, emphasis = DsTotalsEmphasis.Normal, valueColor = null)
DsAllocationBar(parts: List<DsAllocation>)
```

`DsTotalsEmphasis`: `Muted`, `Normal`, `Discount`, `Positive`, `Total` — the row picks weight and color, callers
never pass a color for a total.

`DsMenuCard` is the product card of a menu-order screen: header, body, optional footer, optional media.
`DsMenuCardVariant.Tile` is the product grid (media on top); `.Row` is a list — order review, search results —
with the footer trailing so a stepper stays under the thumb. `DsProductCard` stays the minimal grid tile when all
you have is a name and a price.

## Menus and messages

```kotlin
DsMenu(items: List<DsMenuItem>, expanded, onDismissRequest, anchor = { … })
DsOverflowMenu(items, icon = DsIcons.More, contentDescription = "Plus d'actions", enabled = true)
DsMenuItem(label, icon = null, enabled = true, destructive = false, onClick)
DsBanner(message, tone = DsBannerTone.Info, title, actionLabel, onAction, onDismiss)
DsEmptyState(title, description, icon, actionLabel, onAction)
```

`DsOverflowMenu` owns its open state — the common case behind a "…" icon. `DsMenu` is the stateless version for a
custom anchor. Destructive entries get the error tint and go last.
