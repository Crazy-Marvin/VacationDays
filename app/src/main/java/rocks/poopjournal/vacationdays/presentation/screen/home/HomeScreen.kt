package rocks.poopjournal.vacationdays.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.component.CalenderView
import rocks.poopjournal.vacationdays.presentation.component.CustomTab
import rocks.poopjournal.vacationdays.presentation.navigation.About_Screen
import rocks.poopjournal.vacationdays.presentation.navigation.Add_Screen
import rocks.poopjournal.vacationdays.presentation.navigation.Setting_Screen
import rocks.poopjournal.vacationdays.presentation.ui.theme.gray
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navHostController: NavHostController) {
    val vacation by viewModel.holidays.collectAsState()
    val (selectedTab, setSelectedTab) = remember { mutableIntStateOf(0) }
    val vacationDays by viewModel.vacationDays.collectAsState()
    val sickDays by viewModel.sickDays.collectAsState()
    val totalHolidays by viewModel.totalHolidays.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Add_Screen) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TopBar(
                    selectedTab = selectedTab,
                    onTabSelected = setSelectedTab,
                    navHostController = navHostController,
                    total = totalHolidays,
                    vacationDays = vacationDays,
                    sickDays = sickDays,
                    isSickEnabled = viewModel.themeSetting.isFeatureEnabled
                )
                val deleteMessage = stringResource(R.string.delete_vacation)
                val actionLabel = stringResource(R.string.undo)
                when (selectedTab) {
                    0 -> TimelineView(
                        vacationList = vacation,
                        onDelete = {
                            coroutineScope.launch {
                                viewModel.deleteVacation(it)
                                val result = snackbarHostState.showSnackbar(
                                    message = deleteMessage,
                                    actionLabel = actionLabel,
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.restoreVacation(it)
                                }
                            }
                        })

                    1 -> Calendar(holiday = vacation)
                }
            }
        })


}


@Composable
private fun TopBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navHostController: NavHostController,
    sickDays: Int,
    vacationDays: Int,
    total: Int,
    isSickEnabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(0.3f))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.umbrella),
                        contentDescription = "Vacation",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = vacationDays.toString(),
                        color = MaterialTheme.colorScheme.onBackground
                    )


                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.briefcase),
                        contentDescription = "Total Days",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = total.toString(), color = MaterialTheme.colorScheme.onBackground)

                    Spacer(modifier = Modifier.width(8.dp))
                    if (isSickEnabled) {
                        Icon(
                            painter = painterResource(id = R.drawable.tempreature),
                            contentDescription = "Sick Days",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = sickDays.toString(),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // MoreVert Icon on the right
            Box(
                modifier = Modifier.padding(end = 16.dp)
            ) {
                IconButton(
                    onClick = { expanded = true },
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_problem),
                                    contentDescription = "About",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(id = R.string.about),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        },
                        onClick = {
                            expanded = false
                            navHostController.navigate(About_Screen)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_settings),
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(id = R.string.settings),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        },
                        onClick = {
                            expanded = false
                            navHostController.navigate(Setting_Screen)
                        }
                    )
                }
            }
        }

        // Custom Tab below
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTab(
                items = listOf("Timeline", "Calendar"),
                selectedItemIndex = selectedTab,
                onClick = { index -> onTabSelected(index) },
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimelineView(vacationList: List<VacationData>, onDelete: (VacationData) -> Unit) {
    val groupedVacations = vacationList.groupBy { vacation ->
        YearMonth.parse(vacation.startDate, DateTimeFormatter.ofPattern("d/MM/yyyy"))
    }
    val today = LocalDate.now()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (vacationList.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.empty_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        } else {
            LazyColumn {
                groupedVacations.forEach { (yearMonth, vacations) ->
                    item {
                        Text(
                            text = yearMonth.format(DateTimeFormatter.ofPattern("MMM yyyy")),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                    items(vacations) { item ->
                        val isStartDateToday =
                            item.startDate == today.format(DateTimeFormatter.ofPattern("d/MM/yyyy"))
                        val isEndDateToday = item.endDate != null && item.endDate == today.format(
                            DateTimeFormatter.ofPattern("d/MM/yyyy")
                        )
                        val cardBorderColor =
                            if (isStartDateToday || isEndDateToday) MaterialTheme.colorScheme.surface else Color.Transparent
                        SwipeToDismissListItem(
                            onEndToStart = {
                                onDelete(item)
                            },
                            content = {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = item.startDate.substringBefore("/"),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                        if (!item.endDate.isNullOrEmpty()) { // Show the down icon and end date if endDate is present
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_down),
                                                contentDescription = "down",
                                                tint = gray
                                            )
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Text(
                                                text = item.endDate.substringBefore("/"),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(if (item.endDate.isNullOrEmpty()) 70.dp else 90.dp)
                                                .padding(10.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = cardBorderColor,
                                                    shape = RoundedCornerShape(10.dp)
                                                ),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onTertiary,
                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        ) {
                                            Text(
                                                text = item.name,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .fillMaxWidth(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                }

                            })
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(holiday: List<VacationData>) {
    CalenderView(isRangeSelection = false, holidays = holiday)
}

@Composable
fun SwipeToDismissListItem(
    modifier: Modifier = Modifier,
    onEndToStart: () -> Unit = {},
    content: @Composable () -> Unit
) {

    // 1. State is hoisted here
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {

            // 2. Animate the swipe by changing the color
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                    SwipeToDismissBoxValue.StartToEnd -> Color.Transparent
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                },
                label = "swipe"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color) // 3. Set the animated color here
            ) {

                // 4. Show the correct icon
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> {}

                    SwipeToDismissBoxValue.EndToStart -> {
                        Icon(
                            modifier = Modifier
                                .align(androidx.compose.ui.Alignment.CenterEnd)
                                .padding(end = 16.dp),
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = "delete"
                        )
                    }

                    SwipeToDismissBoxValue.Settled -> {
                        // Nothing to do
                    }
                }

            }
        }
    ) {
        content()
    }

    // 5. Trigger the callbacks
    when (dismissState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(dismissState.currentValue) {
                onEndToStart()

                // 6. Don't forget to reset the state value
                dismissState.snapTo(SwipeToDismissBoxValue.Settled) // or dismissState.reset()
            }

        }

        SwipeToDismissBoxValue.StartToEnd -> {}

        SwipeToDismissBoxValue.Settled -> {
            // Nothing to do
        }
    }
}



