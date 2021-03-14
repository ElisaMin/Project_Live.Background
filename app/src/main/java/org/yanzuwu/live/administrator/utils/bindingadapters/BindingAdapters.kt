package org.yanzuwu.live.administrator.utils.bindingadapters

import android.view.View
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

/**
 * Mutable end icon drawable
 *
 * 作用于[TextInputLayout]的可变式拓展Adapter
 * @param view
 * @param mutableEndIconDrawable
 * @param onClickListener
 */
@BindingAdapter("mutableEndIconDrawable","onEndIconClicked",requireAll = false)
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
fun isShowing(view: View,isShowing:Boolean) {
    view.visibility =
        if (isShowing) View.VISIBLE
        else View.GONE
}

@BindingAdapter("android:adapter")
fun recyclerViewAdapter(recyclerView: RecyclerView,adapter:RecyclerView.Adapter<*>) {
    recyclerView.adapter = adapter
}
