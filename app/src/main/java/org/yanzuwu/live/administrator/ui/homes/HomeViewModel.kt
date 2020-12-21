package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.*
import org.yanzuwu.live.administrator.utils.asLiveData
import org.yanzuwu.live.administrator.utils.plus

class HomeViewModel : ViewModel(),LifecycleObserver {

    private val _title = MutableLiveData("")

    val title get() = _title.asLiveData()
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initialize() {
        _title + "title"
    }

}