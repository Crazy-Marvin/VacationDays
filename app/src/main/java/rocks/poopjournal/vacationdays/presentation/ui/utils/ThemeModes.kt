package rocks.poopjournal.vacationdays.presentation.ui.utils

import kotlinx.coroutines.flow.StateFlow
import rocks.poopjournal.vacationdays.R

enum class AppTheme(val nameTheme : String){
    LIGHT("Light"),
    DARK( "Dark"),
    FOLLOW_SYSTEM( "Follow System");

    companion object{
        fun fromOrdinal(ordinal : Int) = entries[ordinal]
    }
    fun getStringResId(): Int {
        return when (this) {
            LIGHT -> R.string.light
            DARK -> R.string.dark
            FOLLOW_SYSTEM -> R.string.followsys
        }
    }
}


interface ThemeSetting {
    val themeFlow : StateFlow<AppTheme>
    var theme : AppTheme
    var isFeatureEnabled: Boolean
    var isExcludeWeekendsEnabled: Boolean
    val isExcludeWeekendsFlow: StateFlow<Boolean>
    var isShowWeekDaysHeaderEnabled: Boolean
    val isShowWeekDaysHeaderFlow: StateFlow<Boolean>
    val isVacationNotificationEnabledFlow: StateFlow<Boolean>
    var isVacationNotificationEnabled: Boolean
}