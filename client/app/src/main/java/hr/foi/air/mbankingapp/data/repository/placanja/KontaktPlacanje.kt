package hr.foi.air.mbankingapp.data.repository.placanja

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import hr.foi.air.mbankingapp.ui.views.KontaktiView

class KontaktPlacanje : PlacanjeRepository {
    @Composable
    override fun pozivPlacanja(navController: NavHostController) {
        KontaktiView(
            onNavigateToBack = {
                if (navController.previousBackStackEntry != null) {
                    navController.navigateUp()
                }
            },
            onNavigateToKreirajTransakciju = { data ->
                if (navController.previousBackStackEntry != null) {
                    navController.navigateUp()
                    navController.navigate("transakcija/nova?kontakt=$data")
                }
            }
        )
    }
}