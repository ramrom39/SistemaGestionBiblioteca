package com.example.sistemagestionbiblioteca.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(navController: NavController,title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8B75E) // Color de fondo, ajusta a tu estilo
        ,
    ) {
        TopAppBar(
            title = { Text(text = title, color = Color.White) },
            actions = {
                TextButton(
                    onClick = {
                        // Navega a Login y limpia el back stack de Home para cerrar sesión
                        navController.navigate(AppScreens.Login.route) {
                            popUpTo(AppScreens.Home.route) { inclusive = true }
                        }
                    }
                ) {
                    Text("Cerrar Sesión", color = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent // El color viene del Surface
            )
        )
    }
}