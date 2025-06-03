package rocks.poopjournal.vacationdays.domain.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import rocks.poopjournal.vacationdays.data.Holiday
import rocks.poopjournal.vacationdays.domain.service.holidays.HolidayDao
import javax.inject.Inject

class HolidaysRepository @Inject constructor(
    private val holidayDao: HolidayDao
) {
    fun getYearsHolidays(year: Int) : Flow<List<Holiday>> = holidayDao.getForYear(year.toString()).flowOn(Dispatchers.IO).conflate()
    suspend fun insert(data: Holiday) = holidayDao.insert(data)
    suspend fun insertAll(data: List<Holiday>) = holidayDao.bulkInsert(data)
    suspend fun delete(data: Holiday) = holidayDao.delete(data)
}