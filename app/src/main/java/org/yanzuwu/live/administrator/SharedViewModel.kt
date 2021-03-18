package org.yanzuwu.live.administrator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import me.heizi.kotlinx.android.default
import org.yanzuwu.live.administrator.models.UserType
import org.yanzuwu.live.administrator.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: UserRepository,
):ViewModel() {
    val type get() = _type.shareIn(viewModelScope, SharingStarted.Eagerly)
    var name = ""
    var phone:String? = null
        set(value) {
            default {
                _type.emit(repository.checkPhoneOnLogged(value))
                if (value!=null) name = repository.getUserNameByID(value)
            }
            field = value
        }
    private val _type = MutableStateFlow<UserType?>(null)
    fun checkPreferencePhone(){
        phone = repository.preferences.phone
    }
}