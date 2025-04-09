package com.example.sistemagestionbiblioteca.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.navigation.AppScreens
import com.example.sistemagestionbiblioteca.viewmodel.RegisterViewModel
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.delay

@Composable
fun Register(navController: NavController) {
    val focusManager = LocalFocusManager.current
    val gradientBrush = Brush.radialGradient(
        colors = listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
        center = Offset(0.5f, 0.5f),
        radius = 2000f
    )

    // Instanciar el ViewModel
    val usersViewModel: RegisterViewModel = viewModel()

    // Estados para los campos del formulario:
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Observar los LiveData del ViewModel para recibir la respuesta y errores
    val registerResponse by usersViewModel.registerResponse.observeAsState()
    val errorMessage by usersViewModel.errorMessage.observeAsState()

    // Una vez que se reciba la respuesta exitosa, navegar a Home.
    LaunchedEffect(registerResponse) {
        registerResponse?.let {
            // Opcional: puedes agregar un pequeño delay para mostrar algún feedback.
            delay(500)
            navController.navigate(AppScreens.Login.route)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(gradientBrush)
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Campo para Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Apellidos
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Usuario
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Contraseña
                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botón de Registro
                Button(
                    onClick = {
                        // Validar que ningún campo esté vacío
                        if (nombre.isNotEmpty() && apellidos.isNotEmpty() && usuario.isNotEmpty() && contraseña.isNotEmpty()) {
                            val user = UserRegister(
                                nombre = nombre,
                                apellidos = apellidos,
                                usuario = usuario,
                                contraseña = contraseña
                            )
                            // Llamar al método del ViewModel para registrar el usuario.
                            usersViewModel.viewmodelUsers(user)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrarse", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje de error en caso de fallo
                errorMessage?.let { error ->
                    Text(text = "Error: $error", color = Color.Red)
                }
            }
        }
    }
}
