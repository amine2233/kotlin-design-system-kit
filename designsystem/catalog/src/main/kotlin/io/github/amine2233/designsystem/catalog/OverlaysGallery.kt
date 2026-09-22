package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import io.github.amine2233.designsystem.organisms.DsBottomSheet
import io.github.amine2233.designsystem.organisms.DsDialog
import io.github.amine2233.designsystem.organisms.DsNavItem
import io.github.amine2233.designsystem.organisms.DsNavigationRail
import io.github.amine2233.designsystem.organisms.DsSheetHandle
import io.github.amine2233.designsystem.organisms.DsSnackbarHost
import io.github.amine2233.designsystem.organisms.showError
import io.github.amine2233.designsystem.organisms.showSuccess
import kotlinx.coroutines.launch

/**
 * The components that only exist while open — dialog, bottom sheet, snackbar host — plus the
 * navigation rail on its own. Open them from the sample app: a popup renders in its own window and
 * never lands in a snapshot, so only the triggers are captured here.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverlaysGallery(modifier: Modifier = Modifier) {
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var dialog by remember { mutableStateOf(false) }
    var destructiveDialog by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf(false) }

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
