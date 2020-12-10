package org.yanzuwu.live.administrator.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.yanzuwu.live.administrator.R

class LoginViewModel : ViewModel() {
    /**
     * username 用户名称
     */
    private val _username = MutableLiveData("您好，请登入。。")
    val username:LiveData<String> = _username
    /**
     * Hint 提示框内容
     */
    private val _hint = MutableLiveData("输入后即可。")
    val hint:LiveData<String> =_hint
    /**
     * input 输入内容
     */
    val input = MutableLiveData("")
    /**
     * notice text 提示文字
     */
    private val _noticeText = MutableLiveData("输入后即可。")
    val noticeText:LiveData<String> = _noticeText
    /**
     * End icon drawable
     */
    private  val _endIconDrawable = MutableLiveData(R.drawable.ic_twotone_textsms_24)
    val endIconDrawable get() = _endIconDrawable as LiveData<Int>

    /**
     * Next
     *
     * 点击下一步时检测文字是否达到11，在没有达到时，提示，达到之后 如果员工表没有则提示 否则
     */
    fun next() {

        input.value?.takeIf { it.length ==11 }?.run {

        } ?: kotlin.run {
            _username.value = "爬！"
        }
    }
}