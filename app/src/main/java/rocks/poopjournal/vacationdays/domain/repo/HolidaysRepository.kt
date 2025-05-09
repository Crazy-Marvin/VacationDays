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
    fun getAllData() : Flow<List<Holiday>> = holidayDao.getAllData().flowOn(Dispatchers.IO).conflate()
    fun getYearData(year: Int) : Flow<List<Holiday>> = holidayDao.getForYear(year.toString()).flowOn(Dispatchers.IO).conflate()
    fun getData(id: Int) : Flow<Holiday> = holidayDao.getData(id).flowOn(Dispatchers.IO).conflate()
    suspend fun insertData(data: Holiday) = holidayDao.insert(data)
    suspend fun updateData(data: Holiday) = holidayDao.update(data)
    suspend fun deleteData(data: Holiday) = holidayDao.delete(data)
}