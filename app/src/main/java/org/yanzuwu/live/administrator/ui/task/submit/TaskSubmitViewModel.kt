package org.yanzuwu.live.administrator.ui.task.submit

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.repositories.TaskRepository



class TaskSubmitViewModel @ViewModelInject constructor (
    private val repository: TaskRepository,
) : ViewModel() {

    lateinit var task:Task


}
