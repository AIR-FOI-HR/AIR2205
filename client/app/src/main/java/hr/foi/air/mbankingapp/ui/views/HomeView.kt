package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.mbankingapp.ui.composables.RacunCard
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.TransakcijaItem
import hr.foi.air.mbankingapp.ui.theme.UnselectedColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeView(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel()
) {
    val racuni by viewModel.racuni.observeAsState()
    val transakcije by viewModel.transakcije.observeAsState()
    
    val errorRacuni by viewModel.errorRacuni
    val errorRacuniText by viewModel.errorRacuniText

    val errorTran by viewModel.errorTran
    val errorTranText by viewModel.errorTranText

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp)
    ) {
        if (racuni?.isEmpty() == true) {
            if (errorRacuni) {
                if (errorRacuniText != "") {
                    Text(errorRacuniText, textAlign = TextAlign.Center)
                } else {
                    Text("Greška kod učitavanja popisa računa.")
                }
            } else {
                CircularProgressIndicator()
            }
        } else {
            val pagerState = rememberPagerState {
                racuni!!.size
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.wrapContentSize()
            ) { currentPage ->
                if (racuni?.get(currentPage) != null) {
                    RacunCard(
                        vrsrac = racuni?.get(currentPage)!!.vrstaRacuna,
                        iban = racuni?.get(currentPage)!!.iban,
                        stanje = racuni?.get(currentPage)!!.stanje.toString(),
                        onClick = {}
                    )
                }
            }
        }
        Text(
            text = "Zadnje transakcije",
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 20.sp
        )
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
                            onClick = {}
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
