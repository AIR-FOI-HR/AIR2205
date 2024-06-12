package hr.foi.air.mbankingapp.ui.views.Home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import hr.foi.air.mbankingapp.data.context.Theme
import hr.foi.air.mbankingapp.ui.navigation.HomeNavigation
import hr.foi.air.mbankingapp.ui.theme.Primary
import hr.foi.air.mbankingapp.ui.theme.UnselectedColor
import hr.foi.air.mbankingapp.ui.viewmodels.ObavijestiViewModel

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val navigation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRootView(
    navControllerRoot: NavHostController,
    onNavigateToObavijest: (String) -> Unit,
    onNavigateToSveObavijesti: () -> Unit,
    navController: NavHostController = rememberNavController(),
    obavijestiViewModel: ObavijestiViewModel = viewModel()
) {
    val bottomBarItems = listOf<BottomNavigationItem>(
        BottomNavigationItem("Početna", Icons.Outlined.Home, "home"),
        BottomNavigationItem("Transakcije", Icons.Outlined.List, "transakcije"),
        BottomNavigationItem("Bankomati", Icons.Outlined.Place, "bankomati"),
        BottomNavigationItem("Postavke", Icons.Outlined.Settings, "postavke"),
    )

    val obavijesti by obavijestiViewModel.obavijesti.observeAsState();
    var focusManager = LocalFocusManager.current;

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    var obavijestiExpanded by remember { mutableStateOf(false) };

    LaunchedEffect(Unit) {
        obavijestiViewModel.fetchObavijesti()
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
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
                actions = {
                    IconButton(onClick = { obavijestiExpanded = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Obavijesti",
                            tint = Color.White
                        )
                        DropdownMenu(
                            expanded = obavijestiExpanded,
                            onDismissRequest = { obavijestiExpanded = false }
                        ) {
                            if (obavijesti?.isEmpty() != true) {
                                var brojac = 1;
                                for (obavijest in obavijesti!!) {
                                    if (brojac > 3) break;
                                    DropdownMenuItem(
                                        modifier = Modifier.width(200.dp),
                                        text = {
                                            Text(
                                                text = obavijest.naslov,
                                                fontWeight = if (obavijest.procitano != true) FontWeight.Bold else FontWeight.Normal
                                            )
                                       },
                                        onClick = {
                                            if (obavijest.procitano != true) {
                                                obavijest.id?.let {
                                                    obavijestiViewModel.procitajObavijest(it)
                                                }
                                            }
                                            obavijest.id?.let {
                                                onNavigateToObavijest(it.toString())
                                            }
                                        },
                                        trailingIcon = {
                                            if (obavijest.procitano != true) {
                                                Icon(
                                                    imageVector = Icons.Outlined.MarkEmailUnread,
                                                    contentDescription = "Nepročitano"
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Outlined.MarkEmailRead,
                                                    contentDescription = "Pročitano"
                                                )
                                            }
                                        }
                                    )
                                    brojac++;
                                }
                                Divider()
                                DropdownMenuItem(
                                    text = { Text("Prikaži više",
                                        fontWeight = FontWeight.Bold, color = Primary) },
                                    onClick = { onNavigateToSveObavijesti() }
                                )
                            } else {
                                Text("Nisu pronađene obavijesti!");
                            }
                        }
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
                containerColor = if (Theme.isDarkTheme.value) Primary else Primary,
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