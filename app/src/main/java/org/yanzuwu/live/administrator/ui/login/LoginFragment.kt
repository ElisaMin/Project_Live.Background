package org.yanzuwu.live.administrator.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.*
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.dialog
import me.heizi.kotlinx.android.main
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.Main.Companion.sharedViewModel
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.LoginFragmentBinding
import org.yanzuwu.live.administrator.models.UserType


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding:LoginFragmentBinding by lazy { LoginFragmentBinding.bind(requireView()) }
    private val viewModel: LoginViewModel by viewModels()

    private val collectState = lifecycleScope.launch(Default,CoroutineStart.LAZY) {
        Log.i(TAG, "collectubg: ")
        viewModel.state.collectLatest { status ->
            Log.d(TAG, "collect: $status")
        when (status) {
            is LoginViewModel.Status.SendingMessage, -> {
                delay(500)
                main {
                    context?.dialog(title = status.code)
                }
            }
            LoginViewModel.Status.JumpingToHome -> {
                mainActivity.sharedViewModel.phone = viewModel.phone
            }
            else -> Log.i(TAG, "onViewCreated: $status")
        } }
    }

    private val collectType = lifecycleScope.launch(Default,CoroutineStart.LAZY) {
            sharedViewModel.type.collect {
                Log.i(TAG, "UserType: $it")
                if (it!=null) if (it == UserType.NOT_ARROW ) default {
                    collectState.start()
                    viewModel.start()
                    Log.d(TAG, "collect: startd")
                } else {
                    Log.d(TAG, "colllect: cancel")
                    collectState.cancelAndJoin()
                    jumping()
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel
        Log.d(TAG, "onCreate: called")
        collectType.start()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: called")
        //设置Binding的lifecycleOwner和ViewModel
        binding.run {
            viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
        }
    }


    private fun jumping() = main {
        findNavController().run {
            navigate(R.id.launchHome)
            this.popBackStack()
            lifecycleScope.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        collectState.cancel()
        Log.i(TAG, "onDestroy: Login")
    }

}