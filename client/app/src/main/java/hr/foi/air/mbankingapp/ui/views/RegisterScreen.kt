package hr.foi.air.mbankingapp.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    var ime by remember { mutableStateOf("") }
    var prezime by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var oib by remember { mutableStateOf("") }

    Scaffold (
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                Row (modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Dobrodošli na mBanking!", fontSize = 24.sp)
                }
                Row (modifier = Modifier
                    .padding(top = 10.dp, bottom = 40.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("Registrirajte se", fontSize = 16.sp, color = Color.Gray)
                }
                Row (modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {Text("Ime")}, value = ime, onValueChange = {ime = it}, shape = RoundedCornerShape(12.dp))
                }
                Row (modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {Text("Prezime")}, value = prezime, onValueChange = {prezime = it}, shape = RoundedCornerShape(12.dp))
                }
                Row (modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {Text("E-mail")}, value = email, onValueChange = {email = it}, shape = RoundedCornerShape(12.dp))
                }
                Row (modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(modifier = Modifier.fillMaxWidth(), label = {Text("OIB")}, value = oib, onValueChange = {oib = it}, shape = RoundedCornerShape(12.dp))
                }
            }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}