package org.yanzuwu.live.administrator.ui.homes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.main
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.sharedViewModel
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.models.UserType

class EmptyFragmentForNavigating:Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreateNav: called")
        lifecycleScope.default collect@{
            Log.d(TAG, "onCreate: nav task")
            sharedViewModel.type.collect {
                Log.d(TAG, "onCreateNav: changed $it")
                when(it) {
                    UserType.READY,
                    null -> Log.d(TAG, "onCreate: to fast")
                    UserType.NOT_ARROW -> throw IllegalStateException("shouldn't be here")
                    UserType.HighLevelTaskWorker,
                    UserType.TaskWorker -> {
                        main {
                            findNavController().navigate(R.id.toSubNavTask)
                        }.join()
                        cancel()
                    }
                    UserType.MoneyManager -> {
                        main {
                            findNavController().navigate(R.id.toSubNavMoneyManager)
                            Log.d(TAG, "onCreate: skiped")
                        }.join()
                        cancel()
                    }
                    UserType.PersonalManager -> TODO()
                }
            }
        }.start()
    }
}