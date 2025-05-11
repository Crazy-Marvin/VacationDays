package rocks.poopjournal.vacationdays.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesScopesModule {

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
}