package org.yanzuwu.live.administrator.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.asLiveData():LiveData<T> = this
operator fun <T> MutableLiveData<T>.plus(another:T) {
    this.value = another
}