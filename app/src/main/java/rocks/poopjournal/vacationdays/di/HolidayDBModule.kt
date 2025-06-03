package rocks.poopjournal.vacationdays.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rocks.poopjournal.vacationdays.domain.service.holidays.HolidayDao
import rocks.poopjournal.vacationdays.domain.service.holidays.HolidayDatabase
import rocks.poopjournal.vacationdays.presentation.ui.utils.HOLIDAY_TABLENAME
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HolidayDBModule {
    @Singleton
    @Provides
    fun provideHolidayDao(database: HolidayDatabase): HolidayDao = database.holidayDao()

    @Singleton
    @Provides
    fun provideVacationDatabase(@ApplicationContext context: Context): HolidayDatabase =
        Room.databaseBuilder(
            context,
            HolidayDatabase::class.java,
            HOLIDAY_TABLENAME
        ).build()
}