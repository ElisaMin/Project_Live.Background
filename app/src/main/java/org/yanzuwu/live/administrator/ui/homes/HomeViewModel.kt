package org.yanzuwu.live.administrator.ui.homes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.yanzuwu.live.administrator.utils.asLiveData
import org.yanzuwu.live.administrator.utils.plus

class HomeViewModel : ViewModel() {

    private val _title = MutableLiveData("")

    val title get() = _title.asLiveData()

    fun initialize() {
        _title + "title"

    }

}