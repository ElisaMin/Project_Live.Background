package org.yanzuwu.live.administrator.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.yanzuwu.live.administrator.models.Preferences
import org.yanzuwu.live.administrator.repositories.FinanceRepository
import org.yanzuwu.live.administrator.repositories.TaskRepository
import org.yanzuwu.live.administrator.repositories.UserRepository
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object RepositoryProviders {
    @Singleton @Provides
    fun taskRepository(
        @ApplicationContext context: Context
    ) = TaskRepository(context)

    @Singleton @Provides
    fun userRepository(
        @ApplicationContext context: Context,
        preferences: Preferences,
    ) = UserRepository(context,preferences)
    @Singleton @Provides
    fun financeRepository()= FinanceRepository()
}
@Module @InstallIn(SingletonComponent::class)
object Others {

    @Singleton @Provides
    fun preference() = Preferences()
}