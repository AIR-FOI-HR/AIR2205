package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.NamedLabel
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.RacunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacunView(
    iban: String,
    onNavigateToBack: () -> Unit,
    onNavigateToQr: (String) -> Unit,
    viewModel: RacunViewModel = viewModel()
) {
    var racun by viewModel.racun

    LaunchedEffect(Unit) {
        viewModel.loadRacun(iban)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("mBanking", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigateToBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Natrag",
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
            if (racun != null) {
                NamedLabel(title = "Vrsta računa", value = racun!!.vrstaRacuna)
                NamedLabel(title = "Vlasnik", value = racun!!.vlasnik)
                NamedLabel(title = "IBAN", value = racun!!.iban)
                NamedLabel(title = "Status", value = if (racun!!.aktivnost == "D") "AKTIVAN" else "NEAKTIVAN")
                NamedLabel(title = "Valuta", value = racun!!.valuta)
                NamedLabel(title = "Vrijedi od", value = racun!!.vrijediOd)
                NamedLabel(title = "Trenutno stanje", value = racun!!.stanje.toString() + " €")

                Row (
                    modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        onClick = { onNavigateToQr(racun!!.iban) },
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.QrCode, contentDescription = "QR kod", modifier = Modifier.size(48.dp))
                    }
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RacunViewPreview() {
    RacunView("HR0000000000000000000", {}, {})
}