package io.github.amine2233.designsystem.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsImages
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Clipped image from a DS drawable ([io.github.amine2233.designsystem.core.DsImages]) or any painter. */
@Composable
fun DsImage(
    @DrawableRes resId: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = DsShapes.md,
    contentScale: ContentScale = ContentScale.Crop,
) {
    DsImage(painterResource(resId), contentDescription, modifier, shape, contentScale)
}

@Composable
fun DsImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = DsShapes.md,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(painter, contentDescription, modifier.clip(shape), contentScale = contentScale)
}

/**
 * Image slot with fallback: renders [painter] when present, else a neutral placeholder tile.
 * Hook an image loader (Coil…) upstream and pass its painter; the DS stays loader-agnostic.
 */
@Composable
fun DsImageSlot(
    painter: Painter?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = DsShapes.md,
) {
    if (painter != null) {
        DsImage(painter, contentDescription, modifier, shape)
    } else {
        Box(modifier.clip(shape).background(DsTheme.colors.surfaceMuted), contentAlignment = Alignment.Center) {
            Icon(DsIcons.Image, contentDescription = contentDescription, tint = DsTheme.colors.textDisabled, modifier = Modifier.size(24.dp))
        }
    }
}

/** Fixed-size illustration (empty state, error, offline). */
@Composable
fun DsIllustration(@DrawableRes resId: Int, modifier: Modifier = Modifier, size: Dp = 120.dp) {
    Image(painterResource(resId), contentDescription = null, modifier = modifier.size(size), contentScale = ContentScale.Fit)
}

/** App logo at any size (vector). */
@Composable
fun DsLogo(modifier: Modifier = Modifier, size: Dp = 80.dp) {
    Image(painterResource(DsImages.Logo), contentDescription = "Caisse Pro", modifier = modifier.size(size))
}
