package rocks.poopjournal.vacationdays.domain.service.holidays

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.presentation.ui.utils.HOLIDAY_TABLENAME

@Dao
interface HolidayDao {
        @Query("SELECT * from $HOLIDAY_TABLENAME WHERE year = :year")
        fun getForYear(year: String): Flow<List<Holiday>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(data: Holiday)

        @Insert
        suspend fun bulkInsert(holidays: List<Holiday>)

        @Delete
        suspend fun delete(data: Holiday)
}