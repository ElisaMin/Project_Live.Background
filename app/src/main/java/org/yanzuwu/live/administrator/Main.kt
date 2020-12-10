package org.yanzuwu.live.administrator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.withContext
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.data.SharedViewModel
import org.yanzuwu.live.administrator.data.TheDao
import javax.inject.Inject

@AndroidEntryPoint
class Main : AppCompatActivity() {
    companion object {
        val Fragment.mainActivity:Main get() = requireActivity() as Main
    }
    @Inject lateinit var dao:TheDao
    val sharedViewModel by viewModels<SharedViewModel>()
    private val defaultPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    suspend fun updateType(phone:String?=defaultPreferences.getString("key",null)):TheDao.UserType {
        sharedViewModel.type =
            withContext(IO) { dao.checkPhoneOnLogged(phone) }
        return sharedViewModel.type
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking  {
            updateType()
        }
        setContentView(R.layout.main)
    }
}