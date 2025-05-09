package rocks.poopjournal.vacationdays.domain.service.holidays

import androidx.room.Database
import androidx.room.RoomDatabase
import rocks.poopjournal.vacationdays.data.Holiday

@Database(entities = [Holiday::class], version = 1, exportSchema = false)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao() : HolidayDao
}