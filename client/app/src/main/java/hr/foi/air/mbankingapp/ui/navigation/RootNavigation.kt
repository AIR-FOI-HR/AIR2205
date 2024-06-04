package hr.foi.air.mbankingapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.Home.HomeRootView
import hr.foi.air.mbankingapp.ui.views.KreirajTransakcijuView
import hr.foi.air.mbankingapp.ui.views.QrRacunView
import hr.foi.air.mbankingapp.ui.views.RacunView
import hr.foi.air.mbankingapp.ui.views.TransakcijaView
import hr.foi.air.qr.composables.CameraView

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
                },
                onNavigateToQr = { iban ->
                    navController.navigate("racun/qr/$iban")
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
        composable(
            route = "racun/qr/{iban}",
            arguments = listOf(navArgument("iban") { type = NavType.StringType })
        ) { navBackStackEntry ->
            QrRacunView(
                iban = navBackStackEntry.arguments?.getString("iban") ?: "?",
                onNavigateToBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable(
            route = "transakcija/nova/{qr}",
            arguments = listOf(navArgument("qr") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            KreirajTransakcijuView (
                qr = navBackStackEntry.arguments?.getString("qr") ?: null,
                onNavigateToBack = {
                    navBackStackEntry.savedStateHandle.remove<String>("qr")
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable(
            route = "transakcija/skeniraj"
        ) { navBackStackEntry ->
            CameraView(
                onSuccessfullScan = { data ->
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                        navController.navigate("transakcija/nova/$data")
                    }
                }
            )
        }
    }
}