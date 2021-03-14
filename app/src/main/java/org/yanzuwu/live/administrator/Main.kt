package org.yanzuwu.live.administrator

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.models.Preferences
import javax.inject.Inject




@AndroidEntryPoint
class Main : AppCompatActivity() {
    val sharedViewModel by viewModels<SharedViewModel>()

    @Inject lateinit var preferences:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        preferences.owner = this

    }

    override fun onStart() {
        super.onStart()

        sharedViewModel.checkPreferencePhone()
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: activity")
    }

    companion object {
        const val TAG = "Main"
        val Fragment.mainActivity:Main get() = requireActivity() as Main
        val Fragment.sharedViewModel: SharedViewModel get() = mainActivity.sharedViewModel
        const val KEY_PHONE = "key1"
    }
}