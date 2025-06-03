package rocks.poopjournal.vacationdays.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.poopjournal.vacationdays.presentation.ui.utils.HOLIDAY_TABLENAME
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = HOLIDAY_TABLENAME)
data class Holiday(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val year: String,
    val date: String,
    val name: String
) {

    val localDate: LocalDate by lazy { LocalDate.parse(date, formatter) }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd"
        private val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    }
}
