package hr.foi.air.mbankingapp.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.foi.air.mbankingapp.ui.views.Home.HomeView
import hr.foi.air.mbankingapp.ui.views.Home.SveTransakcijeView

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
            SveTransakcijeView(
                innerPadding = innerPadding,
                onNavigateToTransakcija = { id ->
                    navControllerRoot.navigate("transakcija/$id")
                },
                onNavigateToNovaTransakcija = {
                    navControllerRoot.navigate("transakcija/nova")
                },
                onNavigateToSkeniraj = {
                    navControllerRoot.navigate("transakcija/skeniraj")
                },
                onNavigateToKontakti = {
                    navControllerRoot.navigate("kontakti")
                }
            )
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