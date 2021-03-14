package org.yanzuwu.live.administrator.utils


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.main

/**
 * 适配Flow类型的RecyclerViewAdapter
 * 可以重写onCollect方法
 *
 * @param T 该FLOW的类型
 * @param B 不需要使用ViewHolder 只需要DataBinding
 * @param layout return 一个LayoutResource
 * @param onBind
 * @param scope 需要[CoroutineScope]
 * @param filter
 * @constructor 封闭类 现在不是 过段时间就是了
 */
open class FlowAdapter <T,B:ViewDataBinding> constructor(
    val layout:FlowAdapter<T,B>.() -> Int,
    val onBind:B.(T)->Unit,
    val scope : CoroutineScope,
    inline  val  filter: ((T)->Boolean) = {true}
):RecyclerView.Adapter<FlowAdapter.ViewHolder<B>>() {
    /**
     * 数据源
     */
    private lateinit var flow: Flow<T>
    /**
     * Flow collect task 更好的取消和启动
     */
    private lateinit var flowCollectTask: Job
    /**
     * Shared flow 防止多地collect占线
     */
    private val sharedFlow get() = flow.shareIn(scope, SharingStarted.Eagerly)
    /**
     * buffer : ArrayList 用于缓冲flow
     */
    private var buffer:ArrayList<T> = ArrayList()

    /**
     * Start collect flow
     *
     * 启动收集flow
     */
    private fun startCollectFlow() {
        flowCollectTask = scope.default {
            sharedFlow.collect(::onCollect)
        }
    }

    /**
     * Submit flow
     *
     * 提交一个Flow给Adapter
     */
    suspend fun submitFlow(flow: Flow<T>) {
        //停止collect
        runCatching { flowCollectTask.cancelAndJoin() }
        //清除当前所有的信息
        buffer.clear()
        scope.main {
            notifyDataSetChanged()
        }
        this.flow = flow
        startCollectFlow()
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
//        Log.i(TAG, "onBindViewHolder: position $position") // always be 0
        scope.launch(Main) {
            holder.binding.onBind(buffer[position])
        }
    }

    private suspend fun onCollect(it:T) = scope.default {
        if (filter(it)) {
            buffer.add(0,it)
             launch(Main) {
                notifyItemInserted(0)
            }
        }
    }

    /**
     * 不重要
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),layout(),parent,false))
    class ViewHolder <B:ViewDataBinding> (val binding : B):RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int = buffer.size

}
