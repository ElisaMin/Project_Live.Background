package org.yanzuwu.live.administrator.ui.task.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskHomeFragmentBinding

@AndroidEntryPoint
class TaskHomeFragment : Fragment(R.layout.task_home_fragment) {



    private val viewModel:TaskHomeViewModel by viewModels()
    private val binding  by lazy {TaskHomeFragmentBinding.bind(requireView()).also {
        it.lifecycleOwner = this
        it.viewModel = viewModel
        it.taskListTaskHome.adapter = viewModel.adapter
    }}

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        viewModel.start(findNavController())
        lifecycleScope.launch {

        }
    }


}