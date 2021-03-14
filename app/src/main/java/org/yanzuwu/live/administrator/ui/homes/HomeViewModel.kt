package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.heizi.kotlinx.android.set

class HomeViewModel : ViewModel(),LifecycleObserver {

    val title get() = _title.asStateFlow()
    private val _title = MutableStateFlow("")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        _title set "title"
    }

}