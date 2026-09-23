# atoms — the smallest usable pieces

`io.github.amine2233:designsystem-atoms`. Depends on `core` only. An atom never composes another concept: it draws
one thing and reports interaction.

## Text

```kotlin
DsText(text, style = DsTheme.typography.body, color = DsTheme.colors.textPrimary,
       textAlign = null, maxLines = Int.MAX_VALUE, overflow = TextOverflow.Ellipsis)
```

Use it instead of Material `Text` so the default style and color come from tokens.

## Buttons

```kotlin
DsButton(text, onClick, variant = DsButtonVariant.Primary, size = DsButtonSize.Large,
         enabled = true, loading = false, leadingIcon = null)
DsIconButton(icon, contentDescription, onClick, tint = …, containerColor = Color.Transparent,
             bordered = false, enabled = true, loading = false)
DsOAuthButton(text, onClick, enabled = true, loading = false, logo = null)
```

- `DsButtonVariant`: `Primary`, `Outlined`, `Ghost`, `Danger`. `DsButtonSize`: `Small` 36dp, `Medium` 44dp, `Large` 52dp.
- `loading` replaces the leading icon with a spinner and disables the button; it honours `animationsEnabled`.
- `DsOAuthButton` keeps the provider logo as a slot — brand marks belong to the provider, so the app supplies the
  asset. The fixed 24dp slot keeps labels aligned down a stack of providers.

## Fields

All of them wrap `DsFormField`, so labels, `required`, supporting text and errors look the same everywhere.

```kotlin
DsFormField(label, supportingText, trailingSupportingText, isError, required, enabled) { control() }
DsTextField(value, onValueChange, label, placeholder, leadingIcon, trailingContent, isError,
            supportingText, required, enabled, singleLine = true, minLines = 1,
            visualTransformation, keyboardOptions)
DsTextArea(value, onValueChange, label, placeholder, isError, supportingText, required, enabled,
           minLines = 3, maxLines = 8, maxLength = null)
DsPasswordField(value, onValueChange, label, placeholder, isError, supportingText, required,
                enabled, revealable = true, imeAction = ImeAction.Done)
DsPhoneField(value, onValueChange, label, placeholder, groups = DsPhoneGroupsFr, isError,
             supportingText, required, enabled)
DsSlider(value, onValueChange, valueRange = 0f..1f, steps = 0, label, valueText,
         supportingText, enabled, onValueChangeFinished)
```

- `DsTextArea.maxLength` shows a counter **and** refuses longer input, so the value never needs trimming.
- `DsPasswordField(revealable = false)` for codes that must never be displayed.
- `DsPhoneField` keeps the value as raw digits; grouping is display only. `dsFormatPhone(digits, groups)` is the
  same formatter, e.g. for a receipt line. `DsPhoneGroupsFr` = `[2,2,2,2,2]`.
- `DsSlider.valueText` takes an already-formatted string — the design system never formats numbers.

Building a new field? Use `DsFormField` for label/error and `DsFieldBox` or `DsPickerField` for a read-only box:

```kotlin
DsFieldBox(valueText, placeholder, trailingIcon, onClick, isError, enabled, active, leadingContent)
DsPickerField(valueText, placeholder, trailingIcon, onClick, label, supportingText, isError,
              required, enabled, leadingContent)   // = DsFieldBox inside DsFormField
```

`DsFieldBox` is the one to use when a popup must anchor to the box itself (see `DsDropdown`).

## Selection

```kotlin
DsSwitch(checked, onCheckedChange, label = null, enabled = true)
DsCheckbox(checked, onCheckedChange, label = null, enabled = true)
DsRadioButton(selected, onClick, label, enabled = true)
DsRadioIndicator(selected, size = 20.dp)     // the dot alone, for list rows
DsCheckBullet(size = 20.dp, filled = true)   // static check bullet, for feature lists
```

`DsSwitch` with a label fills the width and pushes the switch to the end; without one it is just the control.

## Chips, tags, badges, avatars, tiles

```kotlin
DsChip(label, selected = false, onClick = null, tone = DsChipTone.Neutral, shape, contentPadding)
DsTag(text, tone = DsTagTone.Neutral)
DsBadge(text, containerColor = DsTheme.colors.primary, contentColor = …)
DsAvatar(initials, size = 36.dp)
DsIconTile(icon, size = 44.dp, selected = false, content = null)
```

- `DsChipTone`: `Neutral`, `Primary`, `Warning`, `Error` — a chip is interactive (filter, toggle).
- `DsTagTone`: `Neutral`, `Primary`, `Success`, `Warning`, `Error`, `Info` — a tag is a read-only status.

## Surfaces, separators, feedback

```kotlin
DsCard(onClick = null, selected = false, shape = DsShapes.card, containerColor, borderColor,
       borderWidth, elevation = DsElevation.none, contentPadding = 0.dp) { content() }
DsContainer(style = DsContainerStyle.Neutral, shape = DsShapes.md, contentPadding, borderWidth,
            backgroundAlpha = DsAlpha.TINT, borderAlpha = DsAlpha.BORDER) { content() }   // ColumnScope
DsContainerStyle.accent()        // the tone at full strength, for text/icons inside
DsDivider(thickness = 1.dp) / DsVerticalDivider(thickness = 1.dp)
DsErrorText(message)                                  // icon + short message, error color
DsLinearProgress(progress: Float?)                    // null = indeterminate
DsCircularProgress(progress: Float?, size = 52.dp, strokeWidth = 4.dp)
DsSkeleton(shape = DsShapes.sm, animate = DsTheme.animationsEnabled)
```

`selected = true` on `DsCard` switches to the primary container + border, the system-wide selected look.

`DsContainer` is the tinted, bordered box for a message or any content — `Neutral`, `Info`, `Success`, `Warning`,
`Danger`, `Primary`. Background and border are the same tone at two opacities (`DsAlpha.TINT` / `DsAlpha.BORDER`)
rather than two opaque colors, so it sits correctly on whatever is behind it in both themes and a new tone costs
one color instead of a light/dark pair. `DsBanner` (molecules) stays the choice when the message needs an icon, a
title and a dismiss action.

## Images

```kotlin
DsImage(resId | painter, contentDescription, shape = DsShapes.md, contentScale = ContentScale.Crop)
DsImageSlot(painter?, contentDescription, shape)   // placeholder tile when painter is null
DsIllustration(resId, size = 120.dp)
DsLogo(size = 80.dp)
```

The design system stays image-loader agnostic: pass a `Painter` (from Coil, a bitmap, a resource); it never loads
anything itself.
