package rocks.poopjournal.vacationdays.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import rocks.poopjournal.vacationdays.domain.repo.HolidaysRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.repo.VacationRepository
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import rocks.poopjournal.vacationdays.presentation.usecase.VacationDataUseCase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Singleton
    @Provides
    fun providesVacationDataUseCase(
        scope: CoroutineScope,
        vacationRepository: VacationRepository,
        holidaysRepository: HolidaysRepository,
        vacationNumberRepository: VacationNumberRepository,
        themeSetting: ThemeSetting
    ): VacationDataUseCase = VacationDataUseCase(
        scope,
        vacationRepository,
        holidaysRepository,
        vacationNumberRepository,
        themeSetting
    )
}