package com.example.sistemagestionbiblioteca.screens

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController,currentUserId: Int) {
    val focusManager = LocalFocusManager.current

    // ViewModels
    val catVm: CategoryViewModel = viewModel()
    val categories by catVm.categories.observeAsState(emptyList())
    val catError  by catVm.error.observeAsState()

    val bookVm: BookViewModel = viewModel()
    val books by bookVm.books.observeAsState(emptyList())
    val bookError by bookVm.statusMessage.observeAsState()
    var showEditBookDialog by remember { mutableStateOf(false) }
    var bookToEdit        by remember { mutableStateOf<Book?>(null) }
    // Estados para creación de categoría
    var showCreateCat by remember { mutableStateOf(false) }
    var newCatName   by remember { mutableStateOf("") }
    var newCatDesc   by remember { mutableStateOf("") }

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
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
                        center = Offset(0.5f, 0.5f), radius = 2000f
                    )
                )
                .pointerInput(Unit){ detectTapGestures { focusManager.clearFocus() } }
        ) {
            Column {
                Spacer(Modifier.height(25.dp))
                Image(
                    painter = painterResource(R.drawable.hero),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .offset(x = (-12).dp)
                )
                Spacer(Modifier.height(16.dp))

                Column(Modifier.padding(16.dp)) {
                    // --- HEADER CATEGORÍAS ---
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Categorías",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8B75E)
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
                        color = Color(0xFFF8B75E),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.46f)
                            .padding(vertical = 8.dp)
                    )

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
                                TextButton(onClick = {
                                    catVm.createCategory(
                                        nombre      = newCatName,
                                        descripcion = newCatDesc.ifBlank { null }
                                    )
                                    newCatName = ""
                                    newCatDesc = ""
                                    showCreateCat = false
                                }) { Text("OK") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showCreateCat = false }) { Text("Cancelar") }
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    // --- LAZYROW CATEGORÍAS ---
                    LazyRow {
                        itemsIndexed(categories) { idx, cat ->
                            val colors = listOf(
                                Color(0xFFB3E5FC),
                                Color(0xFFC8E6C9),
                                Color(0xFFFFF9C4),
                                Color(0xFFFFCCBC),
                                Color(0xFFD1C4E9)
                            ).map { it.copy(alpha = 0.5f) }
                            CategoryCard(
                                category = cat,
                                backgroundColor = colors[idx % colors.size],
                                onDelete = { catVm.deleteCategory(cat.id) },
                                onUpdate = { n, d -> catVm.updateCategory(cat.id, n, d) }
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }
                    catError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = Color.Red)
                    }

                    // --- NOVEDEDADES (LIBROS) ---
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Novedades",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF886742)
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(vertical = 8.dp)
                    )
                    LazyRow {
                        items(books) { book ->
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
                    bookError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}


@Composable
private fun CategoryCard(
    category: Category,
    backgroundColor: Color,
    onDelete: () -> Unit,
    onUpdate: (String, String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(category.nombre) }
    var editDesc by remember { mutableStateOf(category.descripcion.orEmpty()) }

    Card(
        modifier = Modifier
            .width(170.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(category.nombre)
            Spacer(Modifier.height(4.dp))
            Text(category.descripcion.orEmpty())
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Red.copy(alpha = 0.2f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar categoría",
                        tint = Color.Red
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.Blue.copy(alpha = 0.2f), CircleShape)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Actualizar categoría",
                        tint = Color.Blue
                    )
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
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDesc,
                        onValueChange = { editDesc = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onUpdate(editName, editDesc.ifBlank { null })
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Cancelar") }
            }
        )
    }
}



@Composable
private fun BookCard(book: Book, onDelete: ()->Unit, onEdit: ()->Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(200.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(book.Título, fontWeight = FontWeight.Bold)
            Text("Autor: ${book.Autor}")
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.background(Color.Red.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.background(Color.Blue.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(Icons.Default.EditNote, "Editar", tint = Color.Blue)
                }
            }
        }
    }
}

@Composable
private fun BookDialog(
    initial: Book?,
    onConfirm: (Book)->Unit,
    onDismiss: ()->Unit
) {
    var titulo      by remember { mutableStateOf(initial?.Título.orEmpty()) }
    var autor       by remember { mutableStateOf(initial?.Autor.orEmpty()) }
    var año         by remember { mutableStateOf(initial?.Año?.toString().orEmpty()) }
    var sinopsis    by remember { mutableStateOf(initial?.Sinopsis.orEmpty()) }
    var categoria   by remember { mutableStateOf(initial?.Categoría_ID?.toString().orEmpty()) }
    var estado      by remember { mutableStateOf(initial?.Estado.orEmpty()) }
    var fecha       by remember { mutableStateOf(initial?.Fecha.orEmpty()) }
    var estanteria  by remember { mutableStateOf(initial?.Estanteria_ID?.toString().orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial==null) "Crear libro" else "Editar libro") },
        text = {
            Column {
                OutlinedTextField(titulo,   { titulo = it   }, label = { Text("Título") })
                OutlinedTextField(autor,    { autor = it    }, label = { Text("Autor") })
                OutlinedTextField(año,      { año = it      }, label = { Text("Año") })
                OutlinedTextField(sinopsis, { sinopsis = it }, label = { Text("Sinopsis") })
                OutlinedTextField(categoria,{ categoria = it}, label = { Text("Categoría_ID") })
                OutlinedTextField(estado,   { estado = it   }, label = { Text("Estado") })
                OutlinedTextField(fecha,    { fecha = it    }, label = { Text("Fecha") })
                OutlinedTextField(estanteria,{ estanteria = it}, label = { Text("Estanteria_ID") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
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
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}