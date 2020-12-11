package org.yanzuwu.live.administrator.repositories

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import org.yanzuwu.live.administrator.data.TheDao

/**
 * Repository
 *
 * 暂时用的仓库
 * @constructor Create empty Repository
 */
@Module
@InstallIn(SingletonComponent::class)
object Repository {
    /**
     * Dao
     *
     * 假Dao实例
     */
    val dao by lazy { TheDao() }
    @Provides
    fun provideDao(): TheDao = dao
}
@HiltAndroidApp
class EmptyApplication:Application() {

}