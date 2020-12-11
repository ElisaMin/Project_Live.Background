package org.yanzuwu.live.administrator.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.yanzuwu.live.administrator.Main.Companion.mainActivity
import org.yanzuwu.live.administrator.R
import org.yanzuwu.live.administrator.data.TheDao

class LoginViewModel : ViewModel() {
    /**
     * username （其实是提示文字
     */
    private val _username = MutableLiveData<String>()
    val username:LiveData<String> = _username
    /**
     * Hint 提示框内容
     */
    private val _hint = MutableLiveData<String>()
    val hint:LiveData<String> =_hint
    /**
     * input 输入内容
     */
    val input = MutableLiveData<String>()
    /**
     * notice text 提示文字
     */
    private val _noticeText = MutableLiveData<String>()
    val noticeText:LiveData<String> = _noticeText
    /**
     * End icon drawable
     */
    private val _endIconDrawable = MutableLiveData<Int>()
    val endIconDrawable get() = _endIconDrawable as LiveData<Int>

    private val _maxInput = MutableLiveData<Int>()
    val maxInputCount get() = _maxInput as LiveData<Int>

    private val _isShowingProgressBar = MutableLiveData<Boolean>()
    val isShowingProgressBar get() = _isShowingProgressBar as LiveData<Boolean>

    private val _isVisible = MutableLiveData(false)
    val isVisible get() = _isVisible as LiveData<Boolean>

    /**
     * Next
     *
     * 点击下一步时检测文字是否达到11，在没有达到时，提示，达到之后 如果员工表没有则提示 否则
     */
    var isSendAgain = false
    var isPass = false
    var isUserExist:Boolean? = null

    fun next() {
        if (isUserExist  == true){
            isSendAgain = true
            onDealing()
        } else {
            input.value?.takeIf { it.length == maxInputCount.value }?.run {
                _username.value = "等待中"
                isPass = true
                _isShowingProgressBar.value = true
            } ?: kotlin.run {
                _username.value = "请输入正确的数值！"
                isPass = false
            }
        }
    }

    fun initCallback() {
        _username.value="您好，请登入。"
        _hint.value = "输入手机号码。"
        _noticeText.value = "输入完成后点击右边的按钮即可获取验证码。"
        _endIconDrawable.value = R.drawable.ic_twotone_textsms_24
        _maxInput.value = 11
        _isShowingProgressBar.value = false
        isPass = false
        input.value = null
        _isVisible.value = true
    }
    fun onDealing() {
        _username.value = "等待中"
        _isShowingProgressBar.value = true
    }
    private var realUsername:String? = null
    fun verificationCallback(username:String?=null) {
        username?.let { realUsername = it }
        _username.value =  realUsername?.let { "欢迎回来，$it。" } ?:_username.value
        _maxInput.value = 4
        _hint.value = "验证码"
        _noticeText.value = "输入正确的验证码即可登入。"
        _isShowingProgressBar.value = false
        input.value = null
        _endIconDrawable.value = R.drawable.ic_twotone_refresh_24
        isPass = false
    }
    fun badVerifyCodeCallback() {
        _username.value = "验证码错误，点击旁边的刷新按钮即可重新登入。"
        _isShowingProgressBar.value = false
    }
    fun notAllowCallback() {
        _username.value = "该用户不存在！！"
        _isShowingProgressBar.value = false
    }

}