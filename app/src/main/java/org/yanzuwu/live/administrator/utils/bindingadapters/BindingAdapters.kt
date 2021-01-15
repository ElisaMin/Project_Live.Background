package org.yanzuwu.live.administrator.utils.bindingadapters

import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

object BindingAdapters {


    /**
     * Mutable end icon drawable
     *
     * 作用于[TextInputLayout]的可变式拓展Adapter
     * @param view
     * @param mutableEndIconDrawable
     * @param onClickListener
     */
    @JvmStatic @BindingAdapter("mutableEndIconDrawable","onEndIconClicked",requireAll = false)
    fun mutableEndIconDrawable (
        view: TextInputLayout,
        @DrawableRes mutableEndIconDrawable:Int,
        onClickListener: View.OnClickListener?
    ) {
        view.setEndIconDrawable(mutableEndIconDrawable)
        onClickListener?.let(view::setEndIconOnClickListener)
    }

    /**
     * Is showing
     *
     * 用[Boolean]设置是否可见
     * @param view
     * @param isShowing
     */
    @BindingAdapter("isShowing")
    @JvmStatic fun isShowing(view: View,isShowing:Boolean) {
        view.visibility =
            if (isShowing) View.VISIBLE
            else View.GONE
    }


}