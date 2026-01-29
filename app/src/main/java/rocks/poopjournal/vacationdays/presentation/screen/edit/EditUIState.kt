package rocks.poopjournal.vacationdays.presentation.screen.edit

import java.time.LocalDate

data class EditUiState(
    val vacationId: Int = 0,
    val name: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val category: VacationCategory = VacationCategory.Vacation,
    val isInitialized: Boolean = false
)

enum class VacationCategory {
    Sick, Vacation
}

