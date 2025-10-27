package rocks.poopjournal.vacationdays.presentation.navigation

const val Home_Screen = "HomeScreen"
const val Add_Screen = "AddScreen"
const val Add_Screen_VacationId = "vacationId"
const val Setting_Screen = "SettingScreen"
const val About_Screen = "AboutScreen"
const val Vacation_Days_Screen = "VacationDaysScreen"

fun addScreenRoute(vacationId: Int? = null): String {
    return if (vacationId != null) {
        "$Add_Screen?$Add_Screen_VacationId=$vacationId"
    } else {
        Add_Screen
    }
}
