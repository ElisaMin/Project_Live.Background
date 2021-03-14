package org.yanzuwu.live.administrator.ui.task.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import me.heizi.kotlinx.android.default
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.utils.FlowAdapter
import javax.inject.Inject

@HiltViewModel
class TaskHomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    interface Service {
        val onItemClick:(Task)->Unit
        val sendingTask:(List<Task>)->Unit
    }
    private lateinit var service:Service

    val currentPageIsShowTask get() =   _currentPageIsShowTask.asStateFlow()
    val isTaskSender get() = repository.isTaskSender
    val adapter = FlowAdapter<Task?,TaskItemBinding>(
        scope = viewModelScope,
        onBind =
        {task ->
            this.task = task
            root.setOnClickListener{
                service.onItemClick(task!!)
            }
            taskItemCheckbox.setOnCheckedChangeListener { buttonView, _ ->
                buttonView.isChecked = false
                service.onItemClick(task!!)
            }

        },
        layout = {R.layout.task_item},
        filter = lambda@{ return@lambda it?.id!=null}
    )
    private val _currentPageIsShowTask = MutableStateFlow(true)

    fun start(service: Service) {
        this.service = service
        default {
            repository.getTaskByID("")
            currentPageIsShowTask.shareIn(viewModelScope, SharingStarted.Eagerly).collectLatest {
                Log.i(TAG, "start: called")
                adapter.submitFlow(repository.tasks)
                if (!it) {

                }
            }
        }
    }
    fun sendingMessage() {
        adapter
    }
    fun filing(isDone:Boolean?) {
        Log.i(TAG, "filing: $isDone")
        when(isDone) {
            null -> repository.tasks
            else -> repository.tasks.filter { it.isDone == isDone }
        } .let {
            default { adapter.submitFlow(it) }
        }
    }


}