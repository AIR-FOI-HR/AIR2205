package hr.foi.air.mbankingapp.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hr.foi.air.mbankingapp.ui.views.HomeView
import hr.foi.air.mbankingapp.ui.views.RacunView

@Composable
fun HomeNavigation(
    innerPadding: PaddingValues,
    navController: NavHostController,
    navControllerRoot: NavHostController
) {
    NavHost(
        navController = navController,
        route = "home_root",
        startDestination = "home"
    ) {
        composable("home") {
            HomeView(
                innerPadding = innerPadding,
                onNavigateToRacun = { iban ->
                    navControllerRoot.navigate("racun/$iban")
                },
                onNavigateToTransakcija = { id ->
                    navControllerRoot.navigate("transakcija/$id")
                }
            )
        }
        composable("transakcije") {
            Column (modifier = Modifier.padding(innerPadding)) {
                Text("Transakcije")
            }
        }
        composable("bankomati") {
            Column (modifier = Modifier.padding(innerPadding)) {
                Text("Bankomati")
            }
        }
        composable("postavke") {
            Column (modifier = Modifier.padding(innerPadding)) {
                Text("Postavke")
            }
        }
    }
}