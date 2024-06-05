package hr.foi.air.mbankingapp.ui.views.Home

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.composables.TransakcijaItem
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.theme.Secondary
import hr.foi.air.mbankingapp.ui.viewmodels.TransakcijaViewModel
import hr.foi.air.qr.composables.CameraView
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SveTransakcijeView(
    innerPadding: PaddingValues,
    onNavigateToTransakcija: (Int) -> Unit,
    onNavigateToNovaTransakcija: () -> Unit,
    onNavigateToSkeniraj: () -> Unit,
    viewModel: TransakcijaViewModel = viewModel()
) {
    val transakcije by viewModel.transakcije.observeAsState()
    val errorTran by viewModel.errorTran
    val errorTranText by viewModel.errorTranText

    val filter by viewModel.filter
    var isOpenedFilter by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.loadTransakcije()
    }

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp, vertical = 10.dp)
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
            Row {
                if (filter != "") {
                    Text("Ukloni filter",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .clickable { viewModel.loadTransakcije() },
                        color = Secondary,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }
                Text("Filtriraj",
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { isOpenedFilter = true },
                    color = Primary,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        if (filter != "") {
            Text(filter, fontSize = 12.sp)
        }
        Divider(
            color = Color.Black,
            modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
        )
        if (transakcije?.isEmpty() != true) {
            LazyColumn (Modifier.weight(1f)) {
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
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            var menuExpanded by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier.size(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    onClick = { menuExpanded = !menuExpanded },
                    shape = RoundedCornerShape(50.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Menu",
                        modifier = Modifier.size(36.dp)
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Novo plaćanje") }, onClick = { onNavigateToNovaTransakcija() })
                        DropdownMenuItem(text = { Text("Skeniraj i plati") }, onClick = { onNavigateToSkeniraj() })
                        DropdownMenuItem(text = { Text("Kontakti") }, onClick = { /*TODO*/ })
                    }
                }
            }
        }
    }

    var vrstaTran by remember { mutableStateOf("") }
    var odDatumaTran by remember { mutableStateOf("") }
    var doDatumaTran by remember { mutableStateOf("") }
    var odIznosa by remember { mutableStateOf("") }
    var doIznosa by remember { mutableStateOf("") }

    val context = LocalContext.current;
    val calendar = Calendar.getInstance();

    val year = calendar[Calendar.YEAR];
    val month = calendar[Calendar.MONTH];
    val day = calendar[Calendar.DAY_OF_MONTH];

    val datePickerOd = DatePickerDialog(
        context,
        {_: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            odDatumaTran = "$selectedDay-${selectedMonth+1}-$selectedYear"
        }, year, month, day
    )

    val datePickerDo = DatePickerDialog(
        context,
        {_: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            doDatumaTran = "$selectedDay-${selectedMonth+1}-$selectedYear"
        }, year, month, day
    )

    var expanded by remember { mutableStateOf(false) }
    var focusManager = LocalFocusManager.current;

    if (isOpenedFilter) {
        Dialog(
            onDismissRequest = { isOpenedFilter = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { focusManager.clearFocus(true) })
                    },
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Filtriraj transakcije",
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
                            label = { Text("Vrsta transakcije") },
                            value = vrstaTran,
                            onValueChange = {},
                            shape = RoundedCornerShape(12.dp),
                            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("ISPLATA") },
                                onClick = {
                                    vrstaTran = "ISPLATA"
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                            DropdownMenuItem(
                                text = { Text("UPLATA") },
                                onClick = {
                                    vrstaTran = "UPLATA"
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                    FormTextField(
                        modifier = Modifier,
                        label = { Text("Od datuma transakcije") },
                        value = odDatumaTran,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerOd.show() }) {
                                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "DatePick")
                            }
                        }
                    )
                    FormTextField(
                        modifier = Modifier,
                        label = { Text("Do datuma transakcije") },
                        value = doDatumaTran,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDo.show() }) {
                                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "DatePick")
                            }
                        }
                    )
                    FormTextField(
                        modifier = Modifier,
                        label = { Text("Od iznosa €") },
                        value = odIznosa,
                        onValueChange = { odIznosa = it },
                        keyboardType = KeyboardType.Number,
                    )
                    FormTextField(
                        modifier = Modifier,
                        label = { Text("Do iznosa €") },
                        value = doIznosa,
                        onValueChange = { doIznosa = it },
                        keyboardType = KeyboardType.Number,
                        isLast = true
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                viewModel.loadTransakcije(vrstaTran, odDatumaTran, doDatumaTran, odIznosa, doIznosa);
                                vrstaTran = "";
                                odDatumaTran = "";
                                doDatumaTran = "";
                                odIznosa = "";
                                doIznosa = "";
                                isOpenedFilter = false;
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                            Text(text = "Filtriraj")
                        }
                        Button(
                            onClick = { isOpenedFilter = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Secondary)) {
                            Text(text = "Odustani")
                        }
                    }
                }
            }
        }
    }
}