package rocks.poopjournal.vacationdays.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rocks.poopjournal.vacationdays.domain.service.holidays.DateNagerAtClient
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun providesPublicHolidayService() = DateNagerAtClient.service
}


