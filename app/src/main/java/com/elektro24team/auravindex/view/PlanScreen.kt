package com.elektro24team.auravindex.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.theme.MediumPadding
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import com.elektro24team.auravindex.model.Plan
import com.elektro24team.auravindex.ui.components.PlanCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(navController: NavController ) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    /* List of temp plans, later we'll use the API */
    val planList = listOf(
        Plan("1", "Plan 1", 10.0f, 20.0f, 3, 15, 2),
        Plan("2", "Plan 2", 15.0f, 25.0f, 2, 10, 1),
        Plan("3", "Plan 3", 20.0f, 30.0f, 1, 5, 0),
        Plan("4", "Plan 4", 25.0f, 35.0f, 4, 20, 3),
        Plan("5", "Plan 5", 30.0f, 40.0f, 5, 25, 4)
    )


    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(onItemSelected = {
            })
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AURA VINDEX") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "plan",
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Text(
                        text = "Check out our plans",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        modifier = Modifier.padding(MediumPadding)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MediumPadding),
                    ) {
                        /*Text(
                            text = "¡PLAN SCREEN!",
                            style = MaterialTheme.typography.headlineSmall
                        )*/
                        items(planList.size) { index ->
                            PlanCard(plan = planList[index])
                        }

                    }

                }
            }
        )
    }
}
