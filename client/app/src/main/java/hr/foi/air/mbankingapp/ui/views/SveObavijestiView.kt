package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.ObavijestItem
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.ObavijestiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SveObavijestiView(
    onNavigateToObavijest: (String) -> Unit,
    onNavigateToBack: () -> Unit,
    viewModel: ObavijestiViewModel = viewModel()
) {
    val obavijesti by viewModel.obavijesti.observeAsState();
    var error by viewModel.error;
    var errorText by viewModel.errorText;

    LaunchedEffect(Unit) {
        viewModel.fetchObavijesti()
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
        ) {
            Text(
                text = "Obavijesti",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 20.sp
            )
            Divider(
                color = Color.Black,
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )
            if (obavijesti?.isEmpty() != true) {
                LazyColumn(Modifier.weight(1f)) {
                    items(obavijesti!!) {obavijest ->
                        ObavijestItem(
                            naslov = obavijest.naslov,
                            isRead = obavijest.procitano ?: false,
                            onClick = {
                                if (obavijest.id != null) {
                                    if (obavijest.procitano != true) {
                                        viewModel.procitajObavijest(obavijest.id)
                                    }
                                    onNavigateToObavijest(obavijest.id.toString())
                                }
                            }
                        )
                    }
                }
            } else if (error) {
                if (errorText != "") {
                    Text(errorText)
                } else {
                    Text("Greška kod dohvata obavijesti.")
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}