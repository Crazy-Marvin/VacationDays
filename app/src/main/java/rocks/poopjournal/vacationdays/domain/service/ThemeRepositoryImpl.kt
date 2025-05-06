package rocks.poopjournal.vacationdays.domain.service

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import rocks.poopjournal.vacationdays.presentation.ui.utils.AppTheme
import rocks.poopjournal.vacationdays.presentation.ui.utils.BooleanPreferenceDelegate
import rocks.poopjournal.vacationdays.presentation.ui.utils.PreferenceConverter
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import rocks.poopjournal.vacationdays.presentation.ui.utils.TypedIntPreferenceDelegate
import javax.inject.Inject

class ThemeSettingImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ThemeSetting {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)

    private val appThemeDelegate =
        TypedIntPreferenceDelegate(preferences, "app_theme", AppTheme.LIGHT, AppThemeConverter)
    override var theme: AppTheme by appThemeDelegate
    override val themeFlow = appThemeDelegate.flow

    // sick days tracking
    private val featureEnabledDelegate =
        BooleanPreferenceDelegate(preferences, "is_feature_enabled", false)
    override var isFeatureEnabled: Boolean by featureEnabledDelegate
    val featureEnabledFlow = featureEnabledDelegate.flow

    // weekend exclusion
    private val isExcludeWeekendsDelegate =
        BooleanPreferenceDelegate(preferences, "is_exclude_weekends", false)
    override var isExcludeWeekendsEnabled: Boolean by isExcludeWeekendsDelegate
    override val isExcludeWeekendsFlow = isExcludeWeekendsDelegate.flow

    private object AppThemeConverter : PreferenceConverter<AppTheme, Int> {
        override fun serialize(value: AppTheme): Int = value.ordinal
        override fun deserialize(value: Int): AppTheme = AppTheme.fromOrdinal(value)
    }
}
