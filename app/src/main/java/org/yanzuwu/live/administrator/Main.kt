package org.yanzuwu.live.administrator

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.yanzuwu.live.administrator.models.UserType
import org.yanzuwu.live.administrator.repositories.UserRepository
import org.yanzuwu.live.administrator.utils.dialog
import javax.inject.Inject

@AndroidEntryPoint
class Main : AppCompatActivity() {
    companion object {
        const val TAG = "Main"
        val Fragment.mainActivity:Main get() = requireActivity() as Main
        val Fragment.sharedViewModel: SharedViewModel get() = mainActivity.sharedViewModel
        const val KEY_PHONE = "key1"
    }
    val sharedViewModel by viewModels<SharedViewModel>()
    private val defaultPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }
    @Inject lateinit var repository: UserRepository
    /**
     * Check phone
     *
     * @param phone 不输入时从手机内部获取
     * @return 当服务器内验证该手机可登入返回true 否则false
     */
    suspend fun checkPhone(phone:String?=defaultPreferences.getString(KEY_PHONE,null)):Boolean {
        sharedViewModel.phone = phone
        return withContext(IO){sharedViewModel.type.value != UserType.NOT_ARROW}
    }
    fun savePhone(phone: String?) = lifecycleScope.launch(IO) {
        defaultPreferences.edit(commit = true) {
            putString(KEY_PHONE,phone)
        }
    }

    /**
     * Send code
     *
     */
    fun sendCode() {
        lifecycleScope.launch(Main) {
            dialog(
                    title = "验证码",
                    message = repository.code.toString(),
                    isShowUp = true
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: activity")
    }
}