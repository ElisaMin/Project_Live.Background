package org.yanzuwu.live.administrator.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.repositories.UserRepository
import javax.inject.Named
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object RepositoryProviders {
    @Singleton @Provides
    fun taskRepository(
        @ApplicationContext context: Context
    ) = TaskRepository(context)

    @Singleton @Provides
    fun userRepository(
        @ApplicationContext context: Context
    ) = UserRepository(context)
    
}