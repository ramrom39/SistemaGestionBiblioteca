package com.example.sistemagestionbiblioteca.screens

import android.support.annotation.DrawableRes
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.R
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.features.books.BookViewModel
import com.example.sistemagestionbiblioteca.features.category.CategoryViewModel
import com.example.sistemagestionbiblioteca.navigation.BottomBar
import com.example.sistemagestionbiblioteca.navigation.CustomTopBar
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.sistemagestionbiblioteca.data.books.BookCreateRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController,currentUserId: Int) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scroll = rememberScrollState()
    val categoryBgRes = R.drawable.fotocategoria
    // ViewModels
    val catVm: CategoryViewModel = viewModel()
    val categories by catVm.categories.observeAsState(emptyList())
    val catError  by catVm.error.observeAsState()

    var showCreateBookDialog by remember { mutableStateOf(false) }

    val bookVm: BookViewModel = viewModel()
    val books by bookVm.books.observeAsState(emptyList())

    var showEditBookDialog by remember { mutableStateOf(false) }
    var bookToEdit        by remember { mutableStateOf<Book?>(null) }
    // Estados para creación de categoría

    var showCreateCat by remember { mutableStateOf(false) }
    var newCatName   by remember { mutableStateOf("") }
    var newCatDesc   by remember { mutableStateOf("") }

    val isCreateCatEnabled by remember(newCatName, newCatDesc) {
        derivedStateOf { newCatName.isNotBlank() && newCatDesc.isNotBlank() }
    }
    val statusMsg by bookVm.statusMessage.observeAsState()

    val catStatusMsg by catVm.statusMessage.observeAsState()

    // Toast para categoría
    LaunchedEffect(catStatusMsg) {
        catStatusMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            catVm.clearStatusMessage()
        }
    }
    // Toast cada vez que cambie statusMsg
    LaunchedEffect(statusMsg) {
        statusMsg?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            bookVm.clearStatusMessage()
        }
    }
    // Carga inicial
    LaunchedEffect(Unit) {

        catVm.fetchCategories()
        bookVm.fetchBooks()
    }

    Scaffold(
        topBar        = { CustomTopBar(navController) },
        bottomBar = { BottomBar(navController, currentUserId) },
        containerColor = Color(0xFFF6E6CA)
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scroll)       // <-- esto habilita el scroll
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
                        center = Offset(0.5f, 0.5f), radius = 2000f
                    )
                )
                .pointerInput(Unit){ detectTapGestures { focusManager.clearFocus() } }
        ) {
            Column {
                Spacer(Modifier.height(40.dp))
                Image(
                    painter = painterResource(R.drawable.hero),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .offset(x = (-12).dp)
                )
                Spacer(Modifier.height(40.dp))

                Column(Modifier.padding(16.dp)) {
                    // --- HEADER CATEGORÍAS ---
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Categorías",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D3A58)
                        )
                        IconButton(
                            onClick = { showCreateCat = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF8B75E), CircleShape)
                                .clip(CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Crear categoría", tint = Color.White)
                        }
                    }
                    Divider(
                        color = Color(0xFF1D3A58),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.60f)
                            .padding(vertical = 8.dp)
                    )
                    Spacer(Modifier.height(30.dp))
                    // DIÁLOGO CREAR CATEGORÍA
                    if (showCreateCat) {
                        AlertDialog(
                            onDismissRequest = { showCreateCat = false },
                            title = { Text("Crear categoría") },
                            text = {
                                Column {
                                    OutlinedTextField(
                                        value = newCatName,
                                        onValueChange = { newCatName = it },
                                        label = { Text("Nombre") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = newCatDesc,
                                        onValueChange = { newCatDesc = it },
                                        label = { Text("Descripción") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        catVm.createCategory(
                                            nombre      = newCatName,
                                            descripcion = newCatDesc.ifBlank { null }
                                        )
                                        newCatName = ""
                                        newCatDesc = ""
                                        showCreateCat = false
                                    },
                                    enabled = isCreateCatEnabled        // <— aquí
                                ) {
                                    Text("OK")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showCreateCat = false }) { Text("Cancelar") }
                            }
                        )
                        Spacer(Modifier.height(25.dp))
                    }

                    // --- LAZYROW CATEGORÍAS ---
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = categories,
                            key = { it.id }    // ← key única por categoría
                        ) { cat ->
                            CategoryCard(
                                category      = cat,
                                backgroundRes = categoryBgRes,
                                onDelete      = {
                                    // Debug: verifica que el onClick llega
                                    Toast.makeText(context, "Borrando “${cat.nombre}”", Toast.LENGTH_SHORT).show()
                                    catVm.deleteCategory(cat.id)
                                },
                                onUpdate      = { n, d ->
                                    catVm.updateCategory(cat.id, n, d)
                                }
                            )
                        }
                    }
                    catError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = Color.Red)
                    }
                    Spacer(Modifier.height(50.dp))
                    Image(
                        painter = painterResource(R.drawable.fotonovedades),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(vertical = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    // --- NOVEDEDADES (LIBROS) ---
                    Spacer(Modifier.height(24.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Novedades",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D3A58)
                        )
                        IconButton(
                            onClick = { showCreateBookDialog = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF8B75E), CircleShape)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Crear libro",
                                tint = Color.White
                            )
                        }
                    }
                    Divider(
                        color = Color(0xFF1D3A58),
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.46f)
                            .padding(vertical = 8.dp)
                    )
                    if (showCreateBookDialog) {
                        BookDialog(
                            initial = null,
                            onConfirm = { b ->
                                val req = BookCreateRequest(
                                    titulo = b.Título,
                                    autor = b.Autor,
                                    año = b.Año,
                                    sinopsis = b.Sinopsis,
                                    categoria = b.Categoría_ID,
                                    estado = b.Estado,
                                    fecha = b.Fecha,
                                    estanteria = b.Estanteria_ID
                                )
                                bookVm.createBook(req)
                                showCreateBookDialog = false
                            },
                            onDismiss = { showCreateBookDialog = false }
                        )
                    }
                    LazyRow {
                        items(books.reversed()) { book ->
                            BookCard(
                                book = book,
                                onDelete = { bookVm.deleteBook(book.ID) },
                                onEdit   = {
                                    bookToEdit = book
                                    showEditBookDialog = true }
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }

                    if (showEditBookDialog && bookToEdit != null) {
                        BookDialog(
                            initial = bookToEdit!!,
                            onConfirm = { updatedBook ->
                                bookVm.updateBook(updatedBook, currentUserId)
                                showEditBookDialog = false
                                bookToEdit = null
                            },
                            onDismiss = { showEditBookDialog = false; bookToEdit = null }
                        )
                    }

                }
            }
        }
    }
}


@Composable
fun CategoryCard(
    category: Category,
    @DrawableRes backgroundRes: Int,
    onDelete: () -> Unit,
    onUpdate: (String, String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(category.nombre) }
    var editDesc by remember { mutableStateOf(category.descripcion.orEmpty()) }

    Card(
        modifier = Modifier
            .width(170.dp)
            .height(250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Fondo de la categoría
            Image(
                painter = painterResource(backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp))
            )

            // Pequeño contenedor en la parte inferior para título y descripción
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = category.nombre,
                        color = Color(0xFF1D3A58),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = category.descripcion.orEmpty(),
                        color = Color(0xFF1D3A58).copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botones de acción en esquina superior derecha
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Red.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.White)
                }
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Blue.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(Icons.Default.Build, contentDescription = "Editar", tint = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Actualizar categoría") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nombre") },
                        isError = editName.isBlank(),
                        supportingText = { if (editName.isBlank()) Text("Requerido") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDesc,
                        onValueChange = { editDesc = it },
                        label = { Text("Descripción") },
                        isError = editDesc.isBlank(),
                        supportingText = { if (editDesc.isBlank()) Text("Requerido") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onUpdate(editName, editDesc.ifBlank { null })
                        showDialog = false
                    },
                    enabled = editName.isNotBlank() && editDesc.isNotBlank()
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun BookCard(
    book: Book,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp) // Espacio exterior para sombra
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .width(250.dp)
            .height(300.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo ocupando toda la carta
            Image(
                painter = rememberAsyncImagePainter(R.drawable.librocerrado),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            )

            // Etiqueta blanca en la parte inferior
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = book.Título,
                        color = Color(0xFF1D3A58),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.Autor,
                        color = Color(0xFF1D3A58).copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Botones arriba a la derecha
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Red.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.White)
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Blue.copy(alpha = 0.6f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(Icons.Default.EditNote, contentDescription = "Editar", tint = Color.White)
                }
            }
        }
    }
}
@Composable
private fun BookDialog(
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

    val isFormValid by remember(
        titulo, autor, año, sinopsis,
        categoria, estado, fecha, estanteria
    ) {
        derivedStateOf {
            titulo.isNotBlank() &&
                    autor.isNotBlank() &&
                    año.isNotBlank() &&
                    sinopsis.isNotBlank() &&
                    categoria.isNotBlank() &&
                    estado.isNotBlank() &&
                    fecha.isNotBlank() &&
                    estanteria.isNotBlank()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Crear libro" else "Editar libro") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = año,
                    onValueChange = { año = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = sinopsis,
                    onValueChange = { sinopsis = it },
                    label = { Text("Sinopsis") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría_ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = estanteria,
                    onValueChange = { estanteria = it },
                    label = { Text("Estanteria_ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        Book(
                            ID = initial?.ID ?: 0,
                            Título       = titulo,
                            Autor        = autor,
                            Año          = año.toIntOrNull() ?: 0,
                            Sinopsis     = sinopsis,
                            Categoría_ID = categoria.toIntOrNull() ?: 0,
                            Estado       = estado,
                            Fecha        = fecha,
                            Estanteria_ID= estanteria.toIntOrNull() ?: 0
                        )
                    )
                },
                enabled = isFormValid
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
