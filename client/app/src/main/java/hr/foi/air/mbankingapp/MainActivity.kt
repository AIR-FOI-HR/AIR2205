package hr.foi.air.mbankingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.foi.air.mbankingapp.ui.theme.MBankingAppTheme
import hr.foi.air.mbankingapp.ui.viewmodels.HomeViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.HomeView
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreFinalView
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreView
import hr.foi.air.mbankingapp.ui.views.Login.LoginView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterPinView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterView

class MainActivity : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBankingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController();

                    NavHost(navController = navController, startDestination = "login" ) {
                        composable("login") {
                            LoginView(
                                viewModel = loginViewModel,
                                onNavigateToRegister = {
                                    navController.navigate("register") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRestore = { navController.navigate("login/restore"); },
                                onSuccesfullLogin = { navController.navigate("home"); }
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
                        composable("home") {
                            HomeView()
                        }
                    }
                }
            }
        }
    }
}
