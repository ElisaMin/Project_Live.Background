package org.yanzuwu.live.administrator.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.LoginFragmentBinding



@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding:LoginFragmentBinding by lazy { LoginFragmentBinding.bind(requireView()) }
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: called")
        //设置Binding的lifecycleOwner和ViewModel
        binding.run {
            this.viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
        }//设置
        lifecycleScope.launch{
            if (!mainActivity.checkPhone()) viewModel._status.collect { status ->
                Log.i(TAG, "onViewCreated: ${status.javaClass.name}")    
            when (status) {
                LoginViewModel.Status.Initialized -> {
                    viewModel.initCallback()
                }
                LoginViewModel.Status.SendingMessage,
                LoginViewModel.Status.PersonnelExist -> {
                    delay(500)
                    mainActivity.sendCode()
                }
                LoginViewModel.Status.JumpingToHome->{
                    mainActivity.sharedViewModel.phone = viewModel.phone
                    mainActivity.savePhone(viewModel.phone)
                    jumping()
                }
                else -> Log.i(TAG, "onViewCreated: $status")
            } } else {
                jumping()
            }
        }
    }

    private fun jumping() {
        findNavController().run {
            navigateUp()
            navigate(R.id.launchHome)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: Login")
    }

}