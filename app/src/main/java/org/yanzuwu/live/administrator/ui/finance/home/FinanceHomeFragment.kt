package org.yanzuwu.live.administrator.ui.finance.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.FinanceFragmentHomeBinding

@AndroidEntryPoint
class FinanceHomeFragment : Fragment(R.layout.finance_home_fragment_mount) {
    val viewModel:FinanceHomeViewModel by viewModels()
    val binding by lazy { FinanceFragmentHomeBinding.bind(requireView()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
            this.lifecycleOwner = this@FinanceHomeFragment
            this.viewModel = this@FinanceHomeFragment.viewModel
            val tabs = viewModel.tabTitles!!
            tabs.forEach {

            }
            TabLayoutMediator(financeTabs,financePages,)  { tab: TabLayout.Tab, i: Int ->
                financePages.adapter
            }.attach()
        }
        viewModel

    }

}