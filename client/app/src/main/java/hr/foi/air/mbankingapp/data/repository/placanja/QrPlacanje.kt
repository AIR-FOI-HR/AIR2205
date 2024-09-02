package hr.foi.air.mbankingapp.data.repository.placanja

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import hr.foi.air.qr.composables.CameraView

class QrPlacanje : PlacanjeRepository {
    @Composable
    override fun pozivPlacanja(navController: NavHostController) {
        CameraView(
            onSuccessfullScan = { data ->
                if (navController.previousBackStackEntry != null) {
                    navController.navigateUp()
                    navController.navigate("transakcija/nova?qr=$data")
                }
            }
        )
    }


}