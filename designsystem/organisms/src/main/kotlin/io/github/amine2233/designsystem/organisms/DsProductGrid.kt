package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.molecules.DsEmptyState
import io.github.amine2233.designsystem.molecules.DsProductCard
import io.github.amine2233.designsystem.molecules.DsSearchBar

/**
 * One tile of a [DsProductGrid]. [id] is the stable lazy-grid key, [price] arrives already formatted,
 * and [outOfStock] mutes the tile and makes it not clickable.
 */
@Immutable
data class DsProductGridItem(
    val id: Any,
    val name: String,
    val price: String,
    val inCartCount: Int = 0,
    val outOfStock: Boolean = false,
    val image: (@Composable () -> Unit)? = null,
)

/** Grid columns for [windowSize]: 2 on a phone, 4 on a tablet or in landscape. */
fun dsProductGridColumns(windowSize: DsWindowSize): Int =
    when (windowSize) {
        DsWindowSize.Compact -> 2
        DsWindowSize.Expanded -> 4
    }

/**
 * Register catalogue browsing: an optional search bar, optional category chips, then a lazy grid of
 * [DsProductCard] with one column count per [windowSize] ([dsProductGridColumns]).
 *
 * Stateless and never filters: it renders exactly [products], in order, so the caller applies
 * [query] and [selectedCategory] itself. A null [query] hides the search bar and empty [categories]
 * hide the chips. An empty [products] list shows a [DsEmptyState] in place of the grid, below the
 * search bar and chips. Give the grid a bounded height.
 */
@Composable
fun DsProductGrid(
    products: List<DsProductGridItem>,
    onProductClick: (DsProductGridItem) -> Unit,
    emptyTitle: String,
    modifier: Modifier = Modifier,
    categories: List<String> = emptyList(),
    selectedCategory: Int? = null,
    onCategorySelect: (Int) -> Unit = {},
    query: String? = null,
    onQueryChange: (String) -> Unit = {},
    searchPlaceholder: String = "Rechercher...",
    onProductLongClick: ((DsProductGridItem) -> Unit)? = null,
    emptyDescription: String? = null,
    emptyIcon: ImageVector? = null,
    emptyActionLabel: String? = null,
    onEmptyAction: (() -> Unit)? = null,
    outOfStockLabel: String = "Rupture de stock",
    windowSize: DsWindowSize = DsTheme.windowSize,
) {
    val spacing = DsTheme.spacing
    Column(modifier.fillMaxWidth()) {
        if (query != null) {
            DsSearchBar(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = spacing.smd, vertical = spacing.xs),
                placeholder = searchPlaceholder,
            )
        }
        if (categories.isNotEmpty()) {
            DsChipRow(categories, selectedCategory, onCategorySelect, layout = DsChipRowLayout.Inline)
        }
        Box(Modifier.weight(1f).fillMaxWidth()) {
            if (products.isEmpty()) {
                DsEmptyState(
                    title = emptyTitle,
                    modifier = Modifier.align(Alignment.Center),
                    description = emptyDescription,
                    icon = emptyIcon,
                    actionLabel = emptyActionLabel,
                    onAction = onEmptyAction,
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(dsProductGridColumns(windowSize)),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = spacing.smd, vertical = spacing.sm),
                    horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(spacing.sm),
                ) {
                    items(products, key = { it.id }) { item ->
                        DsProductCard(
                            name = item.name,
                            price = item.price,
                            onClick = { onProductClick(item) },
                            inCartCount = item.inCartCount,
                            onLongClick = onProductLongClick?.let { { it(item) } },
                            image = item.image,
                            outOfStock = item.outOfStock,
                            outOfStockLabel = outOfStockLabel,
                        )
                    }
                }
            }
        }
    }
}

private val previewProducts =
    listOf(
        DsProductGridItem(1, "Espresso", "150 DA"),
        DsProductGridItem(2, "Cappuccino", "200 DA", inCartCount = 2),
        DsProductGridItem(3, "Latte", "220 DA"),
        DsProductGridItem(4, "Thé menthe", "120 DA", outOfStock = true),
        DsProductGridItem(5, "Croissant", "90 DA", inCartCount = 1),
        DsProductGridItem(6, "Jus d'orange", "180 DA"),
    )

private val previewCategories = listOf("Tous", "Chauds", "Froids", "Snacks")

@Composable
private fun PreviewProductGrid(
    products: List<DsProductGridItem>,
    query: String? = "",
    windowSize: DsWindowSize = DsWindowSize.Compact,
) = DsProductGrid(
    products = products,
    onProductClick = {},
    emptyTitle = "Aucun résultat",
    modifier = Modifier.height(DsTheme.spacing.xxxl * 6),
    categories = previewCategories,
    selectedCategory = 0,
    query = query,
    emptyDescription = "Essayez une autre recherche ou catégorie.",
    emptyIcon = DsIcons.Search,
    windowSize = windowSize,
)

@DsComponentPreview
@Composable
private fun DsProductGridPopulatedPreview() =
    DsPreview { PreviewProductGrid(previewProducts.map { it.copy(inCartCount = 0, outOfStock = false) }) }

@DsComponentPreview
@Composable
private fun DsProductGridInCartPreview() =
    DsPreview { PreviewProductGrid(previewProducts.map { it.copy(outOfStock = false) }, query = null) }

@DsComponentPreview
@Composable
private fun DsProductGridOutOfStockPreview() = DsPreview { PreviewProductGrid(previewProducts.map { it.copy(outOfStock = it.id != 1) }) }

@DsComponentPreview
@Composable
private fun DsProductGridEmptyPreview() = DsPreview { PreviewProductGrid(emptyList(), query = "Mocha") }

@DsComponentPreview
@Composable
private fun DsProductGridExpandedPreview() =
    DsPreview {
        PreviewProductGrid(
            previewProducts + previewProducts.mapIndexed { index, item -> item.copy(id = previewProducts.size + index + 1) },
            windowSize = DsWindowSize.Expanded,
        )
    }
