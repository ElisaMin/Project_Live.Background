package org.yanzuwu.live.administrator.ui.task.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.utils.FlowAdapter
import org.yanzuwu.live.administrator.utils.dataclassess.Task


class TaskHomeViewModel @ViewModelInject constructor (
    private val repository: TaskRepository,
) : ViewModel() {


    fun start() {
        viewModelScope.launch {
            repository.getTaskByID("")
        }
    }

    val adapter =FlowAdapter<Task?,TaskItemBinding>(
            flow = repository.tasks,
            scope = viewModelScope,
            onBind = {task ->  },
            layout = {R.layout.task_item},
            filter = lambda@{ return@lambda it?.id!=null}
    )


}