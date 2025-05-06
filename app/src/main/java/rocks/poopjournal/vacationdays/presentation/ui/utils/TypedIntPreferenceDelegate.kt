package rocks.poopjournal.vacationdays.presentation.ui.utils

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty

class TypedIntPreferenceDelegate<T>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defaultValue: T,
    private val converter: PreferenceConverter<T, Int>
): ReadWriteProperty<Any, T> {
    private val _stateFlow = MutableStateFlow(getValueFromPrefs())
    val flow: StateFlow<T> = _stateFlow

    private fun getValueFromPrefs(): T {
        val stored = prefs.getInt(key, -1)
        return if (stored != -1) converter.deserialize(stored) else defaultValue
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return _stateFlow.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        prefs.edit { putInt(key, converter.serialize(value)) }
        _stateFlow.value = value
    }
}
