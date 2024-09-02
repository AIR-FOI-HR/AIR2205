package hr.foi.air.mbankingapp.data.repository.placanja

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import hr.foi.air.mbankingapp.data.models.Transakcija

interface PlacanjeRepository {
    @Composable
    fun pozivPlacanja(navController: NavHostController)
}