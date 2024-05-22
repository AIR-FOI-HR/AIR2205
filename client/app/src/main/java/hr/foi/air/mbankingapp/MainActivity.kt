package hr.foi.air.mbankingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.foi.air.mbankingapp.ui.theme.MBankingAppTheme
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.Register.RegisterKodView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterPinView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterView

class MainActivity : ComponentActivity() {
    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBankingAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController();

                    NavHost(navController = navController, startDestination = "register" ) {
                        composable("register") {
                            RegisterView(viewModel = registerViewModel, navController = navController)
                        }
                        composable("register/pin") {
                            RegisterPinView(viewModel = registerViewModel, navController = navController)
                        }
                        composable("register/pin/kod") {
                            RegisterKodView(viewModel = registerViewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
