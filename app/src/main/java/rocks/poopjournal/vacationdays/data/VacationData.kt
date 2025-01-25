package rocks.poopjournal.vacationdays.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.poopjournal.vacationdays.presentation.ui.utils.THETABLE_TABLENAME

@Entity(tableName = THETABLE_TABLENAME)
data class VacationData(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val startDate : String,
    val endDate : String? = null,
    val category : String,
)
