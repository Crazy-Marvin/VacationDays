package rocks.poopjournal.vacationdays.domain.service.holidays

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.ui.utils.HOLIDAY_TABLENAME
import rocks.poopjournal.vacationdays.presentation.ui.utils.THETABLE_TABLENAME

@Dao
interface HolidayDao {
        @Query("SELECT * from $HOLIDAY_TABLENAME")
        fun getAllData(): Flow<List<Holiday>>

        @Query("SELECT * from $HOLIDAY_TABLENAME WHERE id = :id")
        fun getData(id: Int): Flow<Holiday>

        @Query("SELECT * from $HOLIDAY_TABLENAME WHERE year = :year")
        fun getForYear(year: String): Flow<List<Holiday>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(data: Holiday)

        @Update
        suspend fun update(data: Holiday)

        @Delete
        suspend fun delete(data: Holiday)
}