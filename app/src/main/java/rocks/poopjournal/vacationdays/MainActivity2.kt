package rocks.poopjournal.vacationdays

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rocks.poopjournal.vacationdays.presentation.navigation.NavGraph
import rocks.poopjournal.vacationdays.presentation.screen.home.HomeScreen
import rocks.poopjournal.vacationdays.presentation.ui.theme.MyVacationDays2Theme
import rocks.poopjournal.vacationdays.presentation.ui.utils.AppTheme
import rocks.poopjournal.vacationdays.presentation.ui.utils.ThemeSetting
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    @Inject
    lateinit var themeSetting: ThemeSetting
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme = themeSetting.themeFlow.collectAsState()
            val useDarkColors = when (theme.value) {
                AppTheme.LIGHT -> false
                AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                AppTheme.DARK -> true
            }
            MyVacationDays2Theme(darkTheme = useDarkColors) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(navHostController = rememberNavController())
                }
            }
        }
    }
}

