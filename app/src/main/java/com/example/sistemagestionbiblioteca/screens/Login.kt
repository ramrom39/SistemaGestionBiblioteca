package com.example.sistemagestionbiblioteca.screens

/**
 * Pantalla de inicio de sesión donde el usuario ingresa sus credenciales.
 *
 * Muestra campos para correo/usuario y contraseña (con opción de mostrar/ocultar),
 * un botón para iniciar sesión y navega a Home en caso de éxito.
 * Gestiona estados de carga y muestra animaciones Lottie mientras espera respuesta.
 *
 * @param navController Controlador de navegación para mover entre pantallas.
 */
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sistemagestionbiblioteca.R
@Composable
fun Login(navController: NavController) {
    val loginVm       : LoginViewModel = viewModel()
    val loginMsg      by loginVm.loginResponse.observeAsState()
    val errorMessage  by loginVm.errorMessage.observeAsState()
    val userId        by loginVm.userId.observeAsState()
    val focusManager  = LocalFocusManager.current
    val context       = LocalContext.current

    var email        by remember { mutableStateOf("") }
    var password     by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading    by remember { mutableStateOf(false) }



    val loadComp by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val loadProg by animateLottieCompositionAsState(loadComp, iterations = LottieConstants.IterateForever)


    LaunchedEffect(loginMsg, errorMessage) {
        if (!isLoading) return@LaunchedEffect

        when {
            loginMsg == "loginexistoso" && userId != null -> {
                isLoading = false
                navController.navigate(AppScreens.Home.createRoute(userId!!)) {
                    popUpTo(AppScreens.Login.route) { inclusive = true }
                }
            }
            !errorMessage.isNullOrBlank() -> {
                isLoading = false
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } },
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter            = painterResource(R.drawable.orange_white_background),
                contentDescription = null,
                modifier           = Modifier.matchParentSize(),
                contentScale       = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier            = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Iniciar Sesión", fontSize = 30.sp, color = Color(0xFFDC993F))
                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = { Text("Correo o usuario", fontWeight = FontWeight.Bold) },
                        singleLine      = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier        = Modifier.fillMaxWidth(),
                        colors          = textFieldColors()
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value                = password,
                        onValueChange        = { password = it },
                        label                = { Text("Contraseña", fontWeight = FontWeight.Bold) },
                        singleLine           = true,
                        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon         = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (showPassword)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors   = textFieldColors()
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                Toast.makeText(context, "Faltan datos de autenticación", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                loginVm.loginUser(email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF6B459),
                            contentColor   = Color.White
                        )
                    ) {
                        Text("Iniciar Sesión", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(onClick = { navController.navigate(AppScreens.Register.route) }) {
                        Text("¿No tienes cuenta? Regístrate")
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier         = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(composition = loadComp, progress = loadProg, modifier = Modifier.size(120.dp))
                }
            }
        }
    }
}

/**
 * Colores personalizados para los TextField de la pantalla de Login.
 *
 * Define los colores para texto, indicadores y cursor en estados enfocado y no enfocado.
 *
 * @return Un objeto [TextFieldColors] con la configuración deseada.
 */
@Composable
fun textFieldColors() = TextFieldDefaults.colors(
    unfocusedTextColor      = Color(0xFFDC993F),
    focusedTextColor        = Color(0xFFDC993F),
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor   = Color.Transparent,
    focusedIndicatorColor   = Color(0xFFFAA634),
    unfocusedIndicatorColor = Color(0xFFFAA634),
    cursorColor             = Color(0xFFFAA634),
    focusedLabelColor       = Color(0xFFDC993F),
    unfocusedLabelColor     = Color(0xFFDC993F)
)
