package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.sharedViewModel
import org.yanzuwu.live.administrator.R

import org.yanzuwu.live.administrator.databinding.HomeFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    private val viewModel by viewModels<HomeViewModel>()
    private val binding:HomeFragmentBinding by lazy {
        HomeFragmentBinding.bind(requireView()).let {
            it.viewModel = viewModel
            it.lifecycleOwner = this
            it
        }
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        viewModel.initialize()

//        when(sharedViewModel.type.value){
//            TheDao.UserType.HighLevelTaskWorker,
//            TheDao.UserType.TaskWorker -> {
//                 R.navigation.task_sub_nav
//            }
//            else  -> 0
//        } .let(binding.containerJusthome.findNavController()::setGraph)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: Home")
    }


}