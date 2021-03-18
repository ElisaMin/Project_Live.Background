package org.yanzuwu.live.administrator.ui.task.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import me.heizi.kotlinx.android.main
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskHomeFragmentBinding
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.ui.finance.peroid.BindingViewHolder

@AndroidEntryPoint
class TaskHomeFragment : Fragment(R.layout.task_home_fragment) {

    private val viewModel:TaskHomeViewModel by viewModels()

    private val adapter = object : ListAdapter<Task,BindingViewHolder<TaskItemBinding>>(object : DiffUtil.ItemCallback<Task>() { override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id;override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem }) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<TaskItemBinding> { return  TaskItemBinding.inflate(layoutInflater,parent,false).let(::BindingViewHolder) }
        override fun onBindViewHolder(holder: BindingViewHolder<TaskItemBinding>, position: Int) = holder.binding.run {
            task = getItem(position)
            root.setOnClickListener{
                anyway(task!!)
            }
            taskItemCheckbox.setOnCheckedChangeListener { buttonView, _ ->
                buttonView.isChecked = false
                anyway(task!!)
            }
        }
        override fun submitList(list: MutableList<Task>?) {
            super.submitList(list?.sortedBy { it.generateTime })
        }

    }
    private val binding  by lazy {TaskHomeFragmentBinding.bind(requireView()).also {
        it.lifecycleOwner = this
        it.viewModel = viewModel
    }}
    override fun onViewCreated(nbcs : View, savedInstanceState : Bundle?) {
        super.onViewCreated(nbcs, savedInstanceState)
        binding.lifecycleOwner = this
        viewModel.start()
//        if (viewModel.isTaskSender) {
//
//        }
        binding.adapter = adapter
        binding.taskSubTab.let { tabs -> for ( (index,it) in arrayOf("全部","待完成","已完成").withIndex()) {
            tabs.newTab()
                .setText(it)
                .setId(index)
                .let(tabs::addTab)
        };tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.filing(when(it.id) {1-> false;2 -> true;else -> null },adapter::submitList)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }
        }) }
        adapter.submitList(viewModel.list)
        viewModel.list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Task>>() {
            override fun onChanged(sender: ObservableList<Task>?) {
                adapter.submitList(sender)
            }
            override fun onItemRangeChanged(sender: ObservableList<Task>?, positionStart: Int, itemCount: Int) {}
            override fun onItemRangeInserted(sender: ObservableList<Task>?, positionStart: Int, itemCount: Int) {
                onChanged(sender)
                main {
                    adapter.notifyItemRangeInserted(positionStart,itemCount)
                }
            }
            override fun onItemRangeMoved(sender: ObservableList<Task>?, fromPosition: Int, toPosition: Int, itemCount: Int) {}
            override fun onItemRangeRemoved(sender: ObservableList<Task>?, positionStart: Int, itemCount: Int) {}
        })
    }
    private fun anyway(it:Task) {
        findNavController().navigate(R.id.task_submit_action, bundleOf("task" to it))
    }
}
