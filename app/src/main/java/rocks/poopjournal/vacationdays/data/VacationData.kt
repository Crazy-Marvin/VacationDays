package rocks.poopjournal.vacationdays.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.poopjournal.vacationdays.presentation.ui.utils.THETABLE_TABLENAME
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = THETABLE_TABLENAME)
data class VacationData(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val startDate : String,
    val endDate : String? = null,
    val category : String = "Vacation",
) {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("d/MM/yyyy") // Match the saved format
    }

    val parsedDates by lazy {
        val start = LocalDate.parse(startDate, formatter)
        val end = endDate?.let { LocalDate.parse(it, formatter) }
        start to end
    }
}
