package org.yanzuwu.live.administrator.ui.homes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import me.heizi.kotlinx.android.default
import me.heizi.kotlinx.android.main
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.sharedViewModel
import org.yanzuwu.live.administrator.databinding.HomeFragmentBinding


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding: HomeFragmentBinding by lazy {
        HomeFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) = default {
        super.onViewCreated(view, savedInstanceState)
        main {
            binding.viewModel = viewModel
            binding.lifecycleOwner = this@HomeFragment
        }
        while (sharedViewModel.name.isEmpty()) {
            delay(1000)
            Log.i(TAG, "onViewCreated: null still")
        }
        main {
            viewModel.initialize(sharedViewModel.name)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: Home")
    }


}