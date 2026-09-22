package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.amine2233.designsystem.atoms.DsCheckbox
import io.github.amine2233.designsystem.atoms.DsPasswordField
import io.github.amine2233.designsystem.atoms.DsSwitch
import io.github.amine2233.designsystem.atoms.DsTextArea
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsDropdown

/** Every form building block on one page — the reference for assembling a form screen. */
@Composable
fun FormsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Text fields") {
            var name by rememberSaveable { mutableStateOf("") }
            DsTextField(name, { name = it }, label = "Nom du client", placeholder = "Ex. Amina K.", required = true)
            DsTextField(
                "nom@exemple",
                {},
                label = "E-mail",
                leadingIcon = DsIcons.Account,
                isError = true,
                supportingText = "Adresse invalide",
            )
        }
        CatalogSection("Text area") {
            var note by rememberSaveable { mutableStateOf("Sans oignons, servir le café après le plat.") }
            DsTextArea(note, { note = it }, label = "Note de commande", maxLength = 140)
        }
        CatalogSection("Password") {
            var password by rememberSaveable { mutableStateOf("motdepasse") }
            DsPasswordField(password, { password = it }, label = "Mot de passe", required = true)
            DsPasswordField("123", {}, label = "Code caisse", isError = true, supportingText = "6 chiffres minimum", revealable = false)
        }
        CatalogSection("Select") {
            var vat by rememberSaveable { mutableIntStateOf(0) }
            DsDropdown(
                listOf("20%", "10%", "5,5%"),
                vat,
                { vat = it },
                label = "Taux de TVA",
                required = true,
                supportingText = "Appliqué à toute la ligne",
            )
            DsDropdown(listOf("Fidélité", "Geste commercial"), null, {}, label = "Motif de remise")
        }
        CatalogSection("Toggles") {
            var receipt by rememberSaveable { mutableStateOf(true) }
            DsSwitch(receipt, { receipt = it }, label = "Imprimer le ticket")
            var terms by rememberSaveable { mutableStateOf(false) }
            DsCheckbox(terms, { terms = it }, label = "J'accepte les conditions")
        }
    }
}

@Preview(name = "forms", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun FormsGalleryPreview() = DsTheme { FormsGallery() }
