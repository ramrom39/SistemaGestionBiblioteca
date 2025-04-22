package com.example.sistemagestionbiblioteca.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.navigation.AppScreens
import com.example.sistemagestionbiblioteca.features.users.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun Login(navController: NavController) {
    val loginVm: LoginViewModel = viewModel()
    val loginMsg by loginVm.loginResponse.observeAsState()
    val userId by loginVm.userId.observeAsState()

    // Este LaunchedEffect se disparará en cuanto cambie loginMsg o userId
    LaunchedEffect(loginMsg, userId) {
        if (loginMsg == "loginexistoso" && userId != null) {
            // Navegamos a Home pasando el userId como argumento
            navController.navigate("home/$userId") {
                popUpTo(AppScreens.Login.route) { inclusive = true }
            }
        }
    }

    val focusManager = LocalFocusManager.current

    // Campos para email y contraseña
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Obtenemos el LoginViewModel
    val loginViewModel: LoginViewModel = viewModel()

    // Observamos la respuesta y el error del login
    val loginResponse by loginViewModel.loginResponse.observeAsState()
    val errorMessage by loginViewModel.errorMessage.observeAsState()

    LaunchedEffect(loginMsg, userId) {
        if (loginMsg == "loginexistoso") {
            userId?.let { uid ->
                navController.navigate(AppScreens.Home.createRoute(uid)) {
                    popUpTo(AppScreens.Login.route) { inclusive = true }
                }
            }
        }
    }

    // Fondo con degradado
    val gradientBrush = Brush.radialGradient(
        colors = listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
        center = Offset(0.5f, 0.5f),
        radius = 2000f
    )

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
                    .padding(20.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Iniciar Sesión", fontSize = 30.sp, color = Color(0xFFDC993F))
                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo o usuario", color = Color(0xFF886742)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color(0xFF886742)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Color(0xFFF6B459)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        // Llamamos al método del ViewModel para iniciar sesión
                        loginViewModel.loginUser(email, password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6B459),
                        contentColor = Color(0xFF886742)
                    )
                ) {
                    Text("Iniciar sesión")
                }

                Spacer(modifier = Modifier.height(20.dp))

                TextButton(
                    onClick = { navController.navigate(AppScreens.Register.route) }
                ) {
                    Text("¿No tienes cuenta? Regístrate", color = Color(0xFFDC993F))
                }

                // Muestra error, si lo hay
                if (!errorMessage.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = errorMessage!!, color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    unfocusedTextColor = Color(0xFFFAA634),
    focusedTextColor = Color(0xFFFAA634),
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    focusedIndicatorColor = Color(0xFFFAA634),
    unfocusedIndicatorColor = Color(0xFFFAA634),
    cursorColor = Color(0xFFFAA634),
    focusedLabelColor = Color.Black,
    unfocusedLabelColor = Color.Black
)
