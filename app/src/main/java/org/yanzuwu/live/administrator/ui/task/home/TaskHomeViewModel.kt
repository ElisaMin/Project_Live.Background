package org.yanzuwu.live.administrator.ui.task.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R

import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.repositories.TaskRepository.tasks
import org.yanzuwu.live.administrator.utils.FlowAdapter
import org.yanzuwu.live.administrator.utils.dataclassess.Task


class TaskHomeViewModel() : ViewModel() {



    val adapter =FlowAdapter<Task,TaskItemBinding>(
            flow = tasks,
            scope = viewModelScope,
            onBind = {task -> this.task = task },
            layout = {R.layout.task_item},
            filter = lambda@{ return@lambda it.id!=null}
    )


}