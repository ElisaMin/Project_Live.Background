package org.yanzuwu.live.administrator.data

import androidx.lifecycle.ViewModel

class SharedViewModel:ViewModel() {
    var type = TheDao.UserType.NOT_ARROW
}