package rocks.poopjournal.vacationdays.domain.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import rocks.poopjournal.vacationdays.data.VacationNumber
import rocks.poopjournal.vacationdays.presentation.ui.utils.THEOTHER_TABLENAME

@Dao
interface VacationNumberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vacationNumber: VacationNumber)

    @Update
    suspend fun update(vacationNumber: VacationNumber)

    @Query("SELECT * FROM $THEOTHER_TABLENAME WHERE id = :id")
    suspend fun getVacationNumberById(id: Int): VacationNumber?

    @Query("SELECT * FROM $THEOTHER_TABLENAME")
    fun getAllVacationNumbers(): Flow<List<VacationNumber>>

    @Query("SELECT * FROM $THEOTHER_TABLENAME WHERE name = :year LIMIT 1")
    suspend fun getVacationNumber(year: String): VacationNumber?

    @Query("SELECT * FROM $THEOTHER_TABLENAME ORDER BY id DESC LIMIT 1")
    suspend fun getLatestVacationNumber(): VacationNumber?

    @Delete
    suspend fun deleteVacationNumber(vacationNumber: VacationNumber)
}