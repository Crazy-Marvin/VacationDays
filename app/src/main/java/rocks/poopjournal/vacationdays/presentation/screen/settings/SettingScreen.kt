package rocks.poopjournal.vacationdays.presentation.screen.settings

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import rocks.poopjournal.vacationdays.R
import rocks.poopjournal.vacationdays.presentation.component.ThemeSelectionDialog
import rocks.poopjournal.vacationdays.presentation.navigation.Vacation_Days_Screen
import rocks.poopjournal.vacationdays.presentation.ui.theme.gray
import rocks.poopjournal.vacationdays.presentation.ui.theme.lightGray
import java.time.LocalDate


@Composable
fun SectionHeader(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.surface,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )

    }

    HorizontalDivider(color = lightGray)
}

@Composable
fun SettingRow(
    text: String,
    subText: String? = null,
    beforeText: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    block: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 16.dp, end = 16.dp)
            .clickable {
                onClick?.let { it() }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Row {
                beforeText?.let {
                    it()
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            subText?.also {
                Text(
                    text = it,
                    color = gray,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        block?.also {
            it()
        }
    }
}

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }
    val vacationList by viewModel.vacationNumber.collectAsState()
    val context = LocalContext.current
    val currentYear = LocalDate.now().year.toString()
    val vacationForCurrentYear = vacationList.find { it.name == currentYear }
    val vacationNumber = vacationForCurrentYear?.numberOfVacation ?: 0
    var pendingEnableNotification by remember { mutableStateOf(false) }

    var localFeatureEnabled by remember { mutableStateOf(false) }
    var localExcludeWeekendsEnabled by remember { mutableStateOf(false) }
    val localShowWeekdaysHeaderEnabled by viewModel.themeSetting.isShowWeekDaysHeaderFlow.collectAsState()
    val isNotificationEnabled by viewModel.themeSetting.isVacationNotificationEnabledFlow.collectAsState()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && pendingEnableNotification) {
            viewModel.themeSetting.isVacationNotificationEnabled = true
            viewModel.scheduleVacationNotifications(context)
        } else {
            Toast.makeText(context, R.string.notification_permission_denied, Toast.LENGTH_SHORT).show()
        }
        pendingEnableNotification = false
    }

    LaunchedEffect(localFeatureEnabled) {
        localFeatureEnabled = viewModel.themeSetting.isFeatureEnabled
        localExcludeWeekendsEnabled = viewModel.themeSetting.isExcludeWeekendsEnabled
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        TopBar(onClose = { navHostController.popBackStack() })
        Column {
            SectionHeader(stringResource(id = R.string.general))

            SettingRow(
                text = stringResource(id = R.string.noofvacations),
                subText = vacationNumber.toString(),
                onClick = { navHostController.navigate(Vacation_Days_Screen) }
            )

            SettingRow(
                text = stringResource(id = R.string.track)
            ) {
                Switch(
                    checked = localFeatureEnabled,
                    onCheckedChange = { newState ->
                        localFeatureEnabled = newState

                        viewModel.themeSetting.isFeatureEnabled = newState
                    },
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        checkedThumbColor = MaterialTheme.colorScheme.surface
                    ),
                )
            }

            SettingRow(
                text = stringResource(id = R.string.exclude_weekends)
            ) {
                Switch(
                    checked = localExcludeWeekendsEnabled,
                    onCheckedChange = { newState ->
                        localExcludeWeekendsEnabled = newState

                        viewModel.themeSetting.isExcludeWeekendsEnabled = newState
                    },
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        checkedThumbColor = MaterialTheme.colorScheme.surface
                    ),
                )
            }

            SettingRow(
                text = stringResource(id = R.string.show_weekdays_header)
            ) {
                Switch(
                    checked = localShowWeekdaysHeaderEnabled,
                    onCheckedChange = { newState ->
                        viewModel.themeSetting.isShowWeekDaysHeaderEnabled = newState
                    },
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        checkedThumbColor = MaterialTheme.colorScheme.surface
                    ),
                )
            }

            SettingRow(
                text = stringResource(id = R.string.enable_notification),
                subText = stringResource(id = R.string.get_notified)
            ) {
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = { newState ->
                        if (newState) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                // Ask for notification permission
                                pendingEnableNotification = true
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                // No permission required on older Android versions
                                viewModel.themeSetting.isVacationNotificationEnabled = true
                                viewModel.scheduleVacationNotifications(context)
                            }
                        } else {
                            viewModel.themeSetting.isVacationNotificationEnabled = false
                            viewModel.cancelVacationNotifications(context)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                        checkedThumbColor = MaterialTheme.colorScheme.surface
                    ),
                )
            }


            SettingRow(
                text = stringResource(id = R.string.appearance),
                subText = stringResource(id = viewModel.themeSetting.theme.getStringResId()),
                onClick = { showDialog = true }
            ) {
                if (showDialog) {
                    ThemeSelectionDialog(
                        onDismissRequest = { showDialog = false },
                        userSetting = viewModel.themeSetting
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SectionHeader(stringResource(R.string.data))
            SettingRow(
                text = stringResource(id = R.string.backupheading),
                onClick = { viewModel.backupDatabase(context.getString(R.string.backupMessage)) },
                beforeText = {
                    Icon(
                        painter = painterResource(id = R.drawable.backup),
                        contentDescription = "Backup",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            SettingRow(
                text = stringResource(id = R.string.restore),
                onClick = { viewModel.restoreDatabase() },
                beforeText = {
                    Icon(
                        painter = painterResource(id = R.drawable.restore),
                        contentDescription = "restore",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun TopBar(onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = stringResource(id = R.string.settings),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
