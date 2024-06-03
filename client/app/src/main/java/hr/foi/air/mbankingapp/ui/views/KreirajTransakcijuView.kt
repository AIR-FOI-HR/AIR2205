package hr.foi.air.mbankingapp.ui.views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.theme.Primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KreirajTransakcijuView(
    onNavigateToBack: () -> Unit
) {
    var racunIban by remember { mutableStateOf("") }
    var primateljIban by remember { mutableStateOf("") }
    var opisPlacanja by remember { mutableStateOf("") }
    var iznos by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var pozivNaBroj by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

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
                    DropdownMenuItem(
                        text = { Text("Tekući račun - HR000000") },
                        onClick = {
                            //vrstaTran = "ISPLATA"
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
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
                    onClick = {  },
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
    KreirajTransakcijuView({})
}