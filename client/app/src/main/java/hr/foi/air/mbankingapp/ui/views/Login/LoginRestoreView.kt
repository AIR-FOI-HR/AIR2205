package hr.foi.air.mbankingapp.ui.views.Login

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.theme.Secondary
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRestoreView(
    viewModel: LoginViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToNext: () -> Unit,
) {
    var email by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold (
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus(true) })
            }
            .padding(horizontal = 20.dp)
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row (modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Oporavak računa", fontSize = 24.sp)
            }
            Row (modifier = Modifier
                .padding(top = 10.dp, bottom = 30.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Unesite podatke", fontSize = 16.sp, color = Color.Gray)
            }
            FormTextField(
                modifier = Modifier.focusRequester(focusRequester),
                label = { Text("E-mail") },
                value = email,
                onValueChange = {email = it},
                isLast = true
            )
            Row (modifier = Modifier
                .padding(top = 20.dp, bottom = 30.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { onNavigateToLogin()  },
                    colors = ButtonDefaults.buttonColors(containerColor = Secondary, contentColor = Color.White)) {
                    Text(text = "Odustani")
                }
                Button(
                    onClick = { viewModel.restore(context, email, onNavigateToNext)  },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White)) {
                    Text(text = "Pošalji")
                }
            }
        }
    }
}