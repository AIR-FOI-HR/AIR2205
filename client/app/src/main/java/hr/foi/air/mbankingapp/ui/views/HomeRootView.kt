package hr.foi.air.mbankingapp.ui.views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.mbankingapp.ui.navigation.HomeNavigation
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.theme.UnselectedColor

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val navigation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootView(
    navControllerRoot: NavHostController,
    navController: NavHostController = rememberNavController()
) {
    val bottomBarItems = listOf<BottomNavigationItem>(
        BottomNavigationItem("Početna", Icons.Outlined.Home, "home"),
        BottomNavigationItem("Transakcije", Icons.Outlined.List, "transakcije"),
        BottomNavigationItem("Bankomati", Icons.Outlined.Place, "bankomati"),
        BottomNavigationItem("Postavke", Icons.Outlined.Settings, "postavke"),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("mBanking", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Obavijesti",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            navControllerRoot.navigate("auth") {
                                popUpTo("home_root") {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ExitToApp,
                            contentDescription = "Odjava",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Primary,
            ) {
                bottomBarItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Primary,
                            unselectedIconColor = Primary,
                            selectedTextColor = Primary,
                            indicatorColor = Primary
                        ),
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index;
                            navController.navigate(item.navigation);
                        },
                        label = { Text(item.title, color = if (index == selectedItemIndex) Color.White else UnselectedColor) },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (index == selectedItemIndex) Color.White else UnselectedColor
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeNavigation(
            innerPadding = innerPadding,
            navController = navController,
            navControllerRoot = navControllerRoot
        )
    }
}