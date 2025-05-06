package rocks.poopjournal.vacationdays.presentation.ui.utils

interface PreferenceConverter<T, R> {
    fun serialize(value: T): R
    fun deserialize(value: R): T
}
