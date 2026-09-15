package io.github.amine2233.designsystem.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Curated icon set — the only icons product code should reach for.
 * Backed by Material Symbols today; swap an entry here to re-skin every screen.
 */
object DsIcons {
    // Navigation
    val Back: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
    val Close: ImageVector = Icons.Filled.Close
    val ChevronRight: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight
    val ChevronDown: ImageVector = Icons.Filled.KeyboardArrowDown
    val ChevronUp: ImageVector = Icons.Filled.KeyboardArrowUp
    val More: ImageVector = Icons.Filled.MoreVert
    val Home: ImageVector = Icons.Filled.Home
    val Settings: ImageVector = Icons.Filled.Settings
    val Search: ImageVector = Icons.Filled.Search
    val Account: ImageVector = Icons.Filled.Person

    // Actions
    val Add: ImageVector = Icons.Filled.Add
    val Remove: ImageVector = Icons.Filled.Remove
    val Edit: ImageVector = Icons.Filled.Edit
    val Delete: ImageVector = Icons.Filled.Delete
    val Check: ImageVector = Icons.Filled.Check
    val Backspace: ImageVector = Icons.Filled.Backspace
    val Hold: ImageVector = Icons.Filled.PauseCircle
    val Print: ImageVector = Icons.Filled.Print

    // POS domain
    val Cart: ImageVector = Icons.Filled.ShoppingCart
    val Card: ImageVector = Icons.Filled.CreditCard
    val Cash: ImageVector = Icons.Filled.Payments
    val Contactless: ImageVector = Icons.Filled.Contactless
    val Split: ImageVector = Icons.Filled.CallSplit
    val Discount: ImageVector = Icons.Filled.Percent
    val Tip: ImageVector = Icons.Outlined.Savings
    val Receipt: ImageVector = Icons.AutoMirrored.Filled.ReceiptLong
    val Register: ImageVector = Icons.Filled.PointOfSale
    val Catalogue: ImageVector = Icons.Filled.Inventory2
    val Store: ImageVector = Icons.Filled.Storefront
    val Coffee: ImageVector = Icons.Filled.LocalCafe
    val Image: ImageVector = Icons.Filled.Image

    // Status
    val Info: ImageVector = Icons.Filled.Info
    val Success: ImageVector = Icons.Filled.CheckCircle
    val Warning: ImageVector = Icons.Filled.Warning
    val Error: ImageVector = Icons.Filled.Error
    val Offline: ImageVector = Icons.Filled.WifiOff
}
