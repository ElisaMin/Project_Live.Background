package org.yanzuwu.live.administrator.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {
    private val sdfMain by lazy {
        SimpleDateFormat("dd日HH:mm", Locale.CHINA)
    }
    fun Long.toShortFormat(): String = sdfMain.format(Date(this))
}