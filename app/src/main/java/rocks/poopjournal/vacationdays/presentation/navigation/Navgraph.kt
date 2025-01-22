package rocks.poopjournal.vacationdays.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import rocks.poopjournal.vacationdays.presentation.screen.about.AboutScreen
import rocks.poopjournal.vacationdays.presentation.screen.add.AddScreen
import rocks.poopjournal.vacationdays.presentation.screen.home.HomeScreen

@Composable
fun NavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Home_Screen) {
        composable(route = Home_Screen) {
            HomeScreen(navHostController = navHostController)
        }

        composable(route = Add_Screen) {
            AddScreen(navHostController = navHostController)
        }

        composable(route = Setting_Screen) {

        }

        composable(route = About_Screen) {
            AboutScreen(navHostController)
        }

        composable(route = Vacation_Days_Screen) {

        }
    }
}