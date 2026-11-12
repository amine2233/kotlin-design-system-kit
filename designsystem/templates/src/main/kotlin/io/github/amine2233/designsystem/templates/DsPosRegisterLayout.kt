package io.github.amine2233.designsystem.templates

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize
import io.github.amine2233.designsystem.organisms.DsBottomCartBar
import io.github.amine2233.designsystem.organisms.DsTopBar

/** Point-of-sale register (sales) layout: [products] with a pinned [cartPane] on Expanded, a [DsBottomCartBar] on Compact. */
@Composable
fun DsPosRegisterLayout(
    cartItemCount: Int,
    cartTotal: String,
    onExpandCart: () -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier,
    windowSize: DsWindowSize = DsTheme.windowSize,
    topBar: @Composable () -> Unit = {},
    cartPane: @Composable () -> Unit,
    products: @Composable () -> Unit,
) {
    DsScreenScaffold(modifier = modifier, topBar = topBar) {
        DsTwoPaneLayout(
            windowSize = windowSize,
            primary = products,
            secondary = cartPane,
            compactSecondary = {
                DsBottomCartBar(itemCount = cartItemCount, total = cartTotal, onExpand = onExpandCart, onPay = onPay)
            },
        )
    }
}

@DsScreenPreview
@Composable
private fun DsPosRegisterLayoutPreview() =
    DsTheme {
        DsPosRegisterLayout(
            cartItemCount = 3,
            cartTotal = "737 DA",
            onExpandCart = {},
            onPay = {},
            topBar = { DsTopBar("Caisse") },
            cartPane = { DsText("Panier", style = DsTheme.typography.title, modifier = Modifier.padding(DsTheme.spacing.md)) },
            products = { DsText("Produits", style = DsTheme.typography.title, modifier = Modifier.padding(DsTheme.spacing.md)) },
        )
    }
