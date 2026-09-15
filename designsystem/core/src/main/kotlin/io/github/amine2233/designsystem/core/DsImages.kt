package io.github.amine2233.designsystem.core

import androidx.annotation.DrawableRes

/** Image assets shipped with the design system (vector drawables in `core/res/drawable`). */
object DsImages {
    @DrawableRes val Logo = R.drawable.ds_logo

    @DrawableRes val ProductPlaceholder = R.drawable.ds_placeholder_product

    object Illustrations {
        @DrawableRes val EmptyCart = R.drawable.ds_illustration_empty_cart

        @DrawableRes val Error = R.drawable.ds_illustration_error

        @DrawableRes val Offline = R.drawable.ds_illustration_offline
    }
}
