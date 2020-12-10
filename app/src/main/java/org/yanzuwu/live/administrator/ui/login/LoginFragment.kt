package org.yanzuwu.live.administrator.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.TheDao
import org.yanzuwu.live.administrator.databinding.LoginFragmentBinding
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding:LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    @Inject lateinit var dao: TheDao

    fun checkUserType() {
        lifecycleScope.launch {
            //操作
            when(mainActivity.updateType()) {

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (mainActivity.sharedViewModel.type == TheDao.UserType.NOT_ARROW) {
            _binding = DataBindingUtil.inflate(layoutInflater,R.layout.login_fragment,container,false)
            return binding.root
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.run {
            this.viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
        }
        viewModel.isShowingProgressBar.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                delay(500)
                when (mainActivity.updateType(viewModel.input.value)) {
                    TheDao.UserType.NOT_ARROW -> {
                        if (viewModel.next) {
                            viewModel.notAllowCallback()
                        }
                    }else -> {
                        if (viewModel.next) {
                            viewModel.verificationCallback(dao.getUserNameByID(viewModel.input.value?.runCatching { toInt() }?.getOrNull() ?:0))
                            checkUserType()
                        }
                    }
                }
            }
        }

    }

}