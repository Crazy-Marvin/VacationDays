package rocks.poopjournal.vacationdays.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.poopjournal.vacationdays.presentation.ui.utils.THEOTHER_TABLENAME

@Entity(tableName = THEOTHER_TABLENAME)
data class VacationNumber(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val numberOfVacation : Int,
    val name : String,
    val currentYear : String,
)
