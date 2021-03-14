package org.yanzuwu.live.administrator.ui.task.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskHomeFragmentBinding
import org.yanzuwu.live.administrator.models.Task

@AndroidEntryPoint
class TaskHomeFragment : Fragment(R.layout.task_home_fragment) {

    private val viewModel:TaskHomeViewModel by viewModels()
    private val viewModelServiceImpl by lazy {
        object : TaskHomeViewModel.Service {
            override val onItemClick: (Task) -> Unit = {
                this@TaskHomeFragment.findNavController().navigate(R.id.task_submit_action, bundleOf("task" to it))
            }
            override val sendingTask: (List<Task>) -> Unit
                get() = TODO("Not yet implemented")
        }
    }
    private val binding  by lazy {TaskHomeFragmentBinding.bind(requireView()).also {
        it.lifecycleOwner = this
        it.viewModel = viewModel
    }}
    override fun onViewCreated(nbcs : View, savedInstanceState : Bundle?) {
        super.onViewCreated(nbcs, savedInstanceState)
        binding.lifecycleOwner = this
        viewModel.start(viewModelServiceImpl)
        if (viewModel.isTaskSender) {

//            TabLayoutMediator(binding.taskParentTab)
        }
        val tabs = binding.taskSubTab
//        arrayOf("全部","未完成","已完成").forEachIndexed { index, s ->
//            val tab = TabLayout.Tab()
//            tab.apply {
//                this.parent = tabs
//                val tabView:TabLayout.TabView = tabs.TabView(requireContext())
//                view = tabView
//                text = s
//                id = index
//            }
//            tabs.addTab(tab)
//        }
        binding.taskSubTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.i(TAG, "onTabSelected: called")
                tab?.let {
                    Log.i(TAG, "onTabSelected:  ${it.id} ${it.view.id} ${View.NO_ID} ")
                    when(it.id) {
                        1-> false
                        2 -> true
                        else -> null
                    }.let(viewModel::filing)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.i(TAG, "onTabReselected: called")
                onTabSelected(tab)
            }
        })

    }




}