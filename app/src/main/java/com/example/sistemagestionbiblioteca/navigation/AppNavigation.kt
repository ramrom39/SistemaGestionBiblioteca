package com.example.sistemagestionbiblioteca.navigation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.sistemagestionbiblioteca.screens.Login
import com.example.sistemagestionbiblioteca.screens.Register
import com.example.sistemagestionbiblioteca.screens.Home
import com.example.sistemagestionbiblioteca.screens.Shelves
import com.example.sistemagestionbiblioteca.screens.History



import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.height
import androidx.compose.ui.draw.drawBehind

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route,
        modifier = Modifier.background(Color(0xFF002A40))
    ) {
        composable(AppScreens.Login.route) {
            Login(navController)
        }

        composable(AppScreens.Register.route) {
            Register(navController)
        }

        composable(AppScreens.Home.route) {
            Home(navController)
        }

        composable(AppScreens.History.route) {
            History(navController)
        }

        composable(AppScreens.Shelves.route) {
            Shelves(navController)
        }

    }


    @Composable
    fun BottomBar(navController: NavController) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        val visibleRoutes = listOf(
            AppScreens.Home.route,
            AppScreens.History.route,
            AppScreens.Shelves.route
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
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind { drawRect(gradientBrush) }
                .height(54.dp)
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

}
