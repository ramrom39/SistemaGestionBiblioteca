package com.example.sistemagestionbiblioteca.navigation


import android.annotation.SuppressLint
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

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.draw.drawBehind
import androidx.navigation.NavType
import androidx.navigation.navArgument


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.Login.route,
    ) {
        composable(AppScreens.Login.route) {
            Login(navController)
        }
        composable(AppScreens.Register.route) {
            Register(navController)
        }
        // Home ya estaba parametrizado:
        composable(
            route = AppScreens.Home.route,               // "home/{userId}"
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { back ->
            Home(navController, back.arguments!!.getInt("userId"))
        }

        // History parametrizado
        composable(
            route = "history/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { back ->
            History(navController, back.arguments!!.getInt("userId"))
        }

        // Shelves parametrizado
        composable(
            route = "shelves/{userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
            })
        ) { back ->
            Shelves(navController, back.arguments!!.getInt("userId"))
        }
    }
}
