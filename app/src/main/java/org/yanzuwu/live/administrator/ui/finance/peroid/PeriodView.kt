package org.yanzuwu.live.administrator.ui.finance.peroid

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.databinding.FinanceHomeFragmentMountBinding
import org.yanzuwu.live.administrator.databinding.FinanceItemPendingHomeBinding
import org.yanzuwu.live.administrator.databinding.FinanceItemPreviewBinding
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel.BillOverviewViewModel as Overview
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel.ToDoneViewModel as ToDone



class PeriodView(
    parent: ViewGroup?,
    val setLoading:(boolean: Boolean)->Unit = {},
    poolToDone:RecyclerView.RecycledViewPool,
    poolOverview:RecyclerView.RecycledViewPool,
) {
    private val context = parent?.context
    val view get() = binding.root as NestedScrollView
    val binding = FinanceHomeFragmentMountBinding.inflate(LayoutInflater.from(context),parent,false)

    init {
        binding.listToDoneFinanceMonth.setRecycledViewPool(poolToDone)
        binding.listOverviewsFinanceMonth.setRecycledViewPool(poolOverview)
    }

    /** 预览Adapter */
    private val overviewsAdapter = object : ListAdapter<Overview,BindingViewHolder<FinanceItemPreviewBinding>>(
        object : DiffUtil.ItemCallback<Overview>() {
            override fun areItemsTheSame(oldItem: Overview, newItem: Overview): Boolean {
                val b = oldItem.billName == newItem.billName
                Log.i(TAG, "areItemsTheSame: b")
                return b
            };override fun areContentsTheSame(oldItem: Overview, newItem: Overview): Boolean = oldItem.done == newItem.done && oldItem.roomCount == newItem.roomCount && oldItem.billName == newItem.billName}
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPreviewBinding> = FinanceItemPreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false).let(::BindingViewHolder)
        override fun onBindViewHolder(holder: BindingViewHolder<FinanceItemPreviewBinding>, position: Int) {
            Log.i(TAG, "onBindViewHolder: called $position ${getItem(position)} ")
            holder.binding.viewModel = getItem(position)
        }
        override fun getItemCount(): Int {
            val itemCount = super.getItemCount()
            Log.i(TAG, "getItemCount: $itemCount")
            return itemCount
        }

        override fun submitList(list: MutableList<FinanceHomeViewModel.BillOverviewViewModel>?) {
            Log.i(TAG, "submitList: ${list?.size}")
            super.submitList(list)
        }
    }
    /** 待解决Adapter */
    private val toDone =  object : ListAdapter<ToDone,BindingViewHolder<FinanceItemPendingHomeBinding>>(
        object : DiffUtil.ItemCallback<ToDone>() {
            override fun areItemsTheSame(oldItem: ToDone, newItem: ToDone): Boolean = oldItem.phone == newItem.phone
            override fun areContentsTheSame(oldItem: ToDone, newItem: ToDone): Boolean  = false
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPendingHomeBinding> = FinanceItemPendingHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false).let(::BindingViewHolder)
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder:BindingViewHolder<FinanceItemPendingHomeBinding>, position: Int) {
            val viewModel = getItem(position)
            val binding = holder.binding
            binding.viewModel = viewModel
            if(viewModel.toPays.isEmpty()) binding.root.isVisible = false
            viewModel.toPays.keys.forEach {
                val b = viewModel.toPays[it]
                binding.financeItemChipsPending.addView(Chip(context).apply {
                    text = "${it.translate}未缴：$b"
                })
            }

        }
    }

    fun submitViewModel(viewModel:FinanceHomeViewModel.SinglePeriodViewModel) {
        setLoading(true)
        binding.viewModel = viewModel
        binding.adapterToDone = toDone
        binding.adapterOverview = overviewsAdapter
        overviewsAdapter.submitList(ArrayList(viewModel.overView))
        toDone.submitList(viewModel.toDone)
        setLoading(false)

    }
}

