package org.yanzuwu.live.administrator.ui.recycler_view_adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.StateFlow
import org.yanzuwu.live.administrator.data.beans.Task
import org.yanzuwu.live.administrator.databinding.TaskItemBinding
import org.yanzuwu.live.administrator.utils.FlowAdapter





//class TaskAdapter(): ListAdapter<Task, TaskAdapter.ViewHolder>(
//        object : DiffUtil.ItemCallback<Task>() {
//            override fun areItemsTheSame(oldItem : Task, newItem : Task) : Boolean = (oldItem.id == newItem.id)
//            override fun areContentsTheSame(oldItem : Task, newItem : Task) : Boolean = (oldItem.id == newItem.id)
//        }
//) {
//
//
//    class ViewHolder(private val binding: TaskItemBinding): RecyclerView.ViewHolder(binding.root) {
//        fun binding(position : Int) {
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder = ViewHolder(TaskItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
//    override fun onBindViewHolder(holder : ViewHolder, position : Int) = position.let(holder::binding)
//}
