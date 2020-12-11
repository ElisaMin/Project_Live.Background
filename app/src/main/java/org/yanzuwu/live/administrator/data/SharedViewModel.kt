package org.yanzuwu.live.administrator.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.repositories.Repository.dao



class SharedViewModel:ViewModel() {
    var type = MutableStateFlow(TheDao.UserType.NOT_ARROW)
    var phone:String? = null
        set(value) {
            viewModelScope.launch(IO) {
                type.emit(dao.checkPhoneOnLogged(value))
            }
            field = value
        }
}