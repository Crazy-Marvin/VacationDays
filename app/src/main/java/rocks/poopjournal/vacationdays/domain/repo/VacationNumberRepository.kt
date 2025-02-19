package rocks.poopjournal.vacationdays.domain.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import rocks.poopjournal.vacationdays.data.VacationNumber
import rocks.poopjournal.vacationdays.domain.service.VacationNumberDao
import javax.inject.Inject

class VacationNumberRepository @Inject constructor(private val vacationNumberDao: VacationNumberDao) {

    suspend fun insertVacationNumber(vacationNumber: VacationNumber) {
        vacationNumberDao.insert(vacationNumber)
    }

    suspend fun updateVacationNumber(vacationNumber: VacationNumber) {
        vacationNumberDao.update(vacationNumber)
    }

    suspend fun getVacationNumberById(id: Int): VacationNumber? {
        return vacationNumberDao.getVacationNumberById(id)
    }

    fun getAllVacationNumbers(): kotlinx.coroutines.flow.Flow<List<VacationNumber>> =
        vacationNumberDao.getAllVacationNumbers().flowOn(Dispatchers.IO).conflate()


    suspend fun deleteVacationNumber(vacationNumber: VacationNumber) {
        vacationNumberDao.deleteVacationNumber(vacationNumber)
    }

    suspend fun getLatestVacationNumber(): VacationNumber? {
        return vacationNumberDao.getLatestVacationNumber()
    }

    suspend fun getVacationNumberForYear(year: String): Int {
        return vacationNumberDao.getVacationNumber(year)?.numberOfVacation ?: 0
    }
}