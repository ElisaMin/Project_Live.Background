package org.yanzuwu.live.administrator

import dagger.hilt.android.HiltAndroidApp
import me.heizi.kotlinx.android.preferences.PreferencesManager.Global.Companion.registerPreferencesMapper
import android.app.Application as app

@HiltAndroidApp
class Application :app() {
    override fun onCreate() {
        super.onCreate()
        registerPreferencesMapper("MAIN")
    }
}