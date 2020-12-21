package org.yanzuwu.live.administrator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.utils.dataclassess.UserType
import org.yanzuwu.live.administrator.repositories.UserRepository.checkPhoneOnLogged


class SharedViewModel:ViewModel() {
    var type = MutableStateFlow(UserType.NOT_ARROW)
    var phone:String? = null
        set(value) {
            viewModelScope.launch(IO) {
                type.emit(checkPhoneOnLogged(value))
            }
            field = value
        }
}