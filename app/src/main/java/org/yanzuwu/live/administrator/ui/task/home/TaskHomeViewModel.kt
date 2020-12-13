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



    val adapter =FlowAdapter<Task,TaskItemBinding>(
            flow = dao.tasks,
            scope = viewModelScope,
            onBind = {task -> this.task = task },
            layout = {R.layout.task_item},
            filter = lambda@{ return@lambda it.id!=null}
    )


}