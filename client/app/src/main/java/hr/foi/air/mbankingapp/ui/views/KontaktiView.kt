package hr.foi.air.mbankingapp.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import hr.foi.air.kontakti.models.Contact
import hr.foi.air.mbankingapp.ui.composables.KontaktItem
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.KontaktiViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun KontaktiView(
    onNavigateToBack: () -> Unit,
    onNavigateToKreirajTransakciju: (String) -> Unit,
    viewModel: KontaktiViewModel = viewModel()
) {
    val kontakti by viewModel.kontakti.observeAsState();
    val error by viewModel.error;
    val errorText by viewModel.errorText;

    val racun by viewModel.racun;
    var errorRacun by viewModel.errorRacun;
    val errorRacunText by viewModel.errorRacunText;

    val context = LocalContext.current;
    var telBroj: String = ""

    LaunchedEffect(Unit) {
        viewModel.loadContacts(context);
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Kontakti",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 20.sp
            )
            Divider(
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )
            if (kontakti?.isEmpty() != true) {
                LazyColumn(Modifier.weight(1f)) {
                    items(kontakti!!) {
                        kontakt -> KontaktItem(kontakt = kontakt.name) {
                            viewModel.fetchRacun(kontakt.phoneNumber);
                            telBroj = kontakt.phoneNumber
                        }
                    }
                }
            } else {
                if (error) {
                    if (errorText != "") {
                        Text(errorText, textAlign = TextAlign.Center)
                    } else {
                        Text("Greška kod učitavanja popisa kontakti.")
                    }
                } else {
                    CircularProgressIndicator()
                }
            }
        }

        if (errorRacun) {
            AlertDialog(
                onDismissRequest = { errorRacun = false },
                confirmButton = { Button(onClick = { errorRacun = false }) { Text("OK") } },
                title = { Text("Pogreška") },
                text = { Text( if (errorRacunText != "") errorRacunText else "Pogreška kod dohvata detalja računa!" ) }
            )
        }

        if (racun != null && !errorRacun) {
            onNavigateToKreirajTransakciju(racun!!.iban);
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KontaktiViewPreview() {
    KontaktiView({}, {})
}