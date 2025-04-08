package com.example.sistemagestionbiblioteca.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.navigation.BottomBar

@Composable
fun Home(navController: NavController) {
    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = Color(0xFFF3F3F3)

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Respeta el padding del Scaffold para que no tape la BottomBar
                .background(Color(0xFFF3F3F3)), // Fondo azul oscuro para Home
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Pantalla de Home", color = Color.Black, fontSize = 24.sp)
        }
    }
}
