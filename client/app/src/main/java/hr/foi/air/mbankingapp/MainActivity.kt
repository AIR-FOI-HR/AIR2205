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
import hr.foi.air.mbankingapp.ui.navigation.RootNavigation
import hr.foi.air.mbankingapp.ui.theme.MBankingAppTheme
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.RegisterViewModel
import hr.foi.air.mbankingapp.ui.views.HomeView
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreFinalView
import hr.foi.air.mbankingapp.ui.views.Login.LoginRestoreView
import hr.foi.air.mbankingapp.ui.views.Login.LoginView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterPinView
import hr.foi.air.mbankingapp.ui.views.Register.RegisterView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MBankingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   RootNavigation(navController = rememberNavController())
                }
            }
        }
    }
}
