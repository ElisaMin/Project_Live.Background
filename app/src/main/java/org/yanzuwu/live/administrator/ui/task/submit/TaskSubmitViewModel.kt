package org.yanzuwu.live.administrator.ui.task.submit

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.repositories.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskSubmitViewModel @Inject constructor (
    private val repository: TaskRepository,
) : ViewModel() {

    lateinit var task:Task


}
