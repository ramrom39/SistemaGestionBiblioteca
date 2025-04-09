package com.example.sistemagestionbiblioteca.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.navigation.AppScreens
import com.example.sistemagestionbiblioteca.viewmodel.RegisterViewModel
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
    val registerViewModel: RegisterViewModel = viewModel()

    // Estados de los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Se observa el LiveData del ViewModel para capturar la respuesta o el error
    val registerResponse by registerViewModel.registerResponse.observeAsState()
    val errorMessage by registerViewModel.errorMessage.observeAsState()

    // Si se recibe una respuesta exitosa (por ejemplo, "Registro exitoso") navega a Login,
    // de lo contrario, si hay error (como el 409 que indica que el usuario ya existe)
    // se mostrará el mensaje y se quedará en el registro.
    LaunchedEffect(registerResponse) {
        registerResponse?.let {
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
                        // Validar que no estén vacíos los campos necesarios
                        if (nombre.isNotEmpty() && apellidos.isNotEmpty() && usuario.isNotEmpty() && contraseña.isNotEmpty()) {
                            val user = UserRegister(
                                nombre = nombre,
                                apellidos = apellidos,
                                usuario = usuario,
                                contraseña = contraseña
                            )
                            // Invoca el método del ViewModel para registrar al usuario.
                            registerViewModel.registerUser(user)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrarse", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar mensaje de error en caso de que el usuario ya exista u otro error
                errorMessage?.let { error ->
                    Text(text = error, color = Color.Red)
                }
            }
        }
    }
}
