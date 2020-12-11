package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.databinding.HomeFragmentBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    val viewModel by viewModels<HomeViewModel>()
    lateinit var homeFragmentBinding:HomeFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeFragmentBinding = HomeFragmentBinding.bind(view)
    }

}