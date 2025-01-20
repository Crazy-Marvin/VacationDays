package rocks.poopjournal.vacationdays.domain.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.ui.utils.THETABLE_TABLENAME

@Dao
interface VacationDao {
        @Query("SELECT * from $THETABLE_TABLENAME")
        fun getAllData(): Flow<List<VacationData>>

        @Query("SELECT * from $THETABLE_TABLENAME WHERE id = :id")
        fun getData(id: Int): Flow<VacationData>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(data: VacationData)

        @Update
        suspend fun update(data: VacationData)

        @Delete
        suspend fun delete(data: VacationData)
}