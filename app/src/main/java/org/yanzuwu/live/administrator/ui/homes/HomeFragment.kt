package org.yanzuwu.live.administrator.ui.homes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

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