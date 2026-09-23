package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsImages
import io.github.amine2233.designsystem.core.DsPalette
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsColorGrid
import io.github.amine2233.designsystem.molecules.DsColorPickerField
import io.github.amine2233.designsystem.molecules.DsDatePickerField
import io.github.amine2233.designsystem.molecules.DsDateRangeField
import io.github.amine2233.designsystem.molecules.DsFilePickerField
import io.github.amine2233.designsystem.molecules.DsImagePickerField
import io.github.amine2233.designsystem.molecules.DsListItem
import io.github.amine2233.designsystem.molecules.DsMenu
import io.github.amine2233.designsystem.molecules.DsMenuItem
import io.github.amine2233.designsystem.molecules.DsOverflowMenu
import io.github.amine2233.designsystem.molecules.DsTimePickerField
import io.github.amine2233.designsystem.molecules.dsDefaultDateRangePresets
import io.github.amine2233.designsystem.molecules.rememberDsFilePicker
import io.github.amine2233.designsystem.molecules.rememberDsImagePicker

/** Fields that open something: date, time, color, and the contextual menu. */
@Composable
fun PickersGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Date") {
            var day by rememberSaveable { mutableStateOf<Long?>(1_772_236_800_000L) }
            DsDatePickerField(day, { day = it }, label = "Date de clôture", required = true)
            DsDatePickerField(null, {}, label = "Date de livraison", supportingText = "Facultatif")
            var period by remember { mutableStateOf(1_772_236_800_000L to 1_772_755_200_000L) }
            DsDateRangeField(
                period.first,
                period.second,
                { start, end -> if (start != null && end != null) period = start to end },
                label = "Période du rapport",
            )
            DsDateRangeField(null, null, { _, _ -> }, label = "Période comparée", supportingText = "Facultatif")
            // Presets are computed from a pinned "today" so the example stays reproducible.
            DsDateRangeField(
                period.first,
                period.second,
                { start, end -> if (start != null && end != null) period = start to end },
                label = "Période (raccourcis)",
                presets = dsDefaultDateRangePresets(),
                todayUtcMillis = 1_772_755_200_000L,
                supportingText = "Aujourd'hui · Hier · Cette semaine · Ce mois",
            )
        }
        CatalogSection("Time") {
            var opening by rememberSaveable { mutableStateOf<Int?>(8 * 60 + 30) }
            DsTimePickerField(opening, { opening = it }, label = "Ouverture")
            DsTimePickerField(null, {}, label = "Fermeture", isError = true, supportingText = "Obligatoire")
        }
        CatalogSection("Color") {
            var color by remember { mutableStateOf(DsPalette.Teal700) }
            DsColorPickerField(color, { color = it }, label = "Couleur de la catégorie", required = true)
            DsColorPickerField(null, {}, label = "Couleur du tag")
            DsColorGrid(selected = color, onSelect = { color = it })
        }
        CatalogSection("Image") {
            var picked by remember { mutableStateOf(true) }
            val picker = rememberDsImagePicker { picked = it != null }
            DsImagePickerField(
                painter = if (picked) painterResource(DsImages.ProductPlaceholder) else null,
                onPick = picker,
                label = "Photo de l'article",
                onRemove = { picked = false },
            )
            DsImagePickerField(null, picker, label = "Logo du commerce", isError = true, supportingText = "Obligatoire")
        }
        CatalogSection("File") {
            var attached by remember { mutableStateOf(true) }
            val filePicker = rememberDsFilePicker { attached = it != null }
            DsFilePickerField(
                fileName = if (attached) "facture-2026-03.pdf" else null,
                onPick = filePicker,
                label = "Justificatif",
                caption = "PDF · 240 Ko",
                onRemove = { attached = false },
            )
            DsFilePickerField(null, filePicker, label = "Bon de commande", isError = true, supportingText = "Obligatoire")
        }
        CatalogSection("Menu") {
            val actions =
                listOf(
                    DsMenuItem("Modifier", DsIcons.Edit) {},
                    DsMenuItem("Imprimer le ticket", DsIcons.Print) {},
                    DsMenuItem("Supprimer la ligne", DsIcons.Delete, destructive = true) {},
                )
            // An open menu is a popup: it renders in its own window and never lands in a snapshot,
            // so only the trigger is captured here.
            DsListItem("Ligne de panier", supporting = "Cappuccino ×2", trailing = { DsOverflowMenu(actions) })
            // DsMenu is the stateless form: the caller owns `expanded` and gives any anchor.
            var open by remember { mutableStateOf(false) }
            DsMenu(actions, expanded = open, onDismissRequest = { open = false }) {
                DsButton("Actions sur la ligne", onClick = { open = true }, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium)
            }
        }
    }
}

@Preview(name = "pickers", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun PickersGalleryPreview() = DsTheme { PickersGallery() }
