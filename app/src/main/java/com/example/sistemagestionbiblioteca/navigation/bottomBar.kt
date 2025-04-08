package com.example.sistemagestionbiblioteca.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {
    // Observa los cambios en la navegación.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Rutas en las que se muestra la BottomBar.
    val visibleRoutes = listOf(
        AppScreens.Home.route,
        AppScreens.History.route,
        AppScreens.Shelves.route
    )
    if (currentRoute !in visibleRoutes) return

    // Gradiente vertical para el fondo de la BottomBar:
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE3B47B),  // Color superior
            Color(0xFFF8B75E)   // Color inferior
        )
    )

    // Envolvemos la BottomAppBar en una Surface con esquinas redondeadas.
    androidx.compose.material3.Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        BottomAppBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    // Convertimos 16.dp a pixeles para el radio de esquina.
                    val radius = 16.dp.toPx()
                    drawRoundRect(
                        brush = gradientBrush,
                        cornerRadius = CornerRadius(radius, radius)
                    )
                }
                .height(70.dp)
                .navigationBarsPadding(),
            contentColor = Color.White
        ) {
            // Ítem para Home.
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Inicio",
                            modifier = Modifier.size(36.dp)
                        )
                        if (currentRoute == AppScreens.Home.route) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(2.dp)
                                    .background(
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }
                },
                selected = currentRoute == AppScreens.Home.route,
                onClick = {
                    if (currentRoute != AppScreens.Home.route) {
                        navController.navigate(AppScreens.Home.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFFFFF),
                    unselectedIconColor = Color(0xFFFDFCFC),
                    indicatorColor = Color.Transparent
                )
            )

            // Ítem para Historial.
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = "Historial",
                            modifier = Modifier.size(36.dp)
                        )
                        if (currentRoute == AppScreens.History.route) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(2.dp)
                                    .background(
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }
                },
                selected = currentRoute == AppScreens.History.route,
                onClick = {
                    if (currentRoute != AppScreens.History.route) {
                        navController.navigate(AppScreens.History.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFFFFF),
                    unselectedIconColor = Color(0xFFFAEEE7),
                    indicatorColor = Color.Transparent
                )
            )

            // Ítem para Biblioteca.
            NavigationBarItem(
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "Biblioteca",
                            modifier = Modifier.size(36.dp)
                        )
                        if (currentRoute == AppScreens.Shelves.route) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(2.dp)
                                    .background(
                                        color = Color(0xFFFFFFFF),
                                        shape = RoundedCornerShape(1.dp)
                                    )
                            )
                        }
                    }
                },
                selected = currentRoute == AppScreens.Shelves.route,
                onClick = {
                    if (currentRoute != AppScreens.Shelves.route) {
                        navController.navigate(AppScreens.Shelves.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFFFFF),
                    unselectedIconColor = Color(0xFFFAEEE7),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
