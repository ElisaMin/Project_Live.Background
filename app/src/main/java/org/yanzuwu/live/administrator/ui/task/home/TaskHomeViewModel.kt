package org.yanzuwu.live.administrator.ui.task.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.beans.Task
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.repositories.Repository.dao
import org.yanzuwu.live.administrator.utils.FlowAdapter


class TaskHomeViewModel() : ViewModel() {

    class ViewHolder(binding:TaskItemBinding):FlowAdapter.ViewHolder<Task,TaskItemBinding>(binding) {
        override fun bind(item : Task) {
            binding.task = item
            Log.i(TAG, "bind: $item")
        }
    }

    val adapter = object : FlowAdapter<Task,TaskItemBinding,ViewHolder>(
            flow = dao.tasks,
            getScope = {viewModelScope},
            areContentTheSame = {old, new -> false },
            areItemTheSame = {old, new -> false }
    ) { override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : TaskHomeViewModel.ViewHolder
            = TaskHomeViewModel.ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.task_item,parent,false)) }


}