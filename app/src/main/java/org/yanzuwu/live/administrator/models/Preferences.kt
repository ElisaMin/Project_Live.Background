package org.yanzuwu.live.administrator.models

import me.heizi.kotlinx.android.preferences.PreferencesManager
import org.yanzuwu.live.administrator.Main


class Preferences: PreferencesManager.Global() {
    var phone:String? by Named(Main.KEY_PHONE)
}