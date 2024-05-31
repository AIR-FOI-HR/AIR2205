package hr.foi.air.mbankingapp.ui.views.Login

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.mbankingapp.ui.composables.FormTextField
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToRestore: () -> Unit,
    onSuccesfullLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }

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
                Text("Dobrodošli na ", fontSize = 24.sp)
                Text("mBanking", fontSize = 24.sp, color = Primary)
                Text("!", fontSize = 24.sp)
            }
            Row (modifier = Modifier
                .padding(top = 10.dp, bottom = 30.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Prijavite se", fontSize = 16.sp, color = Color.Gray)
            }
            FormTextField(
                modifier = Modifier.focusRequester(focusRequester),
                label = { Text("E-mail") },
                value = email,
                onValueChange = {email = it}
            )
            FormTextField(
                modifier = Modifier,
                label = { Text("PIN") },
                value = pin,
                onValueChange = {pin = it},
                isLast = true,
                keyboardType = KeyboardType.Number,
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = { viewModel.login(context, email, pin, onSuccesfullLogin) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
                Text(text = "Prijava")
            }
            Row (modifier = Modifier
                .padding(top = 30.dp, bottom = 15.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("Nemate račun? ", fontSize = 12.sp)
                Text("Registrirajte se!",
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {
                        onNavigateToRegister()
                    },
                    color = Primary,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
            Text("Oporavak računa",
                fontSize = 12.sp,
                modifier = Modifier.clickable { onNavigateToRestore() },
                color = Primary,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}