package rocks.poopjournal.vacationdays.presentation.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kizitonwose.calendar.core.CalendarMonth
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.data.VacationData
import rocks.poopjournal.vacationdays.presentation.component.CalenderView
import rocks.poopjournal.vacationdays.presentation.component.CustomTab
import rocks.poopjournal.vacationdays.presentation.component.MonthHeader
import rocks.poopjournal.vacationdays.presentation.navigation.About_Screen
import rocks.poopjournal.vacationdays.presentation.navigation.Add_Screen
import rocks.poopjournal.vacationdays.presentation.navigation.Setting_Screen
import rocks.poopjournal.vacationdays.presentation.ui.theme.gray
import rocks.poopjournal.vacationdays.presentation.ui.theme.lightGray
import rocks.poopjournal.vacationdays.presentation.ui.utils.generateWeekDaysForMonth
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), navHostController: NavHostController) {
    val vacation by viewModel.holidays.collectAsState()
    val (selectedTab, setSelectedTab) = remember { mutableIntStateOf(0) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navHostController.navigate(Add_Screen) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TopBar(
                    selectedTab = selectedTab,
                    onTabSelected = setSelectedTab,
                    navHostController = navHostController
                )
                when (selectedTab) {
                    0 -> TimelineView(vacationList = vacation)
                    1 -> Calendar()
                }
            }
        })


}


@Composable
private fun TopBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    navHostController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Center Column for text and icons
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.umbrella),
                        contentDescription = "Vacation",
                        tint = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "16 (12 left)", color = MaterialTheme.colorScheme.background)

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.briefcase),
                        contentDescription = "Total Days",
                        tint = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "349", color = MaterialTheme.colorScheme.background)

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.tempreature),
                        contentDescription = "Sick Days",
                        tint = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "7", color = MaterialTheme.colorScheme.background)
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
                        tint = MaterialTheme.colorScheme.background
                    )
                }

                DropdownMenu(
                    shape = RoundedCornerShape(10.dp),
                    containerColor = MaterialTheme.colorScheme.background,
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
                                    painter = painterResource(id = R.drawable.ic_error),
                                    contentDescription = "About",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stringResource(id = R.string.about),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground
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
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = stringResource(id = R.string.settings),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimelineView(vacationList: List<VacationData>) {
    val currentMonth = YearMonth.now()

    val weekDays = remember(currentMonth) {
        generateWeekDaysForMonth(currentMonth)
    }
    val calendarMonth = CalendarMonth(
        yearMonth = currentMonth,
        weekDays = weekDays
    )

    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        if (vacationList.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.empty_text),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            LazyColumn {
                item {
                    MonthHeader(calendarMonth = calendarMonth)
                }
                items(vacationList) { item ->
                    val isStartDateToday = item.startDate == today.toEpochDay().toInt()
                    val isEndDateToday = item.endDate != null && item.endDate == today.toEpochDay().toInt()
                    val cardBorderColor =
                        if (isStartDateToday || isEndDateToday) MaterialTheme.colorScheme.primary else Color.Transparent

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = item.startDate.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (item.endDate != null) { // Show the down icon and end date if endDate is present
                                Spacer(modifier = Modifier.height(5.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_down),
                                    contentDescription = "down",
                                    tint = gray
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = item.endDate.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(if (item.endDate == null) 48.dp else 96.dp)
                                    .padding(10.dp)
                                    .border(
                                        width = 1.dp,
                                        color = cardBorderColor,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = lightGray,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                Text(
                                    text = "Holiday",
                                    modifier = Modifier.padding(10.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar() {
    CalenderView(isRangeSelection = false)
}