package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.mbankingapp.ui.composables.RacunCard
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeView(
    viewModel: HomeViewModel = viewModel()
) {
    val racuni by viewModel.racuni.observeAsState()
    val error by viewModel.error
    val errorText by viewModel.errorText

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
        },
        bottomBar = {
            BottomAppBar (
                containerColor = Primary,
                contentColor = Primary
            ) {
                NavigationBar (
                    containerColor = Primary,
                    contentColor = Primary
                ) {
                    NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Početna", tint = Color.White) })
                    NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = { Icon(imageVector = Icons.Outlined.List, contentDescription = "Transakcije", tint = Color.White) })
                    NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = { Icon(imageVector = Icons.Outlined.List, contentDescription = "Bankomati", tint = Color.White) })
                    NavigationBarItem(selected = true, onClick = { /*TODO*/ }, icon = { Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Postavke", tint = Color.White) })
                }
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
                .padding(top = 20.dp)
        ) {
            if (racuni?.isEmpty() == true) {
                if (error) {
                    if (errorText != "") {
                        Text(errorText, textAlign = TextAlign.Center)
                    } else {
                        Text("Greška kod učitavanja popisa računa.")
                    }
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
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
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView(
        viewModel = HomeViewModel()
    )
}