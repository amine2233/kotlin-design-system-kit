package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsPopover
import io.github.amine2233.designsystem.molecules.DsPopoverPlacement
import io.github.amine2233.designsystem.molecules.DsPopoverSurface
import io.github.amine2233.designsystem.organisms.DsBottomSheet
import io.github.amine2233.designsystem.organisms.DsDialog
import io.github.amine2233.designsystem.organisms.DsModal
import io.github.amine2233.designsystem.organisms.DsModalSize
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNavigationRail
import io.github.amine2233.designsystem.organisms.DsSheetHandle
import io.github.amine2233.designsystem.organisms.DsSnackbarHost
import io.github.amine2233.designsystem.organisms.DsToastHost
import io.github.amine2233.designsystem.organisms.DsToastMessage
import io.github.amine2233.designsystem.organisms.DsToastStyle
import io.github.amine2233.designsystem.organisms.rememberDsToastHostState
import io.github.amine2233.designsystem.organisms.showError
import io.github.amine2233.designsystem.organisms.showSuccess
import kotlinx.coroutines.launch

/**
 * The components that only exist while open — dialog, bottom sheet, modal, snackbar host, toast host,
 * popover — plus the navigation rail on its own. Open them from the sample app: a popup renders in its own
 * window and never lands in a snapshot, so only the triggers (and the popover's surface, shown inline) are
 * captured here.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OverlaysGallery(modifier: Modifier = Modifier) {
    val snackbarHost = remember { SnackbarHostState() }
    val toastHost = rememberDsToastHostState()
    val scope = rememberCoroutineScope()
    var dialog by remember { mutableStateOf(false) }
    var destructiveDialog by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf(false) }
    var halfModal by remember { mutableStateOf(false) }
    var fullModal by remember { mutableStateOf(false) }
    var popoverBelow by remember { mutableStateOf(false) }
    var popoverAbove by remember { mutableStateOf(false) }

    Box(modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            CatalogSection("Dialogs") {
                DsButton("Ouvrir une confirmation", onClick = { dialog = true }, size = DsButtonSize.Medium)
                DsButton(
                    "Ouvrir une suppression",
                    onClick = { destructiveDialog = true },
                    variant = DsButtonVariant.Danger,
                    size = DsButtonSize.Medium,
                )
            }
            CatalogSection("Bottom sheet") {
                DsButton("Ouvrir la feuille", onClick = { sheet = true }, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium)
                DsSheetHandle()
            }
            CatalogSection("Modals — sheet on phone, dialog on tablet") {
                DsButton("Ouvrir en demi-hauteur", onClick = { halfModal = true }, size = DsButtonSize.Medium)
                DsButton(
                    "Ouvrir en plein écran",
                    onClick = { fullModal = true },
                    variant = DsButtonVariant.Outlined,
                    size = DsButtonSize.Medium,
                )
            }
            CatalogSection("Popover") {
                DsPopover(
                    expanded = popoverBelow,
                    onDismissRequest = { popoverBelow = false },
                    placement = DsPopoverPlacement.Below,
                    anchor = { DsButton("Ouvrir en dessous", onClick = { popoverBelow = true }, size = DsButtonSize.Medium) },
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                        DsText("Remise de 10 %", style = DsTheme.typography.title)
                        DsText("Appliquée à toute la commande, hors articles déjà soldés.", color = DsTheme.colors.textSecondary)
                        DsButton("Fermer", onClick = { popoverBelow = false }, size = DsButtonSize.Small)
                    }
                }
                DsPopover(
                    expanded = popoverAbove,
                    onDismissRequest = { popoverAbove = false },
                    placement = DsPopoverPlacement.Above,
                    anchor = {
                        DsButton(
                            "Ouvrir au-dessus",
                            onClick = { popoverAbove = true },
                            variant = DsButtonVariant.Outlined,
                            size = DsButtonSize.Medium,
                        )
                    },
                ) {
                    DsText("Le ticket sera envoyé par e-mail.")
                }
                DsPopoverSurface {
                    Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                        DsText("Paiement fractionné", style = DsTheme.typography.title)
                        DsText("Carte puis espèces : le ticket indique chaque moyen.", color = DsTheme.colors.textSecondary)
                    }
                }
            }
            CatalogSection("Snackbar host") {
                Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                    DsButton("Erreur", onClick = {
                        scope.launch { snackbarHost.showError("Terminal injoignable") }
                    }, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
                    DsButton("Succès", onClick = {
                        scope.launch { snackbarHost.showSuccess("Ticket imprimé") }
                    }, size = DsButtonSize.Small)
                }
            }
            CatalogSection("Toast host") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
                    verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
                ) {
                    DsButton("Info", onClick = {
                        toastHost.show(DsToastMessage("Synchronisation terminée"))
                    }, variant = DsButtonVariant.Outlined, size = DsButtonSize.Small)
                    DsButton("Succès", onClick = {
                        toastHost.show(DsToastMessage("Ticket imprimé", DsToastStyle.Success))
                    }, size = DsButtonSize.Small)
                    DsButton("Alerte", onClick = {
                        toastHost.show(DsToastMessage("Papier bientôt épuisé", DsToastStyle.Warning, title = "Imprimante"))
                    }, variant = DsButtonVariant.Outlined, size = DsButtonSize.Small)
                    DsButton("Erreur", onClick = {
                        toastHost.show(DsToastMessage("Terminal injoignable", DsToastStyle.Error))
                    }, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
                }
            }
            CatalogSection("Navigation rail (tablet)") {
                var rail by rememberSaveable { mutableIntStateOf(0) }
                Box(Modifier.height(320.dp)) {
                    DsNavigationRail(
                        topItems =
                            listOf(
                                DsNavItem("Vente", DsIcons.Register),
                                DsNavItem("Commandes", DsIcons.Receipt, badge = "2"),
                                DsNavItem("Catalogue", DsIcons.Catalogue),
                            ),
                        selectedIndex = rail,
                        onSelect = { rail = it },
                        bottomItems = listOf(DsNavItem("Réglages", DsIcons.Settings)),
                    )
                }
            }
        }
        DsSnackbarHost(snackbarHost, Modifier.align(Alignment.BottomCenter))
        DsToastHost(toastHost, Modifier.align(Alignment.TopCenter))
    }

    if (dialog) {
        DsDialog(
            title = "Valider l'encaissement ?",
            text = "Le ticket sera imprimé et la commande clôturée.",
            confirmLabel = "Valider",
            onConfirm = { dialog = false },
            onDismiss = { dialog = false },
        )
    }
    if (destructiveDialog) {
        DsDialog(
            title = "Vider le panier ?",
            text = "Les 3 articles seront retirés. Cette action est définitive.",
            confirmLabel = "Vider",
            onConfirm = { destructiveDialog = false },
            onDismiss = { destructiveDialog = false },
            destructive = true,
        )
    }
    if (halfModal) {
        DsModal(title = "Actions sur la ligne", onDismiss = { halfModal = false }) {
            DsText("Modifier la quantité, appliquer une remise, retirer l'article.")
        }
    }
    if (fullModal) {
        DsModal(
            title = "Catalogue",
            onDismiss = { fullModal = false },
            size = DsModalSize.Full,
            footer = { DsButton("Fermer", onClick = { fullModal = false }, modifier = Modifier.fillMaxWidth()) },
        ) {
            DsText("Une tâche que l'on mène dans la feuille : recherche, sélection, quantités.")
        }
    }
    if (sheet) {
        DsBottomSheet(onDismiss = { sheet = false }) {
            DsText("Actions sur la ligne", style = DsTheme.typography.title)
            DsText("Modifier la quantité, appliquer une remise, retirer l'article.", color = DsTheme.colors.textSecondary)
            DsButton("Fermer", onClick = { sheet = false }, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(name = "overlays", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun OverlaysGalleryPreview() = DsTheme { OverlaysGallery() }
