package com.example.sistemagestionbiblioteca.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
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
    var correoElectronico by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }

    // Variable de estado para controlar si se deben mostrar errores en la UI
    var showErrors by remember { mutableStateOf(false) }

    // Se observa el LiveData del ViewModel para capturar la respuesta o el error
    val registerResponse by registerViewModel.registerResponse.observeAsState()
    val errorMessage by registerViewModel.errorMessage.observeAsState()

    // Navegar a Login si el registro es exitoso
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
                // Campo para Nombre con asterisco condicional
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = {
                        Text(
                            buildAnnotatedString {
                                append("Nombre")
                                if (showErrors && nombre.isEmpty()) {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.Red)) {
                                        append(" *")
                                    }
                                }
                            },
                            color = Color(0xFFDC993F)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Apellidos con asterisco condicional
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = {
                        Text(
                            buildAnnotatedString {
                                append("Nombre")
                                if (showErrors && apellidos.isEmpty()) {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.Red)) {
                                        append(" *")
                                    }
                                }
                            },
                            color = Color(0xFFDC993F)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Correo Electrónico con trailingIcon condicional
                OutlinedTextField(
                    value = correoElectronico,
                    onValueChange = { correoElectronico = it },
                    label = {
                        Text(
                            buildAnnotatedString {
                                append("Nombre")
                                if (showErrors && correoElectronico.isEmpty()) {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.Red)) {
                                        append(" *")
                                    }
                                }
                            },
                            color = Color(0xFFDC993F)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    colors = textFieldColors(),
                    trailingIcon = {
                        if (correoElectronico.isNotEmpty()) {
                            if (validarEmail(correoElectronico)) {
                                Icon(
                                    imageVector = Icons.Default.GppGood,
                                    contentDescription = "Email válido",
                                    tint = Color.Green
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Email inválido",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                )
                // Mensaje de error debajo del campo si el email es inválido.
                if (correoElectronico.isNotEmpty() && !validarEmail(correoElectronico)) {
                    Text(
                        text = "Correo electrónico no válido",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Usuario con asterisco condicional y trailingIcon para duplicidad
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        // Opcional: aquí podrías limpiar el error si se reescribe el campo.
                    },
                    label = {
                        Text(
                            buildAnnotatedString {
                                append("Nombre")
                                if (showErrors && usuario.isEmpty()) {
                                    withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.Red)) {
                                        append(" *")
                                    }
                                }
                            },
                            color = Color(0xFFDC993F)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors(),
                    trailingIcon = {
                        if (usuario.isNotEmpty()) {
                            if (errorMessage?.contains("ya existe", ignoreCase = true) == true) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Usuario duplicado",
                                    tint = Color.Red
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Usuario disponible",
                                    tint = Color.Green
                                )
                            }
                        }
                    }
                )
                // Mensaje de error debajo del campo si el usuario ya existe.
                if (usuario.isNotEmpty() && errorMessage?.contains("ya existe", ignoreCase = true) == true) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo para Contraseña con asterisco condicional y funcionalidades adicionales.
                PasswordTextField(
                    password = contraseña,
                    onPasswordChange = { contraseña = it },
                    onGeneratePassword = { contraseña = generateRandomPassword(12) },
                    showError = showErrors && contraseña.isEmpty() // Pasamos el flag para indicar error.
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de Registro: valida que ninguno de los campos esté vacío.
                Button(
                    onClick = {
                        // Si alguno de los campos está vacío, activamos showErrors.
                        if (nombre.isEmpty() || apellidos.isEmpty() ||
                            usuario.isEmpty() || correoElectronico.isEmpty() || contraseña.isEmpty()
                        ) {
                            showErrors = true
                        } else if (!validarEmail(correoElectronico)) {
                            showErrors = true
                        } else {
                            // Si todos los campos están llenos y el email es válido, se procede al registro
                            showErrors = false
                            val user = UserRegister(
                                nombre = nombre,
                                apellidos = apellidos,
                                usuario = usuario,
                                contraseña = contraseña,
                                correoElectronico = correoElectronico
                            )
                            registerViewModel.registerUser(user)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Registrarse", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Composable para el campo de contraseña con funcionalidades:
 * - Alternar entre ver/ocultar contraseña.
 * - Copiar contraseña al portapapeles.
 * - Generar una contraseña aleatoria (activo sólo si el campo está vacío).
 * Además, muestra un asterisco en el label si el campo está vacío y se ha intentado enviar.
 */
@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    onGeneratePassword: () -> Unit,
    showError: Boolean
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(
                buildAnnotatedString {
                    append("Nombre")
                    if (showError && password.isEmpty()) {
                        withStyle(style = androidx.compose.ui.text.SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    }
                },
                color = Color(0xFFDC993F)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        colors = textFieldColors(),
        trailingIcon = {
            Row {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = Color(0xFFDC993F)
                    )
                }
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(password))
                        Toast.makeText(context, "Contraseña copiada", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar contraseña",
                        tint = Color(0xFFDC993F)
                    )
                }
                IconButton(onClick = { if (password.isEmpty()) onGeneratePassword() }) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = "Generar nueva contraseña",
                        tint = Color(0xFFDC993F)
                    )
                }
            }
        }
    )
}

/**
 * Función para generar una contraseña aleatoria.
 */
fun generateRandomPassword(length: Int = 12): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#%^&*()"
    return (1..length)
        .map { characters.random() }
        .joinToString("")
}

/**
 * Función para validar el formato de un email.
 */
fun validarEmail(email: String): Boolean {
    val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return email.matches(regex)
}

