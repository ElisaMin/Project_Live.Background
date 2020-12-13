package org.yanzuwu.live.administrator.utils

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import org.yanzuwu.live.administrator.Main.Companion.TAG
import java.util.concurrent.Executor
import java.util.concurrent.Executors

abstract class FlowAdapter<T,B:ViewDataBinding,VH:FlowAdapter.ViewHolder<T,B>>(
        private val flow : Flow<T>,
        private inline val getScope:() -> CoroutineScope,
        areItemTheSame:(old:T,new:T) -> Boolean,
        areContentTheSame:(old:T,new:T) -> Boolean
): RecyclerView.Adapter<VH>(), LifecycleObserver {

    private var newLine:CoroutineDispatcher? = null
    private inline val scope  get() =  getScope()
    private val asyncListDiffer:AsyncListDiffer<T> = AsyncListDiffer(this, object : DiffUtil.ItemCallback<T> () {
        override fun areItemsTheSame(oldItem : T, newItem : T) : Boolean=areItemTheSame(oldItem,newItem)
        override fun areContentsTheSame(oldItem : T, newItem : T) : Boolean = areContentTheSame(oldItem,newItem)
    })

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onUsing() {
        Log.i(TAG, "onUsing: called")
        newLine = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        asyncListDiffer.submitList(mutableListOf())
        scope.launch(newLine!!) {
            flow.collect {
                asyncListDiffer.currentList.add(it)
            }
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onClean() {
        Log.i(TAG, "onClean: $itemCount")
        newLine = null
    }

    abstract class ViewHolder<T,B:ViewDataBinding>(val binding:B):RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item:T)
    }

    override fun onBindViewHolder(holder : VH, position : Int) {
        scope.launch(Main) { holder.bind(asyncListDiffer.currentList[position]) }
    }

    override fun getItemCount() : Int = asyncListDiffer.currentList.size
}