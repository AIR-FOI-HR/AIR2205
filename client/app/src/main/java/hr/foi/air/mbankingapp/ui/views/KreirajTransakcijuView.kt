package hr.foi.air.mbankingapp.ui.views

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.JsonObject
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.RacunViewModel
import hr.foi.air.mbankingapp.ui.viewmodels.TransakcijaViewModel
import org.json.JSONException
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KreirajTransakcijuView(
    onNavigateToBack: () -> Unit,
    qr: String?,
    kontakt: String?,
    racunViewModel: RacunViewModel = viewModel(),
    transakcijaViewModel: TransakcijaViewModel = viewModel()
) {
    val racuni = racunViewModel.racuni.observeAsState();
    var error by transakcijaViewModel.errorTran;
    val errorText by transakcijaViewModel.errorTranText;

    var resetData by transakcijaViewModel.resetData;

    var stanjeRacuna by remember { mutableStateOf(0f) }

    var racunIban by remember { mutableStateOf("") }
    var primateljIban by remember { mutableStateOf("") }
    var opisPlacanja by remember { mutableStateOf("") }
    var iznos by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var pozivNaBroj by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current;
    val context = LocalContext.current;

    LaunchedEffect(Unit) {
        if (qr != null) {
            try {
                var json = JSONObject(qr);
                primateljIban = json.getString("primatelj");
                iznos = json.getString("iznos");
                opisPlacanja = json.getString("opis");
                model = json.getString("model");
                pozivNaBroj = json.getString("pozivNaBroj");
            } catch (e: JSONException) {
                Toast.makeText(context, "Neispravan QR kod.", Toast.LENGTH_SHORT).show()
            }
        } else if (kontakt != null) {
            primateljIban = kontakt;
        }

        racunViewModel.loadRacuni()
    }

    if (error) {
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
        error = false;
    }

    if (resetData) {
        racunIban = "";
        primateljIban = "";
        iznos = "";
        opisPlacanja = "";
        model = "";
        pozivNaBroj = "";
        Toast.makeText(context, "Transakcija je uspješno izvršena!", Toast.LENGTH_SHORT).show()
        resetData = false;
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus(true) })
        },
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
                    IconButton(onClick = {
                            onNavigateToBack()
                        }
                    ) {
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
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Nova transakcija",
                fontSize = 20.sp
            )
            Divider(
                color = Color.Black,
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(bottom = 15.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                    singleLine = true,
                    label = { Text("Odabir računa") },
                    value = racunIban,
                    onValueChange = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    if (racuni.value?.isNullOrEmpty() != true) {
                        racuni.value!!.forEach{ racun ->
                            DropdownMenuItem(
                                text = { Text(racun.vrstaRacuna + " - " + racun.iban) },
                                onClick = {
                                    racunIban = racun.iban;
                                    stanjeRacuna = racun.stanje;
                                    expanded = false;
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }
            FormTextField(
                modifier = Modifier,
                label = { Text("IBAN primatelja") },
                value = primateljIban,
                onValueChange = { primateljIban = it }
            )
            FormTextField(
                modifier = Modifier,
                label = { Text("Opis plaćanja") },
                value = opisPlacanja,
                onValueChange = { opisPlacanja = it }
            )
            FormTextField(
                modifier = Modifier,
                label = { Text("Iznos") },
                value = iznos,
                onValueChange = { iznos = it },
                keyboardType = KeyboardType.Decimal,
                followUpText = "EUR"
            )
            Row {
                FormTextField(
                    modifier = Modifier.weight(1.2f),
                    label = { Text("Model") },
                    value = model,
                    onValueChange = { model = it }
                )
                FormTextField(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 10.dp),
                    label = { Text("Poziv na broj") },
                    value = pozivNaBroj,
                    onValueChange = { pozivNaBroj = it },
                    isLast = true
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        if (stanjeRacuna < iznos.toFloat()) {
                            Toast.makeText(context, "Nedovoljno sredstava na računu!", Toast.LENGTH_SHORT).show()
                        } else {
                            transakcijaViewModel.createTransakcija(
                                racunIban,
                                primateljIban,
                                opisPlacanja,
                                iznos,
                                model,
                                pozivNaBroj
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                    Text(text = "Potvrdi")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KreirajTransakcijuViewPreview() {
    KreirajTransakcijuView({}, null,null)
}