package org.yanzuwu.live.administrator.ui.task.home

import androidx.lifecycle.*
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.dataclasses.Task
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.repositories.task.TaskRepository.tasks
import org.yanzuwu.live.administrator.utils.FlowAdapter


class TaskHomeViewModel() : ViewModel() {



    val adapter =FlowAdapter<Task,TaskItemBinding>(
            flow = tasks,
            scope = viewModelScope,
            onBind = {task -> this.task = task },
            layout = {R.layout.task_item},
            filter = lambda@{ return@lambda it.id!=null}
    )


}