package hr.foi.air.mbankingapp.ui.views.Home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.TransakcijaItem
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.TransakcijaViewModel

@Composable
fun SveTransakcijeView(
    innerPadding: PaddingValues,
    onNavigateToTransakcija: (Int) -> Unit,
    viewModel: TransakcijaViewModel = viewModel()
) {
    val transakcije by viewModel.transakcije.observeAsState()
    val errorTran by viewModel.errorTran
    val errorTranText by viewModel.errorTranText

    LaunchedEffect(Unit) {
        viewModel.loadTransakcije()
    }

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp, vertical = 10.dp)
            //.verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Transakcije",
                fontSize = 20.sp
            )
            Text("Filtriraj",
                fontSize = 12.sp,
                modifier = Modifier.clickable {  },
                color = Primary,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
        Divider(
            color = Color.Black,
            modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
        )
        if (transakcije?.isEmpty() != true) {
            LazyColumn {
                items(transakcije!!) { transakcija ->
                    TransakcijaItem(
                        primatelj = if (transakcija.iznos.contains("+")) transakcija.platiteljVlasnik!! else transakcija.primateljVlasnik!!,
                        iznos = transakcija.iznos,
                        onClick = {
                            onNavigateToTransakcija(transakcija.id!!)
                        }
                    )
                }
            }
        } else {
            if (errorTran) {
                if (errorTranText != "") {
                    Text(errorTranText, textAlign = TextAlign.Center)
                } else {
                    Text("Greška kod učitavanja popisa transakcija.")
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}