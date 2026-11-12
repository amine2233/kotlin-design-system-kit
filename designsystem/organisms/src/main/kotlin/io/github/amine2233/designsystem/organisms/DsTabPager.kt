package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import io.github.amine2233.designsystem.molecules.DsTabItem
import io.github.amine2233.designsystem.molecules.DsTabLayout
import io.github.amine2233.designsystem.molecules.DsTabRow

/** Same rule as DsTabRow's own default: past this many tabs the header scrolls. */
private const val GRID_UP_TO = 4

/** [selectedIndex] clamped to the pages that exist; 0 when there are none. */
fun dsTabPagerPage(
    selectedIndex: Int,
    pageCount: Int,
): Int = if (pageCount <= 0) 0 else selectedIndex.coerceIn(0, pageCount - 1)

/** The page to report after a swipe settles, or null when the caller already has it selected. */
fun dsTabPagerSettledSelection(
    settledPage: Int,
    selectedIndex: Int,
): Int? = settledPage.takeIf { it != selectedIndex }

/** A [DsTabRow] of plain labels over swipeable pages, one page per tab. */
@Composable
fun DsTabPager(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    animate: Boolean = DsTheme.animationsEnabled,
    pageContent: @Composable (page: Int) -> Unit,
) {
    DsTabPager(tabs.map(::DsTabItem), selectedIndex, onSelect, modifier, animate = animate, pageContent = pageContent)
}

/**
 * Tabs over swipeable pages, kept in sync both ways: a tap or a new [selectedIndex] moves the pages,
 * and a swipe reports the page it settles on through [onSelect]. Give it a bounded height.
 */
@Composable
fun DsTabPager(
    items: List<DsTabItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    layout: DsTabLayout = if (items.size > GRID_UP_TO) DsTabLayout.Inline else DsTabLayout.Grid,
    animate: Boolean = DsTheme.animationsEnabled,
    pageContent: @Composable (page: Int) -> Unit,
) {
    if (items.isEmpty()) return
    val target = dsTabPagerPage(selectedIndex, items.size)
    val pagerState = rememberPagerState(initialPage = target) { items.size }
    val currentSelectedIndex by rememberUpdatedState(selectedIndex)
    val currentOnSelect by rememberUpdatedState(onSelect)

    LaunchedEffect(target) {
        if (pagerState.currentPage != target) {
            if (animate) pagerState.animateScrollToPage(target) else pagerState.scrollToPage(target)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { page ->
            dsTabPagerSettledSelection(page, currentSelectedIndex)?.let(currentOnSelect)
        }
    }

    Column(modifier) {
        DsTabRow(items, target, onSelect, Modifier.fillMaxWidth(), layout)
        HorizontalPager(pagerState, Modifier.fillMaxWidth().weight(1f)) { page ->
            pageContent(page)
        }
    }
}

@Composable
private fun DsTabPagerPreviewPage(label: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DsText(label, style = DsTheme.typography.body)
    }
}

@DsComponentPreview
@Composable
private fun DsTabPagerPreview() =
    DsPreview {
        val height = Modifier.fillMaxWidth().height(DsTheme.spacing.xxxl * 2)
        val drinks = listOf("Boissons", "Nourriture", "Extras")
        DsTabPager(drinks, 0, {}, height) { DsTabPagerPreviewPage(drinks[it]) }
        DsTabPager(drinks, 2, {}, height) { DsTabPagerPreviewPage(drinks[it]) }
        val orders =
            listOf(
                DsTabItem("En cours", badge = "3", icon = DsIcons.Receipt),
                DsTabItem("Prêtes", badge = "12", icon = DsIcons.Register),
                DsTabItem("Servies", icon = DsIcons.Catalogue),
            )
        DsTabPager(orders, 0, {}, height) { DsTabPagerPreviewPage(orders[it].label) }
        val sections =
            listOf(
                DsTabItem("Vente", icon = DsIcons.Register),
                DsTabItem("Commandes", icon = DsIcons.Receipt, badge = "2"),
                DsTabItem("Catalogue", icon = DsIcons.Catalogue),
                DsTabItem("Clients", icon = DsIcons.Account),
                DsTabItem("Réglages", icon = DsIcons.Settings),
            )
        DsTabPager(sections, 1, {}, height) { DsTabPagerPreviewPage(sections[it].label) }
    }
