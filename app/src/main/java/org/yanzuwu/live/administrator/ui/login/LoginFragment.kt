package org.yanzuwu.live.administrator.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import org.yanzuwu.live.administrator.Main.Companion.KEY_PHONE
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.TheDao
import org.yanzuwu.live.administrator.databinding.HomeFragmentBinding
import org.yanzuwu.live.administrator.databinding.LoginFragmentBinding
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {
    private val binding:LoginFragmentBinding by lazy { LoginFragmentBinding.bind(requireView()) }
    private val viewModel: LoginViewModel by viewModels()
    @Inject lateinit var dao: TheDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: called")
        binding.run {
            this.viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
        }
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

}