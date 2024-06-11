package hr.foi.air.mbankingapp.ui.views.Home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.mbankingapp.data.context.Theme

@Composable
fun PostavkeView(
    innerPadding: PaddingValues
) {
    var darkTheme by Theme.isDarkTheme

    Column (
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "Postavke",
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 20.sp
        )
        Divider(
            modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tamna tema")
            Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Promjena PIN-a")
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos  , contentDescription = "Navigacija")
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Moj profil")
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos  , contentDescription = "Navigacija")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostavkeViewPreview() {
    PostavkeView(innerPadding = PaddingValues())
}