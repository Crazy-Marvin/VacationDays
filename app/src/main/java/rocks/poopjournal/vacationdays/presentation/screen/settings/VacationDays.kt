package rocks.poopjournal.vacationdays.presentation.screen.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.VacationNumber
import rocks.poopjournal.vacationdays.presentation.ui.theme.darkPrimary
import rocks.poopjournal.vacationdays.presentation.ui.theme.lightGray

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VacationDays(
    viewModel: SettingViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val vacationList by viewModel.vacationNumber.collectAsState()
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onClose = { navHostController.popBackStack() })
        Column {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(vacationList) { vacation ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = vacation.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        VacationDaysInput(
                            vacation = vacation,
                            onVacationUpdated = { newVacationNumber ->
                                viewModel.updateVacationNumber(vacation.id, newVacationNumber)
                            }
                        )
                    }
                }
            }

                Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {
                        viewModel.insertNumberOfVacation()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.surface
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    text = "Add ${viewModel.currentYear.value}",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.W500
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { onClose() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = stringResource(id = R.string.vacation_days),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VacationDaysInput(vacation: VacationNumber, onVacationUpdated: (Int) -> Unit) {
    var inputValue by remember { mutableStateOf(vacation.numberOfVacation.toString()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val keyboardActions = KeyboardActions(
        onDone = {
            keyboardController?.hide()
        }
    )

    BasicTextField(
        value = inputValue,
        onValueChange = { input ->
            inputValue = input
        },
        modifier = Modifier
            .width(52.dp)
            .height(52.dp),
        interactionSource = remember { MutableInteractionSource() },
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)
    ) { innerTextField ->
        TextFieldDefaults.TextFieldDecorationBox(
            value = inputValue,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                top = 0.dp,
                bottom = 0.dp
            ),
        )
    }

    LaunchedEffect(inputValue) {
        val parsedValue = inputValue.toIntOrNull()
        if (parsedValue != null) {
            onVacationUpdated(parsedValue)
        }
    }
}