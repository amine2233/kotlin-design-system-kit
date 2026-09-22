package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsFieldBox
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme
import java.text.Normalizer
import java.util.Locale

private val Diacritics = "\\p{Mn}+".toRegex()

private fun fold(text: String): String = Diacritics.replace(Normalizer.normalize(text, Normalizer.Form.NFD), "").lowercase(Locale.ROOT)

/**
 * Default filter: case- and accent-insensitive "contains", so "creme" finds "Crème brûlée".
 * A cashier typing on a hurried terminal will not reproduce the accents.
 */
fun dsMatchesQuery(
    option: String,
    query: String,
): Boolean = query.isBlank() || fold(option).contains(fold(query.trim()))

/**
 * Select field with a search box in the menu — for lists too long to scan: products, customers,
 * tax rates across countries.
 *
 * Below roughly a dozen options prefer [DsDropdown]: a search box the user does not need is one
 * more thing between them and the choice. The query is local and resets when the menu closes.
 */
@Composable
fun DsSearchableDropdown(
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner",
    searchPlaceholder: String = "Rechercher",
    emptyText: String = "Aucun résultat",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    filter: (String, String) -> Boolean = ::dsMatchesQuery,
) {
    val c = DsTheme.colors
    var open by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        Box {
            DsFieldBox(
                valueText = selectedIndex?.let { options.getOrNull(it) },
                placeholder = placeholder,
                trailingIcon = DsIcons.Search,
                onClick = { open = true },
                isError = isError,
                enabled = enabled,
                active = open,
            )
            DropdownMenu(
                expanded = open,
                onDismissRequest = {
                    open = false
                    query = ""
                },
                modifier = Modifier.heightIn(max = 360.dp).width(300.dp),
                containerColor = c.surface,
                shape = DsShapes.md,
            ) {
                DsTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = DsTheme.spacing.smd, vertical = DsTheme.spacing.xs),
                    placeholder = searchPlaceholder,
                    leadingIcon = DsIcons.Search,
                )
                val matches = options.withIndex().filter { filter(it.value, query) }
                if (matches.isEmpty()) {
                    DsText(
                        emptyText,
                        style = DsTheme.typography.body,
                        color = c.textTertiary,
                        modifier = Modifier.padding(horizontal = DsTheme.spacing.md, vertical = DsTheme.spacing.smd),
                    )
                }
                matches.forEach { (index, option) ->
                    DropdownMenuItem(
                        text = {
                            DsText(
                                option,
                                style = DsTheme.typography.body,
                                color = if (index == selectedIndex) c.primary else c.textPrimary,
                            )
                        },
                        onClick = {
                            open = false
                            query = ""
                            onSelect(index)
                        },
                    )
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsSearchableDropdownPreview() =
    DsPreview {
        DsSearchableDropdown(listOf("Crème brûlée", "Cappuccino", "Espresso", "Thé vert"), 1, {}, label = "Article")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSearchableDropdown(List(40) { "Client ${it + 1}" }, null, {}, label = "Client", placeholder = "Rechercher un client")
    }
