package hr.foi.air.mbankingapp.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material3.Icon
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
fun ObavijestItem(
    naslov: String,
    isRead: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(10.dp),
        color = TransakcijaBoxColor
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = naslov,
                fontSize = TextUnit(20.0f, TextUnitType.Sp)
            )
            Icon(
                imageVector = if (isRead) Icons.Outlined.MarkEmailRead else Icons.Outlined.MarkEmailUnread,
                contentDescription = "ReadIndicator"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ObavijestItemPreview() {
    ObavijestItem(naslov = "Obavijest", isRead = true, {})
}