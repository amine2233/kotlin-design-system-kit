package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.amine2233.designsystem.atoms.DsFieldBox
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsPasswordField
import io.github.amine2233.designsystem.atoms.DsPhoneField
import io.github.amine2233.designsystem.atoms.DsPickerField
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.atoms.DsTextArea
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsOtpField

/** The field frame and everything typed into it. Choices live in [ChoicesGallery], pickers in [PickersGallery]. */
@Composable
fun FormsGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        CatalogSection("Field primitives") {
            // What every Ds field is built from: the frame, the closed box, and the two combined.
            DsFormField(label = "DsFormField", supportingText = "Frame: label, required, supporting text") {
                DsText("n'importe quel contrôle", style = DsTheme.typography.bodyStrong)
            }
            DsFieldBox("DsFieldBox", "Placeholder", DsIcons.ChevronDown, {})
            DsFieldBox(null, "Ouvert (popup ancré)", DsIcons.ChevronDown, {}, active = true)
            DsFieldBox("Désactivé", "Placeholder", DsIcons.ChevronDown, {}, enabled = false)
            DsPickerField("DsPickerField", "Placeholder", DsIcons.Calendar, {}, label = "Boîte + frame")
        }
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
        CatalogSection("Phone") {
            var phone by rememberSaveable { mutableStateOf("0612345678") }
            DsPhoneField(phone, { phone = it }, label = "Téléphone du client")
            DsPhoneField("0612", {}, label = "Incomplet", isError = true, supportingText = "10 chiffres attendus")
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
        CatalogSection("Code") {
            var code by rememberSaveable { mutableStateOf("4218") }
            DsOtpField(code, { code = it }, label = "Code reçu par SMS")
            DsOtpField("1234", {}, length = 4, obscure = true, isError = true, supportingText = "Code incorrect")
        }
    }
}

@Preview(name = "forms", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun FormsGalleryPreview() = DsTheme { FormsGallery() }
