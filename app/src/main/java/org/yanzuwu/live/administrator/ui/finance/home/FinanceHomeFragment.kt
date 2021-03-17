package org.yanzuwu.live.administrator.ui.finance.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.main
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.FinanceFragmentHomeBinding
import org.yanzuwu.live.administrator.ui.finance.peroid.PeriodView

@AndroidEntryPoint
class FinanceHomeFragment : Fragment(R.layout.finance_fragment_home) {
    val viewModel:FinanceHomeViewModel by viewModels()
    val binding by lazy { FinanceFragmentHomeBinding.bind(requireView()) }
    private lateinit var pages:Array<FinanceHomeViewModel.SinglePeriodViewModel>
    val adapter by lazy {object : RecyclerView.Adapter<SinglePageViewHolder>() {
        val billListPool = RecyclerView.RecycledViewPool()
        val ovvwListPoll = RecyclerView.RecycledViewPool()
        val loading = {it:Boolean-> viewModel.updateLoading = it }
        val content = pages
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SinglePageViewHolder {
            return PeriodView(parent,loading,billListPool,ovvwListPoll).let(::SinglePageViewHolder)
        }
        override fun onBindViewHolder(holder: SinglePageViewHolder, position: Int) {
            holder.periodView.submitViewModel(content[position])
        }
        override fun getItemCount(): Int = pages.size
    }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = default {
        super.onViewCreated(view, savedInstanceState)
        main {
            binding.lifecycleOwner = this@FinanceHomeFragment
            binding.viewModel = viewModel
            binding.financePages.isUserInputEnabled = false
        }
//            val tabs = viewModel.tabTitles!!
        var pages = viewModel.pages
        while (pages == null) {
            Log.i(TAG, "onViewCreated: null ,delaying")
            delay(1)
            pages = viewModel.pages
        }
        this@FinanceHomeFragment.pages = pages.toTypedArray()
        adapter
        main {
            binding.financePages.isVisible = false
            binding.financePages.adapter = adapter
            val tabs = binding.financeTabs
            TabLayoutMediator(tabs,binding.financePages) { tab: TabLayout.Tab, i: Int ->
                tab.text = pages[i].periodName
                tab.id = i
            } .attach()
            binding.financePages.isVisible = true
            tabs.isVisible = true
            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener  {
                override fun onTabSelected(tab: TabLayout.Tab?) { viewModel.updateLoading = true }override fun onTabUnselected(tab: TabLayout.Tab?) {}override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
            binding.financePages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateLoading = false
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateLoading = false
    }

    class SinglePageViewHolder(val periodView: PeriodView):RecyclerView.ViewHolder(periodView.view) {}
}