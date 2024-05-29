package hr.foi.air.mbankingapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import hr.foi.air.mbankingapp.ui.theme.BoxColor

@Composable
fun RacunCard(
    vrsrac: String,
    iban: String,
    stanje: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(30.dp),
        color = BoxColor
    ) {
        Column (
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = vrsrac,
                fontSize = TextUnit(20.0f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = iban,
                fontSize = TextUnit(16.0f, TextUnitType.Sp)
            )
            Row (
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "Stanje: ",
                    fontSize = TextUnit(16.0f, TextUnitType.Sp)
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = "$stanje €",
                    fontSize = TextUnit(25.0f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RacunCardPreview() {
    RacunCard(
        vrsrac = "Tekući račun",
        iban = "HR000000000000000000",
        stanje = "10.000,00",
        onClick = {}
    )
}