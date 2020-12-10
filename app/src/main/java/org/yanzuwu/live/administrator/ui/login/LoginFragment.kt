package org.yanzuwu.live.administrator.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.TheDao
import org.yanzuwu.live.administrator.databinding.LoginFragmentBinding
import javax.inject.Inject

class LoginFragment : Fragment() {
    private var _binding:LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when(mainActivity.sharedViewModel.type) {
            TheDao.UserType.NOT_ARROW -> {
                _binding = DataBindingUtil.inflate(layoutInflater,R.layout.login_fragment,container,false);
                binding.root
            }
            else -> {
                null
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.viewModel = viewModel
    }

}