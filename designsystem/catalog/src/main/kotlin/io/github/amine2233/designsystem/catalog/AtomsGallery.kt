package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsAvatar
import io.github.amine2233.designsystem.atoms.DsBadge
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsCheckBullet
import io.github.amine2233.designsystem.atoms.DsCheckbox
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.atoms.DsChipTone
import io.github.amine2233.designsystem.atoms.DsCircularProgress
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsErrorText
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsIconTile
import io.github.amine2233.designsystem.atoms.DsLinearProgress
import io.github.amine2233.designsystem.atoms.DsRadioButton
import io.github.amine2233.designsystem.atoms.DsRadioIndicator
import io.github.amine2233.designsystem.atoms.DsSkeleton
import io.github.amine2233.designsystem.atoms.DsSwitch
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.atoms.DsVerticalDivider
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPalette
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

@Composable
fun AtomsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Colors") {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    DsPalette.Teal700,
                    DsPalette.Teal400,
                    DsPalette.Teal100,
                    DsPalette.Teal50,
                    DsPalette.Red500,
                    DsPalette.Orange700,
                    DsPalette.Green700,
                    DsPalette.Grey500,
                    DsPalette.Grey200,
                ).forEach { Box(Modifier.size(32.dp).background(it, DsShapes.sm)) }
            }
        }
        CatalogSection("Typography") {
            val t = DsTheme.typography
            Text("Display 1 080 DA", style = t.displayMedium)
            Text("Headline", style = t.headline)
            Text("Title large", style = t.titleLarge)
            Text("Title", style = t.title)
            Text("Body large", style = t.bodyLarge)
            Text("Body — Encaissez vite, gérez mieux.", style = t.body)
            Text("Label", style = t.label)
            Text("Caption", style = t.caption, color = DsTheme.colors.textSecondary)
            Text("OVERLINE", style = t.overline, color = DsTheme.colors.textTertiary)
        }
        CatalogSection("Buttons") {
            DsButton("Se connecter", onClick = {}, modifier = Modifier.fillMaxWidth())
            DsButton("Créer un compte", onClick = {}, variant = DsButtonVariant.Outlined, modifier = Modifier.fillMaxWidth())
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsButton("Annuler", onClick = {}, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
                DsButton("Vider", onClick = {}, variant = DsButtonVariant.Danger, size = DsButtonSize.Small)
                DsButton("Payer 737 DA", onClick = {}, size = DsButtonSize.Medium)
                DsButton("Off", onClick = {}, size = DsButtonSize.Small, enabled = false)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsButton("Paiement en cours", onClick = {}, size = DsButtonSize.Medium, loading = true)
                DsButton("Envoi", onClick = {}, size = DsButtonSize.Medium, variant = DsButtonVariant.Outlined, loading = true)
            }
            Row {
                DsIconButton(Icons.Default.Settings, contentDescription = "Réglages", onClick = {}, bordered = true)
                DsIconButton(Icons.Default.Email, contentDescription = null, onClick = {}, tint = DsTheme.colors.primary)
                DsIconButton(DsIcons.Print, contentDescription = "Impression", onClick = {}, loading = true, bordered = true)
            }
        }
        CatalogSection("Chips & badges") {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                DsChip("Tous", selected = true)
                DsChip("Chauds")
                DsChip("TVA 10% ▾", tone = DsChipTone.Primary)
                DsChip("% Remise →", tone = DsChipTone.Warning)
                DsChip("Erreur", tone = DsChipTone.Error)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsBadge("3")
                DsBadge("12", containerColor = DsTheme.colors.error)
                DsAvatar("AK")
                DsRadioIndicator(selected = true)
                DsRadioIndicator(selected = false)
                DsCheckBullet()
                DsCheckBullet(filled = false)
            }
        }
        CatalogSection("Inputs") {
            var value by rememberSaveable { mutableStateOf("") }
            DsTextField(value, { value = it }, label = "Libellé (optionnel)", placeholder = "Misc service")
            DsTextField("nom@exemple.fr", {}, leadingIcon = Icons.Default.Email, isError = true, supportingText = "Email invalide")
        }
        CatalogSection("Selection controls") {
            var on by rememberSaveable { mutableStateOf(true) }
            DsSwitch(on, { on = it }, label = "Variantes activées")
            var box by rememberSaveable { mutableStateOf(true) }
            DsCheckbox(box, { box = it }, label = "Imprimer le ticket")
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DsRadioButton(selected = true, onClick = {}, label = "Espèces")
                DsRadioButton(selected = false, onClick = {}, label = "Carte")
            }
        }
        CatalogSection("Tags, tiles, errors") {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                DsTag("Neutre")
                DsTag("Terminal connecté", tone = DsTagTone.Primary)
                DsTag("Payé", tone = DsTagTone.Success)
                DsTag("En attente", tone = DsTagTone.Warning)
                DsTag("Échec", tone = DsTagTone.Error)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                DsIconTile(DsIcons.Card)
                DsIconTile(DsIcons.Cash, selected = true)
                DsIconTile(DsIcons.Coffee, size = 36.dp)
                DsSkeleton(Modifier.size(120.dp, 14.dp), animate = false)
            }
            DsErrorText("Montant invalide")
        }
        CatalogSection("Dividers") {
            DsDivider()
            Row(Modifier.height(24.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Sous-total", style = DsTheme.typography.body)
                DsVerticalDivider(Modifier.padding(horizontal = 8.dp))
                Text("1 080 DA", style = DsTheme.typography.body)
            }
        }
        CatalogSection("Cards & progress") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsCard(Modifier.weight(1f), contentPadding = 12.dp) { Text("Card", style = DsTheme.typography.body) }
                DsCard(Modifier.weight(1f), selected = true, contentPadding = 12.dp) {
                    Text("Selected", style = DsTheme.typography.body, color = DsTheme.colors.primary)
                }
            }
            DsLinearProgress(0.72f)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DsCircularProgress(0.65f)
                DsCircularProgress(0.3f, size = 32.dp, strokeWidth = 3.dp)
            }
        }
    }
}

@Preview(name = "atoms", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun AtomsGalleryPreview() = DsTheme { AtomsGallery() }
