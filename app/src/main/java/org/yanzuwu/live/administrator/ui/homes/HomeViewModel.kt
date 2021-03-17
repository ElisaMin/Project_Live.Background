package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.heizi.kotlinx.android.set

class HomeViewModel : ViewModel() {

    val title get() = _title.asStateFlow()
    private val _title = MutableStateFlow("")
//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        _title set "正在加载中。。。。。。"
    }
    fun initialize(name:String) {
        _title set "欢迎回来，$name。"
    }

}