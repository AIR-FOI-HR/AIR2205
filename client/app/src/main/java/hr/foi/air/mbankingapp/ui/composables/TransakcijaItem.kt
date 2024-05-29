package hr.foi.air.mbankingapp.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import hr.foi.air.mbankingapp.ui.theme.TransakcijaBoxColor

@Composable
fun TransakcijaItem(
    primatelj: String,
    iznos: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = TransakcijaBoxColor
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(primatelj, fontSize = TextUnit(20.0f, TextUnitType.Sp),)
            Text("$iznos €", fontSize = TextUnit(20.0f, TextUnitType.Sp),)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransakcijaItemPreview() {
    TransakcijaItem(primatelj = "Primatelj", iznos = "10.00", onClick = {})
}