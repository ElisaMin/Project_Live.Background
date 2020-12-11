package org.yanzuwu.live.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.content.edit
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
import kotlinx.coroutines.withContext
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.data.SharedViewModel
import org.yanzuwu.live.administrator.data.TheDao
import org.yanzuwu.live.administrator.utils.dialog
import javax.inject.Inject

@AndroidEntryPoint
class Main : AppCompatActivity() {
    companion object {
        const val TAG = "Main"
        val Fragment.mainActivity:Main get() = requireActivity() as Main
        const val KEY_PHONE = "key1"
    }
    @Inject lateinit var dao:TheDao
    val sharedViewModel by viewModels<SharedViewModel>()
    val defaultPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    suspend fun updateType(phone:String?=defaultPreferences.getString(KEY_PHONE,null)) {
        sharedViewModel.type.emit(dao.checkPhoneOnLogged(phone))
    }
    fun savePhone(phone: String?) = lifecycleScope.launch(IO) {
        defaultPreferences.edit(commit = true) {
            putString(KEY_PHONE,phone)
        }
    }
    fun sendCode() {
        lifecycleScope.launch(Main) {
            dialog(
                    title = "验证码",
                    message = TheDao.code.toString(),
                    isShowUp = true
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
    }
}