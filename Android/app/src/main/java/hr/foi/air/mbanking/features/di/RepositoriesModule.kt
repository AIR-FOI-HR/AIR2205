package hr.foi.air.mbanking.features.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.foi.air.mbanking.features.data.repositories.RemoteNotificationRepository
import hr.foi.air.mbanking.features.data.repositories.RemoteUserAccountRepository
import hr.foi.air.mbanking.features.domain.repositories.NotificationRepository
import hr.foi.air.mbanking.features.domain.repositories.UserAccountRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideUserAccountRepository(): UserAccountRepository = RemoteUserAccountRepository()

    @Singleton
    @Provides
    fun provideNotificationRepository(): NotificationRepository = RemoteNotificationRepository()

}