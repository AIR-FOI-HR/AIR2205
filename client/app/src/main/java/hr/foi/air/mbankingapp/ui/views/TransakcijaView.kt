package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.NamedLabel
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.TransakcijaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransakcijaView(
    id: Int,
    onNavigateToBack: () -> Unit,
    viewModel: TransakcijaViewModel = viewModel()
) {
    var transakcija by viewModel.transakcija

    LaunchedEffect(Unit) {
        viewModel.loadTransakcija(id);
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Transakcija",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 20.sp
            )
            Divider(
                color = Color.Black,
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )
            if (transakcija != null) {
                NamedLabel(title = "Primatelj", value = transakcija!!.primateljVlasnik ?: "")
                NamedLabel(title = "Primatelj IBAN", value = transakcija!!.primateljIban)
                NamedLabel(title = "Platitelj", value = transakcija!!.platiteljVlasnik ?: "")
                NamedLabel(title = "Platitelj IBAN", value = transakcija!!.platiteljIban)
                NamedLabel(title = "Opis plaćanja", value = transakcija!!.opisPlacanja)
                NamedLabel(title = "Iznos", value = "${transakcija!!.iznos} €")
                NamedLabel(title = "Model plaćanja", value = transakcija!!.model)
                NamedLabel(title = "Poziv na broj", value = transakcija!!.pozivNaBroj)
                NamedLabel(title = "Datum transakcije", value = transakcija!!.datum)
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}