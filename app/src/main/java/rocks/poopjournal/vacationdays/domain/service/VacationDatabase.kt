package rocks.poopjournal.vacationdays.domain.service

import androidx.room.Database
import androidx.room.RoomDatabase
import rocks.poopjournal.vacationdays.data.VacationData

@Database(entities = [VacationData::class], version = 1, exportSchema = false)
abstract class VacationDatabase : RoomDatabase() {
    abstract fun vacationDao() : VacationDao
}