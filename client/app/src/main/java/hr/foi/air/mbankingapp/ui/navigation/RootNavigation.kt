package hr.foi.air.mbankingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.HomeRootView
import hr.foi.air.mbankingapp.ui.views.HomeView
import hr.foi.air.mbankingapp.ui.views.RacunView
import hr.foi.air.mbankingapp.ui.views.TransakcijaView

@Composable
fun RootNavigation(navController: NavHostController) {
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(
        navController = navController,
        route = "root",
        startDestination = "auth"
    ) {
        authNavigation(
            navController,
            loginViewModel,
            registerViewModel
        )
        composable("home_root") {
            HomeRootView(
                navController
            )
        }
        composable(
            route = "racun/{iban}",
            arguments = listOf(navArgument("iban") { type = NavType.StringType })
        ) { navBackStackEntry ->
            RacunView(
                iban = navBackStackEntry.arguments?.getString("iban") ?: "?",
                onNavigateToBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable(
            route = "transakcija/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { navBackStackEntry ->
            TransakcijaView (
                id = navBackStackEntry.arguments?.getInt("id") ?: 0,
                onNavigateToBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}