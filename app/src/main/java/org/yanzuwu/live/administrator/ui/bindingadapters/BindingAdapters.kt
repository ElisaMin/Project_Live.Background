package org.yanzuwu.live.administrator.ui.bindingadapters

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {
    @JvmStatic @BindingAdapter("mutableEndIconDrawable","onEndIconClicked",requireAll = false)
    fun mutableEndIconDrawable (
        view: TextInputLayout,
        @DrawableRes mutableEndIconDrawable:Int,
        onClickListener: View.OnClickListener?
    ) {
        view.setEndIconDrawable(mutableEndIconDrawable)
        onClickListener?.let(view::setEndIconOnClickListener)
    }
    @BindingAdapter("isVisible")
    @JvmStatic fun isVisible(view: View,isVisible:Boolean) {
        view.isVisible = isVisible
    }
    @BindingAdapter("isShowing")
    @JvmStatic fun isShowing(view: View,isShowing:Boolean) {
        view.visibility =
            if (isShowing) View.VISIBLE
            else View.GONE
    }
}