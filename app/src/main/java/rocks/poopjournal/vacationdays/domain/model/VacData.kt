package rocks.poopjournal.vacationdays.domain.model

import rocks.poopjournal.vacationdays.data.VacationData

sealed class VacData {
    data object Empty : VacData()

    data class Success(
        val vacations: List<VacationData>,
        val vacationDays: Int,
        val sickDays: Int,
        val vacationsNumber: Int,
    ) : VacData()
}