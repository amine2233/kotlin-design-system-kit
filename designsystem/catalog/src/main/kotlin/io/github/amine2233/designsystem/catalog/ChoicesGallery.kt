package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.amine2233.designsystem.atoms.DsCheckbox
import io.github.amine2233.designsystem.atoms.DsOAuthButton
import io.github.amine2233.designsystem.atoms.DsSlider
import io.github.amine2233.designsystem.atoms.DsSwitch
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsDropdown
import io.github.amine2233.designsystem.molecules.DsMultiSelectField
import io.github.amine2233.designsystem.molecules.DsSearchableDropdown

/** Choosing rather than typing: selects, slider, toggles, and the sign-in providers. */
@Composable
fun ChoicesGallery(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
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
            var allergens by rememberSaveable { mutableStateOf(setOf(0, 1)) }
            DsMultiSelectField(
                listOf("Gluten", "Lait", "Fruits à coque", "Soja"),
                allergens,
                { allergens = it },
                label = "Allergènes",
            )
            DsMultiSelectField(listOf("Cuisine", "Bar", "Comptoir"), setOf(0, 1, 2), {}, label = "Imprimantes du ticket")
            var article by rememberSaveable { mutableStateOf<Int?>(1) }
            DsSearchableDropdown(
                listOf("Crème brûlée", "Cappuccino", "Espresso", "Thé vert"),
                article,
                { article = it },
                label = "Article (recherche)",
            )
        }
        CatalogSection("Slider") {
            var tip by rememberSaveable { mutableFloatStateOf(0.15f) }
            DsSlider(tip, { tip = it }, label = "Pourboire", valueText = "${(tip * 100).toInt()} %")
            DsSlider(3f, {}, valueRange = 1f..5f, steps = 3, label = "Intensité d'impression", valueText = "3 / 5")
        }
        CatalogSection("Sign-in providers") {
            DsOAuthButton("Continuer avec Google", {}, logo = { Icon(DsIcons.Account, contentDescription = null) })
            DsOAuthButton("Continuer avec Apple", {}, logo = { Icon(DsIcons.Store, contentDescription = null) })
            DsOAuthButton("Connexion en cours", {}, loading = true)
        }
        CatalogSection("Toggles") {
            var receipt by rememberSaveable { mutableStateOf(true) }
            DsSwitch(receipt, { receipt = it }, label = "Imprimer le ticket")
            var terms by rememberSaveable { mutableStateOf(false) }
            DsCheckbox(terms, { terms = it }, label = "J'accepte les conditions")
        }
    }
}

@Preview(name = "choices", device = "spec:width=375dp,height=780dp,dpi=420", showBackground = true)
@Composable
private fun ChoicesGalleryPreview() = DsTheme { ChoicesGallery() }
