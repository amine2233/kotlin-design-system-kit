# kotlin-design-system-kit

Jetpack Compose design system, Android only. Everything public is `Ds*`.
Layers: `core ← atoms ← molecules ← organisms ← templates ← catalog ← sample` — imports only point left.

- `designsystem/core` — every token: `DsTheme`, `DsColorScheme`/`DsPalette`, `DsTypography`, `DsSpacing`, `DsShapes`, `DsElevation`, `DsMotion`, `DsIcons`, `DsImages`, `DsWindowSize`, preview annotations.
- `designsystem/atoms` — one-thing components: `DsText`, `DsButton`/`DsIconButton`/`DsOAuthButton`, `DsTextField`/`DsTextArea`/`DsPasswordField`/`DsPhoneField`, `DsFormField`/`DsFieldBox`/`DsPickerField`, `DsSwitch`/`DsCheckbox`/`DsRadioButton`, `DsSlider`, `DsChip`, `DsTag`, `DsBadge`, `DsCard`, `DsDivider`, progress, skeleton, images.
- `designsystem/molecules` — a few atoms doing one job: `DsDropdown`/`DsMultiSelectField`/`DsSearchableDropdown`, the pickers (date, range, time, color, image, file), `DsOtpField`, `DsMenu`, `DsListItem`, `DsSearchBar`, `DsQuantityStepper`, `DsSegmentedControl`, `DsTabRow`, `DsBanner`, `DsEmptyState`.
- `designsystem/organisms` — screen regions: `DsTopBar`, `DsBottomNavBar`/`DsNavigationRail`, `DsBottomSheet`, `DsDialog`, `DsSnackbar`, `DsNumericKeypad`, `DsCartLineItem`, `DsTotalsBlock`, `DsPaymentResult`, `DsNfcWaiting`.
- `designsystem/templates` — page skeletons: `DsScreenScaffold`, `DsNavigationScaffold`, `DsTwoPaneLayout`, `DsCenteredLayout`.
- `designsystem/catalog` — galleries, reference screens, screenshot tests. `sample/` — the app that browses them.

New component:

1. Lowest layer that fits. `Ds` prefix, stateless (value in, lambda out), tokens only — no literal `Color`, `dp`, `sp` or duration. Missing value ⇒ add the token in `core` first.
2. **Preview** — `@DsComponentPreview` in the component's own file, wrapped in `DsPreview { }` (`@DsScreenPreview` for a screen), showing every state that changes the layout (each enum value, error, disabled, empty).
3. **Example in the sample app** — add it to the catalog gallery of its layer, and make sure a `SampleApp` page reaches that gallery (a new gallery needs a new `Page` entry). Not optional: a component you cannot open on a device does not exist.
4. **Snapshot test** — the gallery needs a `@PreviewTest` entry in `CatalogScreenshots.kt` (light + dark); then `mise run snapshot:update`, look at the PNG, then `mise run check`.
5. Animated ⇒ take `animate`/`progress` defaulting to `DsTheme.animationsEnabled`. Pure rule (formatting, filtering) ⇒ unit test. Popups never render in snapshots, so cover the closed state.
6. One component per commit, conventional commits (`feat(atoms): …`), body says why. Ask before adding a dependency.

How the work is organised:

- One component per branch, each in its own worktree **next to** the repo folder: `git worktree add ../ds-<name> -b feat/<name>` (run gradle there with `JAVA_HOME`/`ANDROID_HOME` set; `mise env` and the rtk git wrapper are unavailable inside an isolated worktree).
- Push the branch and open a pull request — `main` takes merges, not direct feature commits. Docs and release chores may land on `main` directly.
- A PR is ready when the six steps above are done and `mise run check` is green; remove the worktree once it is merged (`git worktree remove ../ds-<name>`).
- The PR **title** is linted by CI with commitlint: conventional prefix, and the subject must not start with a PascalCase name — `feat(molecules): tag group that wraps` passes, `feat(molecules): DsTagGroup wraps` fails (`subject-case`). Put the component name later in the sentence.
- Run `mise run lint` before pushing, not just `mise run format`: rules like `mixed-condition-operators` are reported but never auto-fixed.
