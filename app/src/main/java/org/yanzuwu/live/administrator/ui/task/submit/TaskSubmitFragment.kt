package org.yanzuwu.live.administrator.ui.task.submit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.TaskSubmitFragmentBinding
import org.yanzuwu.live.administrator.models.Task

@AndroidEntryPoint
class TaskSubmitFragment : BottomSheetDialogFragment() {
    private val binding by lazy { TaskSubmitFragmentBinding.bind(requireView()) }
    private val viewModel by viewModels<TaskSubmitViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
        = layoutInflater.inflate(R.layout.task_submit_fragment,container,false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.task = (arguments?.get("task") as Task)//捕获Task 没有则炸开
    }
}
