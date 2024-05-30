package hr.foi.air.mbankingapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreFinalView
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreView
import hr.foi.air.mbankingapp.ui.views.Login.LoginView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterPinView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterView

fun NavGraphBuilder.authNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    navigation(
        route = "auth",
        startDestination = "login"
    ) {
        composable("login") {
            LoginView(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRestore = { navController.navigate("login/restore"); },
                onSuccesfullLogin = { navController.navigate("home_root"); }
            )
        }
        composable("login/restore") {
            LoginRestoreView(
                viewModel = loginViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onNavigateToNext = { navController.navigate("login/restore/pin") }
            )
        }
        composable("login/restore/pin") {
            LoginRestoreFinalView(
                viewModel = loginViewModel,
                onNavigateToLogin = { navController.popBackStack(route = "login", inclusive = false) }
            )
        }
        composable("register") {
            RegisterView(
                viewModel = registerViewModel,
                onNavigationToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                },
                onNavigationToRegisterPin = { navController.navigate("register/pin") }
            )
        }
        composable("register/pin") {
            RegisterPinView(
                viewModel = registerViewModel,
                onNavigationToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                },
                onNavigationToBack = { navController.popBackStack(); }
            )
        }
    }
}