package rocks.poopjournal.vacationdays.presentation.ui.utils

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

class BooleanPreferenceDelegate(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean,
): ReadWriteProperty<Any, Boolean> {
    private val _stateFlow = MutableStateFlow(getValueFromPrefs())
    val flow: StateFlow<Boolean> = _stateFlow

    private fun getValueFromPrefs(): Boolean = prefs.getBoolean(key, defaultValue)

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return _stateFlow.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
        _stateFlow.value = value
    }
}
