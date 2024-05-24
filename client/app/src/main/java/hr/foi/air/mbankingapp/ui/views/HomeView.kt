package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.mbankingapp.ui.composables.RacunCard
import hr.foi.air.mbankingapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("mBanking", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Obavijesti",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
                .padding(top = 20.dp)
        ) {
            RacunCard(
                vrsrac = "Tekući račun",
                iban = "HR000000000000000000",
                stanje = "10.000,00",
                onClick = {}
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView()
}