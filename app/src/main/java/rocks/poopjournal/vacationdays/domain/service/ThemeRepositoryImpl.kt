package rocks.poopjournal.vacationdays.domain.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import rocks.poopjournal.vacationdays.presentation.ui.utils.AppTheme
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ThemeSettingImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ThemeSetting {

    // Existing theme-related code
    override var theme: AppTheme by AppThemePreferenceDelegate("app_theme", AppTheme.LIGHT)
    override val themeFlow: MutableStateFlow<AppTheme>
    private val preferences: SharedPreferences =
        context.getSharedPreferences("sample_theme", Context.MODE_PRIVATE)

    // New property for the boolean (e.g., feature enable/disable flag)
    override var isFeatureEnabled: Boolean by BooleanPreferenceDelegate("is_feature_enabled", false)
    val featureEnabledFlow: MutableStateFlow<Boolean>

    init {
        themeFlow = MutableStateFlow(theme)
        featureEnabledFlow = MutableStateFlow(isFeatureEnabled)
    }

    // Delegate for saving and retrieving the theme
    inner class AppThemePreferenceDelegate(
        private val name: String,
        private val default: AppTheme,
    ) : ReadWriteProperty<Any, AppTheme> {
        override fun getValue(thisRef: Any, property: KProperty<*>): AppTheme =
            AppTheme.fromOrdinal(preferences.getInt(name, default.ordinal))

        override fun setValue(thisRef: Any, property: KProperty<*>, value: AppTheme) {
            themeFlow.value = value
            preferences.edit {
                putInt(name, value.ordinal)
            }
        }
    }

    // Delegate for saving and retrieving the boolean feature flag
    inner class BooleanPreferenceDelegate(
        private val name: String,
        private val default: Boolean,
    ) : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
            preferences.getBoolean(name, default)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            featureEnabledFlow.value = value
            preferences.edit {
                putBoolean(name, value)
            }
        }
    }
}
