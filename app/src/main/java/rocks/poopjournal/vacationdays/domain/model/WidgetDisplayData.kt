package rocks.poopjournal.vacationdays.domain.model

data class WidgetDisplayData(
    val vacationDays: Int = 0,
    val sickDays: Int = 0,
    val remainingVacationDays: Int = 0,
    val daysUntilNextVacation: Int? = null,
    val daysSinceLastVacation: Int? = null,
    val daysSinceLastSickDay: Int? = null,
    val vacationDaysTaken: Int?= null,        // NEW
    val vacationDaysPlanned: Int? = null,
    val workDays: Int? = null
) {
    companion object {
        val Empty = WidgetDisplayData()
    }
}
