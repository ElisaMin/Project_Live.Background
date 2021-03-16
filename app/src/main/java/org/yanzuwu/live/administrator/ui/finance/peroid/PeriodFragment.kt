package org.yanzuwu.live.administrator.ui.finance.peroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.yanzuwu.live.administrator.databinding.FinanceHomeFragmentMountBinding
import org.yanzuwu.live.administrator.databinding.FinanceItemPendingHomeBinding
import org.yanzuwu.live.administrator.databinding.FinanceItemPreviewBinding
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel.BillOverviewViewModel as Overview
import org.yanzuwu.live.administrator.ui.finance.home.FinanceHomeViewModel.ToDoneViewModel as ToDone

class PeriodAdapter:ListAdapter<FinanceHomeViewModel.SinglePeriodViewModel,BindingViewHolder<FinanceHomeFragmentMountBinding>>(
    object : DiffUtil.ItemCallback<FinanceHomeViewModel.SinglePeriodViewModel>() {
        override fun areItemsTheSame(oldItem: FinanceHomeViewModel.SinglePeriodViewModel, newItem: FinanceHomeViewModel.SinglePeriodViewModel): Boolean = oldItem.titleFirst == newItem.titleFirst && oldItem.titleSecond == newItem.titleSecond
        override fun areContentsTheSame(oldItem: FinanceHomeViewModel.SinglePeriodViewModel, newItem: FinanceHomeViewModel.SinglePeriodViewModel): Boolean =false
    }
) {
    /** 预览Adapter */
    private val overviewsAdapter get() =  object : ListAdapter<Overview,BindingViewHolder<FinanceItemPreviewBinding>>(
        object : DiffUtil.ItemCallback<Overview>() { override fun areItemsTheSame(oldItem: Overview, newItem: Overview): Boolean = oldItem.billName == newItem.billName;override fun areContentsTheSame(oldItem: Overview, newItem: Overview): Boolean = oldItem.done == newItem.done && oldItem.roomCount == newItem.roomCount && oldItem.billName == newItem.billName}
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPreviewBinding> = FinanceItemPreviewBinding.inflate(layoutInflater,parent,false).let(::BindingViewHolder)
        override fun onBindViewHolder(holder: BindingViewHolder<FinanceItemPreviewBinding>, position: Int) { holder.binding.viewModel = getItem(position) }
    }
    /**  待解决Adapter */
    private val toDone get() =  object : ListAdapter<ToDone,BindingViewHolder<FinanceItemPendingHomeBinding>>(
        object : DiffUtil.ItemCallback<ToDone>() {
            override fun areItemsTheSame(oldItem: ToDone, newItem: ToDone): Boolean = oldItem.phone == newItem.phone
            override fun areContentsTheSame(oldItem: ToDone, newItem: ToDone): Boolean  = false
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPendingHomeBinding> =
            FinanceItemPendingHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false).let(::BindingViewHolder)
        override fun onBindViewHolder(holder:BindingViewHolder<FinanceItemPendingHomeBinding>, position: Int) {
            holder.binding.viewModel = getItem(position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceHomeFragmentMountBinding> { return FinanceHomeFragmentMountBinding.inflate(LayoutInflater.from(parent.context),parent,false).let(::BindingViewHolder) }

    override fun onBindViewHolder(
        holder: BindingViewHolder<FinanceHomeFragmentMountBinding>,
        position: Int
    ) {
        getItem()
    }
}(

)
class PeriodFragment(
    val viewModel:FinanceHomeViewModel.SinglePeriodViewModel
):BottomSheetDialogFragment() {
    /** */
    val binding by lazy { FinanceHomeFragmentMountBinding.inflate(LayoutInflater.from(requireParentFragment().context)) }
    /** 预览Adapter */
    private val overviewsAdapter = object : ListAdapter<Overview,BindingViewHolder<FinanceItemPreviewBinding>>(
        object : DiffUtil.ItemCallback<Overview>() { override fun areItemsTheSame(oldItem: Overview, newItem: Overview): Boolean = oldItem.billName == newItem.billName;override fun areContentsTheSame(oldItem: Overview, newItem: Overview): Boolean = oldItem.done == newItem.done && oldItem.roomCount == newItem.roomCount && oldItem.billName == newItem.billName}
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPreviewBinding> = FinanceItemPreviewBinding.inflate(layoutInflater,parent,false).let(::BindingViewHolder)
        override fun onBindViewHolder(holder: BindingViewHolder<FinanceItemPreviewBinding>, position: Int) { holder.binding.viewModel = getItem(position) }
    }
    /**  待解决Adapter */
    private val toDone = object : ListAdapter<ToDone,BindingViewHolder<FinanceItemPendingHomeBinding>>(
        object : DiffUtil.ItemCallback<ToDone>() {
            override fun areItemsTheSame(oldItem: ToDone, newItem: ToDone): Boolean = oldItem.phone == newItem.phone
            override fun areContentsTheSame(oldItem: ToDone, newItem: ToDone): Boolean  = false
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<FinanceItemPendingHomeBinding> =
            FinanceItemPendingHomeBinding.inflate(layoutInflater,parent,false).let(::BindingViewHolder)
        override fun onBindViewHolder(holder:BindingViewHolder<FinanceItemPendingHomeBinding>, position: Int) {
            holder.binding.viewModel = getItem(position)
        }
    }
    /** */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.adapterOverview = overviewsAdapter
        binding.adapterToDone = toDone
        updateViewModel()
    }
    /** */
    fun updateViewModel(viewModel: FinanceHomeViewModel.SinglePeriodViewModel = this.viewModel) {
        binding.viewModel = viewModel
        overviewsAdapter.submitList(viewModel.overView)
        toDone.submitList(viewModel.toDone)
    }
}

/** 一个ViewHolder */
class BindingViewHolder<T: ViewBinding>(val binding:T) : RecyclerView.ViewHolder(binding.root)
