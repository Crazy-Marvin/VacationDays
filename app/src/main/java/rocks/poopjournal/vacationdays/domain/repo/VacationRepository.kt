package rocks.poopjournal.vacationdays.domain.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.domain.service.VacationDao
import javax.inject.Inject

class VacationRepository @Inject constructor(
    private val vacationDao: VacationDao
) {
    fun getAllData() : Flow<List<VacationData>> = vacationDao.getAllData().flowOn(Dispatchers.IO).conflate()
    fun getData(id: Int) : Flow<VacationData> = vacationDao.getData(id).flowOn(Dispatchers.IO).conflate()
    suspend fun insertData(data: VacationData) = vacationDao.insert(data)
    suspend fun updateData(data: VacationData) = vacationDao.update(data)
    suspend fun deletedata(data: VacationData) = vacationDao.delete(data)
}