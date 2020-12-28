package org.yanzuwu.live.administrator.ui.login


import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.repositories.UserRepository
import org.yanzuwu.live.administrator.utils.dataclassess.UserType
import javax.inject.Inject


class LoginViewModel @ViewModelInject constructor(
    private val repository: UserRepository,
) : ViewModel() {
    /**
     * username （其实是提示文字
     */
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    /**
     * Hint 提示框内容
     */
    private val _hint = MutableLiveData<String>()
    val hint: LiveData<String> = _hint

    /**
     * input 输入内容
     */
    val input = MutableLiveData<String>()

    /**
     * notice text 提示文字
     */
    private val _noticeText = MutableLiveData<String>()
    val noticeText: LiveData<String> = _noticeText

    /**
     * End icon drawable 图标
     */
    private val _endIconDrawable = MutableLiveData<Int>()
    val endIconDrawable get() = _endIconDrawable as LiveData<Int>

    /**
     * max input 限制输入数
     */
    private val _maxInput = MutableLiveData<Int>()
    val maxInputCount get() = _maxInput as LiveData<Int>

    /**
     * is showing progress bar 是否展示ProcessBar
     */
    private val _isShowingProgressBar = MutableLiveData<Boolean>()
    val isShowingProgressBar get() = _isShowingProgressBar as LiveData<Boolean>

    /**
     * is visible 除了process bar外的元素是否可见
     */
    private val _isVisible = MutableLiveData(false)
    val isVisible get() = _isVisible as LiveData<Boolean>

    /**
     * is not failed 除了文字啥都隐藏
     */
    private val _isNotFailed = MutableLiveData(true)
    val isNotFailed get() = _isNotFailed as LiveData<Boolean>

    /**
     * Status 状态
     *
     * @constructor Create empty Status
     */
    sealed class Status {
        object Initialized : Status()
        object PersonnelNotFound : Status()
        object PersonnelExist : Status()
        object SendingMessage : Status()
        object WrongVerifyCode : Status()
        object JumpingToHome : Status()
    }

    /**
     * Status 状态
     */
//    val status get() = MutableStateFlow<Status>(Status.Initialized).asStateFlow()
    val _status = MutableStateFlow<Status>(Status.Initialized)


    private var isDealWithSendVerifyCode = false

    var phone: String? = null
    private lateinit var userType: UserType

    private var realUsername:String? = null
    private suspend fun getRealUsername():String? {
        return if (realUsername==null)
            withContext(IO) {
                repository.getUserNameByID(phone!!)
            }
        else realUsername
    }

    private val inputObserver by lazy {
        Observer <String> { checkingLength {
            viewModelScope.launch {
                //正确时结束声明周期
                if(repository.checkVerificationCode(it)) launch(Main) {
                    _status.value = Status.JumpingToHome
                    _isVisible.value = false
                    onCleared()
                } else launch(Main) {
                    _username.value = "验证码错误，请重新输入，点击旁边的刷新按钮即可重新获取验证码。"
                    _status.value = Status.WrongVerifyCode
                    _isShowingProgressBar.value = false
                }
            }
        } }
    }

    /**
     * Init callback 初始化回调
     * 由Context调用
     */
    fun initCallback() {
        _username.value="您好，请登入。"
        _hint.value = "手机号码。"
        _noticeText.value = "输入完成后点击右边的按钮即可获取验证码。"
        _endIconDrawable.value = R.drawable.ic_twotone_textsms_24
        _maxInput.value = 11
        _isShowingProgressBar.value = false
        input.value = ""
        _isVisible.value = true
    }

    /**
     * Checking length 提高代码重用性
     *
     * @param block
     */
    private fun checkingLength(block:()->Unit) {
        if (input.value?.length == maxInputCount.value) {
            _isShowingProgressBar.value = true
            block()
        }else _username.value = "请输入${maxInputCount.value}位数的${hint.value}！"
    }


    /**
     * On click
     * 当点击时判断按钮此时需要处理事件为下一步还是重新发送验证码
     */
    fun onClick() {
        //显示等待
        viewModelScope.launch { delay(500)}
        _username.value = "等待中"
        _isShowingProgressBar.value = true
        if (!isDealWithSendVerifyCode) checkingLength {
            viewModelScope.launch(Main) {
                onNextStep(async(IO){ delay(500);repository.checkPhoneOnLogged(input.value) })
            }
        } else {//发送验证码
            _status.value = Status.SendingMessage
            _username.value = "验证码已发送，请查看。"
            _isShowingProgressBar.value = false
        }
    }


    /**
     * On next step
     * 点击按钮正确时执行
     * @param task 用户类型
     */
    private suspend fun onNextStep(task:Deferred<UserType>) {
        //储存结果
        phone = input.value
        userType =  task.await()
        //判断用户是否存在
        if (userType == UserType.NOT_ARROW) {
            //不存在 结束
            _username.value = "该用户不存在！！"
            _isNotFailed.value = false
            _status.value = Status.PersonnelNotFound
        } else {
            showNext()
        }
        _isShowingProgressBar.value = false
    }

    private fun showNext() {
        viewModelScope.launch {getRealUsername().let {
            launch(Main) {
                _username.value = "欢迎回来，$it。"
            }
        }}
        _maxInput.value = 4
        _hint.value = "验证码"
        _noticeText.value = "输入正确的验证码即可登入。"
        _isShowingProgressBar.value = false
        input.value = ""
        _endIconDrawable.value = R.drawable.ic_twotone_refresh_24
        isDealWithSendVerifyCode = true
        _status.value = Status.PersonnelExist
        input.observeForever(inputObserver)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: called")
        input.removeObserver(inputObserver)
    }
}