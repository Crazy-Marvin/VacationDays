package rocks.poopjournal.vacationdays.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rocks.poopjournal.vacationdays.domain.service.ThemeSettingImpl
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingModule {
    @Binds
    @Singleton
    abstract fun bindThemeSetting(
        themeSettingImpl: ThemeSettingImpl
    ) : ThemeSetting
}