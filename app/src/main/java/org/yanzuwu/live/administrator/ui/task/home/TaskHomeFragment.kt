package org.yanzuwu.live.administrator.ui.task.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.TheDao
import org.yanzuwu.live.administrator.databinding.TaskHomeFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class TaskHomeFragment : Fragment(R.layout.task_home_fragment) {


    @Inject lateinit var dao : TheDao
    private val viewModel:TaskHomeViewModel by viewModels()
    private val binding  by lazy {TaskHomeFragmentBinding.bind(requireView()).also {
        Log.i(TAG, "onBinding: called")
        it.lifecycleOwner = this
        it.viewModel = viewModel
        it.taskListTaskHome.adapter = viewModel.adapter
        Log.i(TAG, "onBinding: adapter is null is ${it.taskListTaskHome.adapter==null}")
    }}

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.lifecycle.addObserver(viewModel.adapter)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding
        lifecycleScope.launch(IO) {
            Log.i(TAG, "onViewCreated: onIo")
            dao.getTaskByID("")
            Log.i(TAG, "onViewCreated: onIoDone")
        }
        lifecycleScope.launch(IO) {
            delay(1000)
            Log.i(TAG, "onViewCreated: onIo")
        }
    }


}