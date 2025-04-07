package com.example.sistemagestionbiblioteca.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBar(navController: NavController) {
    // Obtenemos la ruta actual usando currentBackStackEntryAsState para reactividad
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Definimos las rutas en las que se muestra la BottomBar
    val visibleRoutes = listOf(
        AppScreens.Home.route,
        AppScreens.Shelves.route,
        AppScreens.History.route
    )

    if (currentRoute !in visibleRoutes) return

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF67A867), // Verde claro
            Color(0xFF5FA85F)  // Verde oscuro
        )
    )

    BottomAppBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind { drawRect(gradientBrush) }
            .height(54.dp)
            .navigationBarsPadding(), // Agrega padding para evitar solapamiento con la barra del sistema
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Inicio",
                    modifier = Modifier.size(30.dp)
                )
            },
            selected = currentRoute == AppScreens.Home.route,
            onClick = {
                if (currentRoute != AppScreens.Home.route) {
                    navController.navigate(AppScreens.Home.route)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFCBEA7B),
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Historial",
                    modifier = Modifier.size(30.dp)
                )
            },
            selected = currentRoute == AppScreens.History.route,
            onClick = {
                if (currentRoute != AppScreens.History.route) {
                    navController.navigate(AppScreens.History.route)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFCBEA7B),
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Estanterías",
                    modifier = Modifier.size(30.dp)
                )
            },
            selected = currentRoute == AppScreens.Shelves.route,
            onClick = {
                if (currentRoute != AppScreens.Shelves.route) {
                    navController.navigate(AppScreens.Shelves.route)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFFCBEA7B),
                unselectedIconColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}
