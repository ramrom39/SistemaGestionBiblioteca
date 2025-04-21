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
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.features.category.CategoryViewModel
import com.example.sistemagestionbiblioteca.navigation.BottomBar
import com.example.sistemagestionbiblioteca.navigation.CustomTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {
    val focusManager = LocalFocusManager.current
    val gradientBrush = Brush.radialGradient(
        colors = listOf(Color(0xFFF6E6CA), Color(0xFFF5EADA)),
        center = Offset(0.5f, 0.5f),
        radius = 2000f
    )

    val vm: CategoryViewModel = viewModel()
    val categories by vm.categories.observeAsState(emptyList())
    val error by vm.error.observeAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var newCatName by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.fetchCategories()
    }

    Scaffold(
        topBar = { CustomTopBar(navController) },
        bottomBar = { BottomBar(navController) },
        containerColor = Color(0xFFF6E6CA)
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(gradientBrush)
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
        ) {
            Column() {
                Spacer(modifier = Modifier.height(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.hero),
                    contentDescription = "Banner categorías",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .offset(x = (-12).dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Categorías",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8B75E)
                        )
                        IconButton(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF8B75E), CircleShape)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Crear categoría",
                                tint = Color.White
                            )
                        }
                    }

                    // Separador negro
                    Divider(
                        color = Color(0xFFF8B75E),
                        thickness = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.46f)
                            .padding(vertical = 8.dp)
                    )

                    // Diálogo de creación
                    if (showCreateDialog) {
                        AlertDialog(
                            onDismissRequest = { showCreateDialog = false },
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
                                    vm.createCategory(
                                        nombre = newCatName,
                                        descripcion = newCatDesc.ifBlank { null }
                                    )
                                    newCatName = ""
                                    newCatDesc = ""
                                    showCreateDialog = false
                                }) { Text("OK") }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showCreateDialog = false
                                }) { Text("Cancelar") }
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    Spacer(Modifier.height(8.dp))

                    // Colores de las tarjetas
                    val cardColors = listOf(
                        Color(0xFFB3E5FC).copy(alpha = 0.5f),
                        Color(0xFFC8E6C9).copy(alpha = 0.5f),
                        Color(0xFFFFF9C4).copy(alpha = 0.5f),
                        Color(0xFFFFCCBC).copy(alpha = 0.5f),
                        Color(0xFFD1C4E9).copy(alpha = 0.5f)
                    )

                    // Carrusel de categorías con colores
                    LazyRow {
                        itemsIndexed(categories) { index, cat ->
                            val bgColor = cardColors[index % cardColors.size]
                            CategoryCard(
                                category = cat,
                                backgroundColor = bgColor,
                                onDelete = { vm.deleteCategory(cat.id) },
                                onUpdate = { updatedName, updatedDesc ->
                                    vm.updateCategory(cat.id, updatedName, updatedDesc)
                                }
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                    }

                    // Mensaje de error
                    error?.let {
                        Spacer(Modifier.height(16.dp))
                        Text(text = it, color = Color.Red)
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