package org.yanzuwu.live.administrator.utils


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*

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
        scope.launch(Main) {
            flow.
            filter { filter(it) }.
            collect {
                buffer.add(0,it)
                notifyItemChanged(0)
                count++
            }
        }
    }

    class ViewHolder <B:ViewDataBinding> (val binding : B):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder<B> =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),layout(),parent,false))
    override fun onBindViewHolder(holder : ViewHolder<B>, position : Int) {
        scope.launch (Main){
            holder.binding.onBind(buffer[position])
        }
    }
}