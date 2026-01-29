package rocks.poopjournal.vacationdays.presentation.screen.edit

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.domain.model.VacData
import rocks.poopjournal.vacationdays.presentation.component.CalenderView
import rocks.poopjournal.vacationdays.presentation.screen.add.VacationTopBar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditScreen(
    vacationId: Int,
    viewModel: EditViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    val emptyVacation = stringResource(R.string.empty_vacation)
    val emptyDate = stringResource(R.string.empty_date)
    val isShowWeekDaysHeader by viewModel.themeSetting.isShowWeekDaysHeaderFlow.collectAsStateWithLifecycle()
    val data by viewModel.vacationsUseCase.vacationsFlow.collectAsStateWithLifecycle(VacData.Empty)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vacationId) {
        viewModel.loadVacation(vacationId)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        VacationTopBar(
            selectedTab = if (uiState.category == VacationCategory.Sick) 0 else 1,
            onTabSelected = {
                viewModel.onCategoryChange(
                    if (it == 0) VacationCategory.Sick
                    else VacationCategory.Vacation
                )
            },
            vacationName = uiState.name,
            onNameChange = viewModel::onNameChange,
            onCheckClick = {
                if (uiState.name.isEmpty()) {
                    Toast.makeText(context, emptyVacation, Toast.LENGTH_SHORT).show()
                } else if (uiState.startDate == null) {
                    Toast.makeText(context, emptyDate, Toast.LENGTH_SHORT).show()
                } else {
                   viewModel.updateVacation()
                   navHostController.popBackStack()
                }
            },
            onCloseClick = { navHostController.popBackStack() },
            isSickEnabled = viewModel.themeSetting.isFeatureEnabled,
            isExceedLimit = false
        )

        CalenderView(
            holidays = when (val _data = data) {
                is VacData.Empty -> emptyList()
                is VacData.Success -> _data.vacations
            },
            isRangeSelection = true,
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            focusOnDate = uiState.startDate ?: LocalDate.now(),
            dateSelected = viewModel::onDateSelected,
            showWeekDaysHeader = isShowWeekDaysHeader,
        )
    }
}