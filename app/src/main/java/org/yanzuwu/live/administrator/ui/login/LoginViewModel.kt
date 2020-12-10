package org.yanzuwu.live.administrator.ui.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.yanzuwu.live.administrator.R

class LoginViewModel : ViewModel() {
    /**
     * username （其实是提示文字
     */
    private val _username = MutableLiveData("您好，请登入。")
    val username:LiveData<String> = _username
    /**
     * Hint 提示框内容
     */
    private val _hint = MutableLiveData("输入手机号码。")
    val hint:LiveData<String> =_hint
    /**
     * input 输入内容
     */
    val input = MutableLiveData("")
    /**
     * notice text 提示文字
     */
    private val _noticeText = MutableLiveData("输入完成后点击右边的按钮即可获取验证码。")
    val noticeText:LiveData<String> = _noticeText
    /**
     * End icon drawable
     */
    private val _endIconDrawable = MutableLiveData(R.drawable.ic_twotone_textsms_24)
    val endIconDrawable get() = _endIconDrawable as LiveData<Int>

    private val _maxInput = MutableLiveData(11)
    val maxInputCount get() = _maxInput as LiveData<Int>

    private val _isShowingProgressBar = MutableLiveData(false)
    val isShowingProgressBar get() = _isShowingProgressBar as LiveData<Boolean>


    /**
     * Next
     *
     * 点击下一步时检测文字是否达到11，在没有达到时，提示，达到之后 如果员工表没有则提示 否则
     */
    var next = false
    fun next() {
        input.value?.takeIf { it.length ==maxInputCount.value }?.run {
            _username.value = "等待中"
            _isShowingProgressBar.value = true
            next = true
        } ?: kotlin.run {
            _username.value = "请输入正确的数值！"
            next = false
        }
    }


    fun verificationCallback(username:String) {
        _username.value = "欢迎回来，$username。"
        _maxInput.value = 4
        _hint.value = "验证码"
        _noticeText.value = "输入正确的密码即可登入。"
        _isShowingProgressBar.value = false
        input.value = null
        next = false
    }
    fun notAllowCallback() {
        _username.value = "该用户不存在！！"
        _isShowingProgressBar.value = false
    }

}