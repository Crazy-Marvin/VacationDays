package rocks.poopjournal.vacationdays.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rocks.poopjournal.vacationdays.presentation.screen.about.AboutScreen
import rocks.poopjournal.vacationdays.presentation.screen.add.AddScreen
import rocks.poopjournal.vacationdays.presentation.screen.edit.EditScreen
import rocks.poopjournal.vacationdays.presentation.screen.home.HomeScreen
import rocks.poopjournal.vacationdays.presentation.screen.settings.SettingScreen
import rocks.poopjournal.vacationdays.presentation.screen.settings.VacationDays

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Home_Screen) {
        composable(route = Home_Screen) {
            HomeScreen(navHostController = navHostController)
        }

        composable(route = Add_Screen) {
            AddScreen(navHostController = navHostController)
        }

        composable(route = "$Edit_Screen/{id}", arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
        ) { backStackEntry ->
            val vacationId = backStackEntry.arguments?.getInt("id") ?: return@composable
            EditScreen(vacationId = vacationId, navHostController = navHostController)
        }

        composable(route = Setting_Screen) {
            SettingScreen(navHostController = navHostController)
        }

        composable(route = About_Screen) {
            AboutScreen(navHostController)
        }

        composable(route = Vacation_Days_Screen) {
            VacationDays(navHostController = navHostController)
        }
    }
}