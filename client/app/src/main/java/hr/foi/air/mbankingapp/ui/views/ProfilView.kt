package hr.foi.air.mbankingapp.ui.views

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.foi.air.mbankingapp.data.context.Auth
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.PostavkeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilView(
    onNavigateToBack: () -> Unit,
    viewModel: PostavkeViewModel = viewModel()
) {
    var telBroj by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nazivKorisnika by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current;

    var error by viewModel.error;
    var errorText by viewModel.errorText;

    var success by viewModel.success;

    if (error) {
        if (errorText != "") {
            Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Došlo je do pogreške!", Toast.LENGTH_SHORT).show();
        }
        error = false;
    }

    if (success) {
        Toast.makeText(context, "Podaci su spremljeni!", Toast.LENGTH_SHORT).show();
        success = false;
    }

    LaunchedEffect(Unit) {
        if (Auth.loggedUser != null) {
            telBroj = Auth.loggedUser!!.telBroj ?: "";
            email = Auth.loggedUser!!.email ?: "";
            nazivKorisnika = "${Auth.loggedUser!!.ime} ${Auth.loggedUser!!.prezime}"
        }
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
                text = "Moj profil - $nazivKorisnika",
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 20.sp
            )
            Divider(
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )
            FormTextField(
                modifier = Modifier.focusRequester(focusRequester),
                label = { Text("Tel. broj") },
                value = telBroj,
                onValueChange = {telBroj = it},
            )
            FormTextField(
                modifier = Modifier,
                label = { Text("E-mail") },
                value = email,
                onValueChange = {email = it},
                keyboardType = KeyboardType.Number,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        viewModel.setPodaci(telBroj, email)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)
                ) {
                    Text(text = "Spremi promjene")
                }
            }
        }
    }
}