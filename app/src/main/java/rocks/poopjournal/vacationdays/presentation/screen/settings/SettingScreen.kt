package rocks.poopjournal.vacationdays.presentation.screen.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(onClose = { navHostController.popBackStack() })
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.general),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )

            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(start = 16.dp)
                    .clickable { navHostController.navigate(Vacation_Days_Screen) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.noofvacations),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = vacationNumber.toString(),
                        color = gray,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.track),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,

                    )

                Switch(
                    checked = true, onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedIconColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        checkedThumbColor = MaterialTheme.colorScheme.primary
                    ),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(start = 16.dp)
                    .clickable { showDialog = true },
                verticalAlignment = Alignment.CenterVertically,
                ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.appearance),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(id = viewModel.themeSetting.theme.getStringResId()),
                        color = gray,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Light,
                    )
                    if (showDialog) {
                        ThemeSelectionDialog(
                            onDismissRequest = { showDialog = false },
                            userSetting = viewModel.themeSetting
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
               verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.data),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(16.dp)
                    .clickable {
                        viewModel.backupDatabase(context.getString(R.string.backupMessage))
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.backup),
                    contentDescription = "Backup",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.backupheading),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 16.dp)
                    .clickable {
                        viewModel.restoreDatabase()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.restore),
                    contentDescription = "Backup",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.restore),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onClose() }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.background
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(id = R.string.settings),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(2f)
            )
        }
    }
}
