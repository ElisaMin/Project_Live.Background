package org.yanzuwu.live.administrator.utils


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 适配Flow类型的RecyclerViewAdapter
 * 可以重写onCollect方法
 *
 * @param T 该FLOW的类型
 * @param B 不需要使用ViewHolder 只需要DataBinding
 * @param flow
 * @param layout return 一个LayoutResource
 * @param onBind
 * @param scope 需要[CoroutineScope]
 * @param filter
 * @constructor 封闭类 现在不是 过段时间就是了
 */


open class FlowAdapter <T,B:ViewDataBinding> constructor(
        val flow:Flow<T>,
        val layout:FlowAdapter<T,B>.() -> Int,
        val onBind:B.(T)->Unit,
        val scope : CoroutineScope,
        inline  val  filter: ((T)->Boolean) = {true}
):RecyclerView.Adapter<FlowAdapter.ViewHolder<B>>() {
    /**
     * 不重要
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> = ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),layout(),parent,false))
    class ViewHolder <B:ViewDataBinding> (val binding : B):RecyclerView.ViewHolder(binding.root)

    /**
     * buffer : ArrayList 用于缓冲flow
     */
    private val buffer:ArrayList<T> = ArrayList()

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
//        Log.i(TAG, "onBindViewHolder: position $position") // always be 0
        scope.launch(Main) {
            holder.binding.onBind(buffer[position])
        }
    }
    override fun getItemCount(): Int = buffer.size

    private suspend fun onCollect(it:T) = scope.launch(IO) {
        if (filter(it)) {
            buffer.add(0,it)
             launch(Main) {
                notifyItemInserted(0)
            }
        }
    }

    init {
        scope.launch(IO) {
             flow.collect(::onCollect)
        }
    }

}
