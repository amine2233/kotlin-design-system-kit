# catalog, previews and releasing

This is for work **inside** the design-system repo (`amine2233/kotlin-design-system-kit`), not for apps consuming it.

## Where a component goes

`core ← atoms ← molecules ← organisms ← templates ← catalog ← sample`, enforced by Gradle.

Put a component at the lowest layer that can hold it. A component that needs a peer from its own layer belongs one
layer up — that rule is why `DsSearchableDropdown` builds its search box from `DsTextField` (an atom) instead of
reusing `DsSearchBar` (a molecule).

## What every component ships with

1. The composable in its layer's package, `Ds`-prefixed, stateless, KDoc saying what it is for and when to use
   something else instead.
2. A `@DsComponentPreview` (light + dark) — `@DsScreenPreview` for anything screen-sized. Wrap in `DsPreview { }`.
3. An entry in a catalog gallery — that gallery is what the screenshot tests render:
   `AtomsGallery`, `MoleculesGallery`, `OrganismsGallery`, `FoundationsGallery`, `FormsGallery`, `PickersGallery`.
4. A recorded reference screenshot (below), reviewed before committing.
5. A page in the sample app if it is a new gallery or reference screen (`sample/…/SampleApp.kt`).
6. A unit test for any pure rule it introduces (`dsFormatPhone`, `otpDigits`, `dsMatchesQuery`, `applyKey`…).
   Composables themselves are covered by the snapshots.

## Snapshots

```bash
mise run snapshot:update   # re-record after an intended visual change
mise run snapshot:check    # verify (fails on regression)
mise run check             # lint + build + unit tests + snapshots — the local CI equivalent
```

References live in `designsystem/catalog/src/screenshotTestDebug/reference/…`; the test declarations are in
`CatalogScreenshots.kt`. Recording happens on macOS and validation runs on Linux CI, so the comparison threshold
is 0.1 % of pixels for anti-aliasing.

Two things that will bite:

- **Popups never render.** Menus, dialogs, bottom sheets and the date/time calendars are separate windows and come
  out blank or missing. Snapshot the closed state (the trigger, the field) and cover the rest with unit tests.
- **Animations must be freezable.** Anything animated takes `animate` or `progress`; read the default from
  `DsTheme.animationsEnabled` so `DsTheme(animationsEnabled = false)` in a snapshot renders a fixed frame.

A gallery page is rendered on a tall canvas (`FORM_PAGE` in `CatalogScreenshots.kt`). Content taller than the
canvas is silently clipped — when a page outgrows it, raise the height or split the gallery.

## Commits and release

Conventional commits, one component per commit: `feat(atoms): …`, `feat(molecules): …`, `fix(core): …`,
`docs:`, `ci:`, `refactor:`. The body says *why*, not what the diff already shows.

Releases are manual: **Actions → Release → Run workflow** on `main`. semantic-release reads the `feat:` / `fix:`
commits since the last tag, cuts `vX.Y.Z`, updates `CHANGELOG.md`, publishes
`io.github.amine2233:designsystem-<module>` to GitHub Packages, and rewrites the Claude plugin manifest
(`.claude-plugin/plugin.json`) to the same version so plugin and library never drift.

```bash
mise run release --dry-run          # preview the next version (needs GITHUB_TOKEN in .env.local)
mise run plugin:version --version 1.2.3   # what the release runs to sync the plugin manifest
```
