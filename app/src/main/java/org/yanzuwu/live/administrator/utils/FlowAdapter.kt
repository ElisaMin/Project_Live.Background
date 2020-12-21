package org.yanzuwu.live.administrator.utils


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*

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

    private var count:Int = 0
    override fun getItemCount() : Int = count
    private val buffer:ArrayList<T> = ArrayList()




    init {
        scope.launch(IO) {
            flow.collect (::onCollect)
        }
    }

    class ViewHolder <B:ViewDataBinding> (val binding : B):RecyclerView.ViewHolder(binding.root)

    companion object {
        operator fun <T> invoke(
                flow : Flow<T>,
                layout:FlowAdapter<T,ViewDataBinding>.() -> Int,
                scope : CoroutineScope,
                onBind:ViewDataBinding.(T)->Unit,
        ):FlowAdapter<T,ViewDataBinding> = FlowAdapter(
                flow = flow,
                layout=layout,
                scope = scope,
                onBind = onBind
        )
    }


    @CallSuper
    fun onCollect(it:T) {
        if (filter(it)) {
            buffer.add(0,it)
            scope.launch(Main) {
                notifyItemChanged(0)
            }
            count++
        }
    }
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder<B> =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),layout(),parent,false))

    @CallSuper
    override fun onBindViewHolder(holder : ViewHolder<B>, position : Int) {
        scope.launch (Main){
            holder.binding.onBind(buffer[position])
        }
    }
}
