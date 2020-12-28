package org.yanzuwu.live.administrator

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.yanzuwu.live.administrator.repositories.UserRepository
import org.yanzuwu.live.administrator.utils.dataclassess.UserType
import javax.inject.Inject


class SharedViewModel @ViewModelInject constructor(
    private val repository: UserRepository,
):ViewModel() {
    var type = MutableStateFlow(UserType.NOT_ARROW)
    var phone:String? = null
        set(value) {
            viewModelScope.launch(IO) {
                type.emit(repository.checkPhoneOnLogged(value))
            }
            field = value
        }
}