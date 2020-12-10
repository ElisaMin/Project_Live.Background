package org.yanzuwu.live.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.data.SharedViewModel
import org.yanzuwu.live.administrator.data.TheDao
import javax.inject.Inject

@AndroidEntryPoint
class Main : AppCompatActivity() {
    companion object {
        val Fragment.mainActivity:Main get() = requireActivity() as Main
        @JvmStatic @BindingAdapter("mutableEndIconDrawable","onEndIconClicked",requireAll = false)
        fun mutableEndIconDrawable (
            view:TextInputLayout,
            @DrawableRes mutableEndIconDrawable:Int,
            onClickListener: View.OnClickListener?
        ) {
            view.setEndIconDrawable(mutableEndIconDrawable)
            onClickListener?.let(view::setEndIconOnClickListener)
        }
    }
    @Inject lateinit var dao:TheDao
    val sharedViewModel by viewModels<SharedViewModel>()
    val defaultPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking  {
            sharedViewModel.type = dao.checkPhoneOnLogged(defaultPreferences.getString("key",null))
        }
        setContentView(R.layout.main)
    }

}