package org.yanzuwu.live.administrator.ui.task.home

import androidx.core.os.bundleOf
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.utils.FlowAdapter
import java.io.Serializable


class TaskHomeViewModel @ViewModelInject constructor (
    private val repository: TaskRepository,
) : ViewModel() {


    private lateinit var onClick:(Task)->Unit

    fun start(navController: NavController) {
        viewModelScope.launch {
            repository.getTaskByID("")
        }
        onClick = {
            navController.navigate(R.id.task_submit_action, bundleOf("task" to it))
        }
    }

    val adapter = FlowAdapter<Task?,TaskItemBinding>(
        flow = repository.tasks,
        scope = viewModelScope,
        onBind =
        {task ->
            this.task = task
            root.setOnClickListener{
                onClick(task!!)
            }
        },
        layout = {R.layout.task_item},
        filter = lambda@{ return@lambda it?.id!=null}
    )
    sealed class test:Serializable {

    }

}