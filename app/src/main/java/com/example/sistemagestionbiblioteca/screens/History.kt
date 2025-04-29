package com.example.sistemagestionbiblioteca.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sistemagestionbiblioteca.features.history.HistoryViewModel
import com.example.sistemagestionbiblioteca.navigation.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(
    navController: NavController,
    currentUserId: Int,
    viewModel: HistoryViewModel = viewModel()
) {
    val searchQuery   by viewModel.searchQuery.collectAsState()
    val suggestions   by viewModel.suggestions.collectAsState()
    val selectedBook  by viewModel.selectedBook.collectAsState()
    val categoryName  by viewModel.categoryName.collectAsState()
    val historyList   by viewModel.historyList.collectAsState()
    val userNames     by viewModel.userNames.collectAsState()
    val focusManager  = LocalFocusManager.current

    val searchBarColor = Color(0xFFF0F0F0)
    val textFieldColors = TextFieldDefaults.colors(
        unfocusedTextColor      = Color(0xFFFAA634),
        focusedTextColor        = Color(0xFFFAA634),
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor   = Color.Transparent,
        focusedIndicatorColor   = Color(0xFFFAA634),
        unfocusedIndicatorColor = Color(0xFFFAA634),
        cursorColor             = Color(0xFFFAA634),
        focusedLabelColor       = Color.Black,
        unfocusedLabelColor     = Color.Black
    )

    Scaffold(
        bottomBar     = { BottomBar(navController, currentUserId) },
        containerColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                    viewModel.clearSuggestions()
                }
        ) {
            Column {
                // — Buscador + OK —
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value         = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder   = { Text("Buscar libro por nombre") },
                        leadingIcon   = { Icon(Icons.Filled.Search, contentDescription = null) },
                        colors        = textFieldColors,
                        modifier      = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .onFocusChanged { state ->
                                if (!state.isFocused) viewModel.clearSuggestions()
                            }
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.onSearchButtonClicked()
                        focusManager.clearFocus()
                    }) {
                        Text("OK")
                    }
                }

                // — Sugerencias —
                if (suggestions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(searchBarColor)
                            .padding(horizontal = 16.dp)
                            .heightIn(max = 180.dp)
                    ) {
                        items(suggestions) { book ->
                            Text(
                                text     = book.Título,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        viewModel.onBookSelected(book)
                                        focusManager.clearFocus()
                                    }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // — Detalles del libro —
                selectedBook?.let { book ->
                    Text(
                        text       = "Detalles del libro",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF1D3A58),
                        modifier   = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier  = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${book.ID}", fontSize = 14.sp)
                            Text("Título: ${book.Título}", fontSize = 16.sp)
                            Text("Autor: ${book.Autor}", fontSize = 14.sp)
                            Text("Año: ${book.Año}", fontSize = 14.sp)
                            Text("Sinopsis: ${book.Sinopsis}", fontSize = 14.sp)
                            Text("Categoría: $categoryName", fontSize = 14.sp)
                            Text("Estado: ${book.Estado}", fontSize = 14.sp)
                            Text("Fecha: ${book.Fecha}", fontSize = 14.sp)
                            Text("Estantería ID: ${book.Estanteria_ID}", fontSize = 14.sp)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // — Historial del libro —
                    Text(
                        text       = "Historial del libro",
                        fontSize   = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF1D3A58),
                        modifier   = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(12.dp))

                    // — Lista de historial —
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(historyList) { history ->

                            Card(
                                modifier  = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Fecha: ${history.date}",      fontSize = 14.sp)
                                    Text("Acción: ${history.actionType}", fontSize = 14.sp)
                                    if (userNames.containsKey(history.userId)) {
                                        Text(
                                            text = "Usuario: ${userNames[history.userId]}",
                                            fontSize = 14.sp
                                        )
                                    } else {
                                        Text(
                                            text = "Usuario: Cargando…",
                                            fontSize = 14.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
