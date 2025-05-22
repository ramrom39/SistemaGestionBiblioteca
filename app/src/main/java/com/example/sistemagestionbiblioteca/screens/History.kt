package com.example.sistemagestionbiblioteca.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate

import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.sistemagestionbiblioteca.R
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.features.history.HistoryViewModel
import com.example.sistemagestionbiblioteca.navigation.BottomBar
import com.example.sistemagestionbiblioteca.navigation.CustomTopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Pantalla de historial donde el usuario puede buscar un libro,
 * ver sugerencias, seleccionar uno y consultar su historial de acciones.
 *
 * Incluye animaciones, diálogos de detalle y edición, y gestiona el estado
 * mediante un ViewModel.
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param currentUserId ID del usuario autenticado, usado para rutas parametrizadas.
 * @param viewModel     Instancia de [HistoryViewModel] para manejar la lógica y datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    navController: NavController,
    currentUserId: Int,
    viewModel: HistoryViewModel = viewModel()
) {
    val title          = "Historial"
    val searchQuery    by viewModel.searchQuery.collectAsState()
    val suggestions    by viewModel.suggestions.collectAsState()
    val selectedBook   by viewModel.selectedBook.collectAsState()
    val categoryName   by viewModel.categoryName.collectAsState()
    val historyList    by viewModel.historyList.collectAsState()
    val userNames      by viewModel.userNames.collectAsState()
    val focusManager   = LocalFocusManager.current

    // Estados para diálogos
    var showDialog     by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var expanded       by remember { mutableStateOf(false) }

    // Colores
    val orange          = Color(0xFFF8B75E)
    val cardBackground  = Color(0xFFFAB652)
    val titleBackground = orange

    val context = LocalContext.current
    // Colores de la barra de búsqueda
    val searchBarColors = TextFieldDefaults.colors(
        unfocusedTextColor      = Color(0xFF1D3A58),
        focusedTextColor        = Color(0xFF1D3A58),
        unfocusedContainerColor = Color.White,
        focusedContainerColor   = Color.White,
        disabledContainerColor  = Color.White,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor   = Color.Transparent,
        disabledIndicatorColor  = Color.Transparent,
        errorIndicatorColor     = Color.Transparent,
        cursorColor             = Color(0xFF1D3A58),
        focusedLabelColor       = Color(0xFF1D3A58),
        unfocusedLabelColor     = Color(0xFF1D3A58),
        disabledLabelColor      = Color(0xFF1D3A58)
    )

    // Animación “pop”
    val coroutineScope = rememberCoroutineScope()
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (isPressed) 1.05f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        topBar        = { CustomTopBar(navController, title) },
        bottomBar     = { BottomBar(navController, currentUserId) },
        containerColor = Color(0xFFF6E6CA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
                        center = Offset(0.5f, 0.5f), radius = 2000f
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .shadow(
                        elevation = 6.dp,
                        shape     = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    )
                    .background(
                        brush = Brush.verticalGradient(listOf(orange, orange)),
                        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.herohistorial),
                        contentDescription = "Busca tu libro favorito",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 10.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value         = searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        placeholder   = {
                            Text(
                                "Ej : El Quijote...",
                                color      = Color(0xFF1D3A58),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingIcon   = {
                            IconButton(onClick = {
                                viewModel.onSearchButtonClicked()
                                focusManager.clearFocus()
                            }) {
                                Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = Color(0xFF1D3A58))
                            }
                        },
                        colors     = searchBarColors,
                        singleLine = true,
                        modifier   = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        isPressed = true
                                        delay(300)
                                        isPressed = false
                                    }
                                } else {
                                    viewModel.clearSuggestions()
                                }
                            }
                    )

                    AnimatedVisibility(visible = suggestions.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .heightIn(max = 200.dp),
                            shape  = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            LazyColumn(modifier = Modifier.padding(vertical = 8.dp)) {
                                items(suggestions) { book ->
                                    Text(
                                        book.ID.toString() + " - "+book.Título,
                                        fontSize = 18.sp,
                                        color    = Color(0xFF1D3A58),
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.onBookSelected(book)
                                                focusManager.clearFocus()
                                            }
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                        viewModel.clearSuggestions()
                    }
            ) {
                if (selectedBook == null) {
                    EmptyHistoryAnimation(modifier = Modifier.align(Alignment.Center))
                }
                Column {
                    selectedBook?.let { book ->
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                // Solo sombra cuando está expandido
                                .then(if (expanded) Modifier.shadow(8.dp, RoundedCornerShape(12.dp)) else Modifier)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .animateContentSize()
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { expanded = !expanded }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(book.Título, fontWeight = FontWeight.Bold,color= Color(0xFF1D3A58),fontSize = 20.sp)
                                        Spacer(Modifier.height(12.dp))
                                        Text(
                                            book.Sinopsis,
                                            maxLines = if (expanded) Int.MAX_VALUE else 2,
                                            overflow = TextOverflow.Ellipsis,
                                            color      = Color(0xFF1D3A58),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    val rotation by animateFloatAsState(if (expanded) 90f else 0f)
                                    Icon(
                                        Icons.Filled.KeyboardArrowRight,
                                        contentDescription = if (expanded) "Colapsar" else "Expandir",
                                        modifier = Modifier.rotate(rotation)
                                    )
                                }

                                AnimatedVisibility(visible = expanded) {
                                    Column {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)

                                        ) {
                                            Button(
                                                onClick = { showDialog = true },
                                                modifier = Modifier.weight(1f),
                                                colors   = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFFF6B459),
                                                    contentColor   = Color.White
                                                )
                                            ) {
                                                Text("Ver detalles", color = Color.White)
                                            }
                                            Button(
                                                onClick = { showEditDialog = true },
                                                modifier = Modifier.weight(1f),
                                                colors   = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFFF6B459),
                                                    contentColor   = Color.White
                                                )
                                            ) {
                                                Text("Editar",color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (showDialog) {
                            Dialog(
                                onDismissRequest = {},
                                properties = DialogProperties(
                                    dismissOnBackPress    = false,
                                    dismissOnClickOutside = false
                                )
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .background(cardBackground, RoundedCornerShape(12.dp))
                                ) {

                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .height(36.dp)
                                            .background(
                                                titleBackground,
                                                RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                            ),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        IconButton(
                                            onClick = { showDialog = false },
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.ArrowBack,
                                                contentDescription = "Volver",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                    Box(
                                        Modifier
                                            .fillMaxWidth()
                                            .background(titleBackground)
                                            .padding(vertical = 12.dp, horizontal = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = book.Título,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    // Contenido
                                    Column(Modifier.padding(16.dp)) {
                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    Color.White.copy(alpha = 0.2f),
                                                    RoundedCornerShape(16.dp)
                                                )
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                book.Autor,
                                                fontSize  = 16.sp,
                                                color     = Color.White,
                                                textAlign = TextAlign.Center,
                                                modifier  = Modifier.fillMaxWidth()
                                            )
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 16.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            listOf(categoryName, book.Estanteria_ID.toString()).forEach { label ->
                                                Box(
                                                    Modifier
                                                        .weight(1f)
                                                        .background(
                                                            Color.White.copy(alpha = 0.2f),
                                                            RoundedCornerShape(16.dp)
                                                        )
                                                        .padding(horizontal = 12.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        label,
                                                        fontSize  = 16.sp,
                                                        color     = Color.White,
                                                        textAlign = TextAlign.Center,
                                                        modifier  = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            "Historial de acciones",
                                            fontSize   = 18.sp,
                                            fontWeight = FontWeight.Medium,
                                            color      = Color.White
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        LazyColumn(
                                            Modifier
                                                .fillMaxWidth()
                                                .heightIn(max = 300.dp)
                                                .padding(bottom = 16.dp)
                                        ) {
                                            items(historyList) { history ->
                                                Row(
                                                    Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Column {
                                                        Text(history.date, fontSize = 14.sp, color = Color.White)
                                                        Text(history.actionType, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                                                    }
                                                    Text(
                                                        userNames[history.userId] ?: "…",
                                                        fontSize  = 12.sp,
                                                        fontStyle = FontStyle.Italic,
                                                        color     = Color.White
                                                    )
                                                }
                                                Divider(color = Color.White.copy(alpha = 0.3f), thickness = 0.5.dp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (showEditDialog) {
                            BookDialogH(
                                initial   = book,
                                onConfirm = { updatedBook ->
                                    viewModel.updateBook(updatedBook, currentUserId)
                                    showEditDialog = false
                                },
                                onDismiss = { showEditDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Muestra una animación Lottie cuando no hay historial de acciones para el libro seleccionado.
 *
 * @param modifier Modificador para personalizar el layout o comportamiento del contenedor.
 */
@Composable
fun EmptyHistoryAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_history_books))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress    = progress,
        modifier    = modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(16.dp)
    )
}
/**
 * Diálogo para crear o editar un libro desde la pantalla de historial.
 *
 * Genera un formulario con campos para todos los atributos de [Book] y botones
 * para confirmar o cancelar. Valida que los campos estén completos antes de habilitar "OK".
 *
 * @param initial   Objeto [Book] con datos actuales o `null` para creación.
 * @param onConfirm Llamada de retorno que recibe el [Book] actualizado o nuevo.
 * @param onDismiss Llamada de retorno para cerrar el diálogo sin guardar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDialogH(
    initial: Book?,
    onConfirm: (Book) -> Unit,
    onDismiss: () -> Unit
) {
    var titulo     by remember { mutableStateOf(initial?.Título.orEmpty()) }
    var autor      by remember { mutableStateOf(initial?.Autor.orEmpty()) }
    var año        by remember { mutableStateOf(initial?.Año?.toString().orEmpty()) }
    var sinopsis   by remember { mutableStateOf(initial?.Sinopsis.orEmpty()) }
    var categoria  by remember { mutableStateOf(initial?.Categoría_ID?.toString().orEmpty()) }
    var estado     by remember { mutableStateOf(initial?.Estado.orEmpty()) }
    var fecha      by remember { mutableStateOf(initial?.Fecha.orEmpty()) }
    var estanteria by remember { mutableStateOf(initial?.Estanteria_ID?.toString().orEmpty()) }

    val isFormValid by remember {
        derivedStateOf {
            titulo.isNotBlank() &&
                    autor.isNotBlank() &&
                    año.toIntOrNull() != null &&
                    sinopsis.isNotBlank() &&
                    categoria.toIntOrNull() != null &&
                    estado.isNotBlank() &&
                    estanteria.toIntOrNull() != null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (initial == null) "Crear libro" else "Editar libro") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = año, onValueChange = { año = it }, label = { Text("Año") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = sinopsis, onValueChange = { sinopsis = it }, label = { Text("Sinopsis") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría_ID") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = estanteria, onValueChange = { estanteria = it }, label = { Text("Estanteria_ID") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val book = Book(
                    ID            = initial?.ID ?: 0,
                    Título        = titulo,
                    Autor         = autor,
                    Año           = año.toInt(),
                    Sinopsis      = sinopsis,
                    Categoría_ID  = categoria.toInt(),
                    Estado        = estado,
                    Fecha         = fecha,
                    Estanteria_ID = estanteria.toInt()
                )
                onConfirm(book)
            }, enabled = isFormValid) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
