package rocks.poopjournal.vacationdays.presentation.screen.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.data.VacationNumber
import rocks.poopjournal.vacationdays.domain.repo.VacationNumberRepository
import rocks.poopjournal.vacationdays.domain.service.DatabaseBackupManager
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val databaseBackupManager: DatabaseBackupManager,
    private val vacationNumberRepository: VacationNumberRepository
) : ViewModel() {

    @Inject
    lateinit var themeSetting: ThemeSetting

    private val _vacationNumber = MutableStateFlow<List<VacationNumber>>(emptyList())
    val vacationNumber: StateFlow<List<VacationNumber>> = _vacationNumber

    @RequiresApi(Build.VERSION_CODES.O)
    var currentYear = mutableStateOf(LocalDate.now().year.toString())
        private set


    init {
        fetchVacation()
        initializeCurrentYear()
    }

    fun backupDatabase(message : String) {
        viewModelScope.launch {
           databaseBackupManager.backupDatabase(message)
        }
    }



    fun restoreDatabase() {
        viewModelScope.launch {
            databaseBackupManager.restoreDatabase()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNumberOfVacation() {
        viewModelScope.launch {
            val nextYear = (currentYear.value.toInt() + 1).toString()
            val newEntry = VacationNumber(
                name = currentYear.value,
                numberOfVacation = 0,
                currentYear = nextYear
            )
            vacationNumberRepository.insertVacationNumber(newEntry)
            currentYear.value = nextYear
            fetchVacation()
        }
    }


    fun updateVacationNumber(id: Int, newNumber: Int) {
        viewModelScope.launch {
            val existingEntry = vacationNumberRepository.getVacationNumberById(id)
            if (existingEntry != null) {
                vacationNumberRepository.updateVacationNumber(existingEntry.copy(numberOfVacation = newNumber))
                fetchVacation()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeCurrentYear() {
        viewModelScope.launch {
            val latestEntry = vacationNumberRepository.getLatestVacationNumber()
            currentYear.value = latestEntry?.currentYear ?: LocalDate.now().year.toString()
        }
    }

    private fun fetchVacation() {
        viewModelScope.launch {
           vacationNumberRepository.getAllVacationNumbers().collect { data ->
                _vacationNumber.value = data
            }
        }
    }
}