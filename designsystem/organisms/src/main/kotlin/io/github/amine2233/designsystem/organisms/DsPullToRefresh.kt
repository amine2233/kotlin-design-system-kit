package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import io.github.amine2233.designsystem.atoms.DsCircularProgress
import io.github.amine2233.designsystem.atoms.DsSpinner
import io.github.amine2233.designsystem.atoms.DsSpinnerSize
import io.github.amine2233.designsystem.atoms.DsSpinnerStyle
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.animationsEnabled
import io.github.amine2233.designsystem.molecules.DsListItem

private const val PREVIEW_HEIGHT_STEPS = 4

/**
 * Pull-to-refresh container around scrollable [content]. The caller owns [isRefreshing] and the work started in
 * [onRefresh]. [animate] false draws the refreshing indicator as a fixed arc (deterministic for screenshot tests).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Chargement",
    animate: Boolean = DsTheme.animationsEnabled,
    content: @Composable BoxScope.() -> Unit,
) {
    val pullState = rememberPullToRefreshState()
    val state = if (isRefreshing && !animate) RestingRefreshState else pullState
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        indicator = {
            PullToRefreshDefaults.IndicatorBox(
                state = state,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = DsTheme.colors.surface,
                elevation = DsElevation.raised,
            ) {
                if (isRefreshing) {
                    DsSpinner(
                        size = DsSpinnerSize.Medium,
                        style = DsSpinnerStyle.Primary,
                        contentDescription = contentDescription,
                        animate = animate,
                    )
                } else {
                    Box(Modifier.clearAndSetSemantics {}) {
                        DsCircularProgress(
                            progress = state.distanceFraction.coerceIn(0f, 1f),
                            size = DsSpinnerSize.Medium.diameter,
                            strokeWidth = DsSpinnerSize.Medium.strokeWidth,
                        )
                    }
                }
            }
        },
        content = content,
    )
}

/** Holds the indicator at its refreshing position from the first frame, without Material's position animation. */
@OptIn(ExperimentalMaterial3Api::class)
private object RestingRefreshState : PullToRefreshState {
    override val distanceFraction: Float = 1f
    override val isAnimating: Boolean = false

    override suspend fun animateToThreshold() = Unit

    override suspend fun animateToHidden() = Unit

    override suspend fun snapTo(targetValue: Float) = Unit
}

@Composable
private fun PreviewRows(
    isRefreshing: Boolean,
    animate: Boolean = DsTheme.animationsEnabled,
) {
    DsPullToRefresh(
        isRefreshing = isRefreshing,
        onRefresh = {},
        modifier = Modifier.height(DsSpacing.xxxl * PREVIEW_HEIGHT_STEPS),
        animate = animate,
    ) {
        LazyColumn(
            Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(DsSpacing.sm),
            verticalArrangement = Arrangement.spacedBy(DsSpacing.sm),
        ) {
            items(4) { DsListItem("Commande #${it + 1}", supporting = "Table ${it + 2}") }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsPullToRefreshPreview() =
    DsPreview {
        PreviewRows(isRefreshing = false)
    }

@DsComponentPreview
@Composable
private fun DsPullToRefreshRefreshingPreview() =
    DsPreview {
        PreviewRows(isRefreshing = true, animate = false)
    }
