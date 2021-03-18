package org.yanzuwu.live.administrator.ui.task.home

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import me.heizi.kotlinx.android.default
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.models.Task
import org.yanzuwu.live.administrator.repositories.TaskRepository
import javax.inject.Inject

@HiltViewModel
class TaskHomeViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val currentPageIsShowTask get() =   _currentPageIsShowTask.asStateFlow()
    val list:ObservableList<Task> get()  = _list
    val isTaskSender get() = repository.isTaskSender
    private val _list = ObservableArrayList<Task>()
    private val _currentPageIsShowTask = MutableStateFlow(false)

    fun start() {
        collectingTask()
        default {
            repository.getTaskByID("0000")
        }
    }
    fun collectingTask() = default {
        repository.tasks.collect { _list.add(it) }
    }
    inline fun filing(isDone:Boolean?,callback:(MutableList<Task>)->Unit) {
        Log.i(TAG, "filing: $isDone")
    when(isDone) {
        null -> list
        else -> list.filter { it.isDone == isDone }
    }.toMutableList().let(callback)
    }


}