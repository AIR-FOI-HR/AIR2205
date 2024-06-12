package hr.foi.air.mbankingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import hr.foi.air.mbankingapp.data.context.Theme
import hr.foi.air.mbankingapp.ui.navigation.RootNavigation
import hr.foi.air.mbankingapp.ui.theme.BoxColor
import hr.foi.air.mbankingapp.ui.theme.MBankingAppTheme
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.theme.TransakcijaBoxColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme by Theme.isDarkTheme;

            if (darkTheme) {
                Primary = Color(0xFF045578)
                BoxColor = Color(0xFF045578)
                TransakcijaBoxColor = Color(0xFF045578)
            } else {
                Primary = Color(0xFF0099DA)
                BoxColor = Color(0xFF87CBE0)
                TransakcijaBoxColor = Color(0xFFD6EEF9)
            }

            MBankingAppTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigation(navController = rememberNavController())
                }
            }
        }
    }
}
