package org.yanzuwu.live.administrator.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import org.yanzuwu.live.administrator.Main.Companion.KEY_PHONE
import org.yanzuwu.live.administrator.Main.Companion.TAG
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

    private var isLaunchUp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Main) {
            mainActivity.sharedViewModel.type.asLiveData().observe(this@LoginFragment) { type ->
                if (isLaunchUp) {
                    if (type == TheDao.UserType.NOT_ARROW) {
                        viewModel.initCallback()
                        isLaunchUp = false
                    } else {
                        launch(Main) {
                            findNavController().navigateUp()
                            findNavController().navigate(R.id.launchHome)
                        }
                    }
                }
            }
        }
        lifecycleScope.launch(IO) {
            mainActivity.updateType()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(layoutInflater,R.layout.login_fragment,container,false)
        return binding.root
    }

    private var whenVerificationCodeInput:Observer<String>? = null
    private fun getWhenVerificationCodeInput() = lifecycleScope.async (IO) {
        if (whenVerificationCodeInput==null) whenVerificationCodeInput = Observer<String> { with(viewModel) {
            if (it!==null){
                if (it.length == 4 ) lifecycleScope.launch(IO) { //4个输入时
                    launch(Main) {
                        isSendAgain = false
                        onDealing() //被再次调用
                    } //正在处理中
                    if (dao.checkVerificationCode(it)) launch(IO) {
                        isLaunchUp = true
                        mainActivity.sharedViewModel.run {
                            phone = phone
                        }
                    }  else launch(Main) {
                        badVerifyCodeCallback()
                    }
                }else launch(Main) {
                    verificationCallback()
                }
            }
        } }
        whenVerificationCodeInput!!
    }
    private fun removeWhenVerificationCodeInput() {
        whenVerificationCodeInput?.let(viewModel.input::removeObserver)
        whenVerificationCodeInput = null

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: called")
        _binding?.run {
            this.viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
        }

        //提高代码重用性
        viewModel.initCallback()
        //处理加载事件
        viewModel.isShowingProgressBar.observe(viewLifecycleOwner) { onProcessing ->
            //在加载时调用
            if (onProcessing&&viewModel.isPass) lifecycleScope.launch(Main) { with(viewModel) {
                delay(500)
                //判断处理事件为发送还是验证登入
                if (isUserExist  == null) {
                    removeWhenVerificationCodeInput()
                    //加载用户是否存在
                    isLaunchUp = false
                    mainActivity.sharedViewModel.phone = input.value
                    lifecycleScope.launch(IO) {
                        mainActivity.sharedViewModel.type.asSharedFlow()
                            .collectLatest { type ->
                                Log.i(TAG, "onViewCreated: $type")
                                isUserExist = (type!=TheDao.UserType.NOT_ARROW)
                                cancel()
                            }

                    }
                    Log.i(TAG, "onViewCreated: out of the flow")
                    //不存在时结束
                    if (isUserExist == false) {
                        notAllowCallback()
                    } else { //存在时立即发送验证码并且把状态切换至验证码状态
                        launch(IO) { mainActivity.sendCode() }
                        onDealing()
                        verificationCallback(dao.getUserNameByID(viewModel.input.value?.runCatching { toInt() }?.getOrNull() ?:0))
                        input.value=null
                        input.observe(viewLifecycleOwner,getWhenVerificationCodeInput().await())
                    }
                //处理验证事件
                }else {
                    //验证码已发送时则为点击再次发送状态
                    if (isSendAgain) {
                        isSendAgain = false
                        viewModel.input.value = null
                        onDealing()
                        launch (IO) { mainActivity.sendCode();launch(Main){ verificationCallback() } }
                    }
                }
            } }
        }
    }

}