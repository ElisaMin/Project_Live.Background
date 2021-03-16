package org.yanzuwu.live.administrator.ui.login


import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import me.heizi.kotlinx.android.set
import org.yanzuwu.live.administrator.Main.Companion.TAG
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.models.UserType
import org.yanzuwu.live.administrator.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    /** 标题 */
    val title get() = _title.asStateFlow()
    /** 输入框内显示的文字 */
    val hint get() =  _hint
    /** 输入内容 */
    val input = MutableStateFlow("")
    /** 解释当前输入的是啥 */
    val noticeText get() = _noticeText.asStateFlow()
    /** End icon drawable 图标 */
    val endIconDrawable get() = _endIconDrawable.asStateFlow()
    /** 限制输入数*/
    val maxInputCount get() = _maxInput.asStateFlow()
    /** 是否展示ProcessBar */
    val isShowingProgressBar get() = _isShowingProgressBar.asStateFlow()
    /** 除了process bar外的元素是否可见 */
    val isVisible get() = _isVisible.asStateFlow()
    /** 除了文字啥都隐藏 */
    val isNotFailed get() = _isNotFailed.asStateFlow()
    val state get() =  _status.shareIn(viewModelScope, SharingStarted.Eagerly)
    //impl
    private val _title = MutableStateFlow("")
    private val _hint = MutableStateFlow("")
    private val _noticeText = MutableStateFlow("")
    private val _endIconDrawable = MutableStateFlow(R.drawable.ic_twotone_textsms_24)
    private val _maxInput = MutableStateFlow(0)
    private val _isShowingProgressBar = MutableStateFlow(false)
    private val _isVisible = MutableStateFlow(false)
    private val _isNotFailed = MutableStateFlow(true)
    private val _status = MutableStateFlow<Status>(Status.Default)

    private var isDealWithSendVerifyCode = false

    var phone: String? = null
    private lateinit var userType: UserType
    private var username:String? = null


    fun start() {
        startCollecting()
        viewModelScope.launch() {
            _status set Status.Initialized
        }
    }
    private fun startCollecting() = viewModelScope.launch(Default) {
        state.collectLatest {
            Log.i(TAG, "start: changed")
            whileStatusIsUpdated(it)
        }
    }

    /**
     * On click
     * 当点击时判断按钮此时需要处理事件为下一步还是重新发送验证码
     */
    fun onClick() {
        updateUi(title = "等待中",progressing = true)
        if (!isDealWithSendVerifyCode) checkingLength {
            _status set Status.CheckPhone
        } else {//发送验证码
            _status set  Status.Resend
        }
    }

    /**
     * 更新UI
     *
     * @param title
     * @param hint
     * @param noticeText
     * @param endIcon
     * @param maxInput
     * @param progressing
     * @param isVisible
     * @param isNotFailed
     */
    private fun updateUi(
        title:String?=null,
        hint:String?=null,
        noticeText:String?=null,
        @DrawableRes endIcon:Int?=null,
        maxInput:Int?=null,
        progressing:Boolean?=null,
        isVisible:Boolean?=null,
        isNotFailed:Boolean? = null
    ) {
        title?.let(_title::set)
        hint?.let(_hint::set)
        noticeText?.let(_noticeText::set)
        endIcon?.let(_endIconDrawable::set)
        maxInput?.let(_maxInput::set)
        progressing?.let(_isShowingProgressBar::set)
        isVisible?.let(_isVisible::set)
        isNotFailed?.let(_isNotFailed::set)
    }

    var isShowingLengthError = false

    /**
     * Checking length 提高代码重用性
     *
     * @param block
     */
    private inline fun checkingLength( crossinline block:()->Unit) {
        if (input.value.length == maxInputCount.value) {
            updateUi(progressing = true)
            block()
        }else if (isShowingLengthError) updateUi(title = "请输入${maxInputCount.value}位数的${hint.value}！")
    }


    /**
     * 监听输入的长度是否符合条件 在符合条件时触发[]
     *
     */
    private fun listenInputOverLength() = viewModelScope.launch(Default) {
        input.collectLatest { checkingLength { _status set Status.CheckCode } }
    }

    /**
     * 当状态更新为[state]时
     *
     * @param state
     */
    private fun whileStatusIsUpdated(state:Status) = viewModelScope.launch(IO) { when(state) {
        is Status.Initialized -> {
            updateUi(
                title = "您好，请登入。",
                hint = "手机号码。",
                noticeText = "输入完成后点击右边的按钮即可获取验证码。",
                endIcon = R.drawable.ic_twotone_textsms_24,
                maxInput = 11,
                progressing = false,
                isVisible = true,
            )
        }
        is Status.CheckPhone -> {
            updateUi(title = "正在检查是否为本公司员工",progressing =  true, )
            phone = input.value
            val userType = repository.checkPhoneOnLogged(phone)
            if (userType == UserType.NOT_ARROW)  {
                Status.PersonnelNotFound
                return@launch
            }
            username = repository.getUserNameByID(phone!!)
            _status set Status.PersonnelExist(userType,username!!)
        }
        is Status.PersonnelExist ->  {
            input set ""
            listenInputOverLength()
            updateUi(
                maxInput = 4,
                hint = "验证码",
                progressing = false,
                endIcon = R.drawable.ic_twotone_refresh_24,
                title = "欢迎回来，$username。"
            )
            userType = state.type
            username = state.name
            isDealWithSendVerifyCode = true
            _status set Status.SendingMessage(true,repository.code.toString())
        }
        is Status.PersonnelNotFound -> {
            updateUi(title = "该用户不存在！！",isNotFailed = false,progressing = false)
        }
        is Status.SendingMessage -> {
            val it = if(state.isFirst) {
                "欢迎回来，$username，验证码已发送，请查看。"
            } else "验证码重新已发送，请查看。"
            updateUi(title =it, progressing = false)
        }
        is Status.Resend -> {
            updateUi(title = "正在重新发送",progressing = true)
            _status set Status.SendingMessage(false,repository.code.toString())
        }
        is Status.CheckCode -> {
            _status set if(repository.checkVerificationCode(input.value)) {
                updateUi(title = "完成 正在跳转到主页",isNotFailed =false,isVisible = false,progressing = true)
                _isVisible.value = false
                Status.JumpingToHome
            } else Status.WrongVerifyCode
        }
        Status.WrongVerifyCode -> {
            updateUi(title =  "验证码错误，请重新输入，点击旁边的刷新按钮即可重新获取验证码。",progressing = false)
        }
    }}

    /**
     * Status 状态
     * init -> checkPhone -> personExist -> sendMessage -> check -> done
     *      -> Failed />                    -> Resend />
     *                       ->NotExist X
     *
     * @constructor Create empty Status
     */
    sealed class Status {
        object Default:Status()
        object Initialized : Status()
        object CheckPhone : Status()
        object PersonnelNotFound : Status()
        class PersonnelExist(val type: UserType,val name:String) : Status()
        object Resend:Status()
        object CheckCode : Status()
        class SendingMessage(val isFirst:Boolean,val code:String) : Status()
        object WrongVerifyCode : Status()
        object JumpingToHome : Status()
    }
}
