package rocks.poopjournal.vacationdays.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rocks.poopjournal.vacationdays.domain.service.DatabaseBackupManager
import rocks.poopjournal.vacationdays.domain.service.VacationDao
import rocks.poopjournal.vacationdays.domain.service.VacationDatabase
import rocks.poopjournal.vacationdays.domain.service.VacationNumberDao
import rocks.poopjournal.vacationdays.presentation.ui.utils.THEDATABASE_DATABASE_NAME
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideVacationDao(vacationDatabase: VacationDatabase): VacationDao =
        vacationDatabase.vacationDao()

    @Singleton
    @Provides
    fun provideVacationNumberDao(vacationDatabase: VacationDatabase) : VacationNumberDao =
        vacationDatabase.vacationNumberDao()

    @Singleton
    @Provides
    fun provideVacationDatabase(@ApplicationContext context: Context): VacationDatabase =
        Room.databaseBuilder(
            context,
            VacationDatabase::class.java,
            THEDATABASE_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideDatabaseBackup(
        @ApplicationContext context: Context,
        vacationDatabase: VacationDatabase
    ): DatabaseBackupManager = DatabaseBackupManager(context, vacationDatabase)
}