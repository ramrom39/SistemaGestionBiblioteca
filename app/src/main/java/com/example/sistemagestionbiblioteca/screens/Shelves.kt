package com.example.sistemagestionbiblioteca.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


import com.example.sistemagestionbiblioteca.navigation.BottomBar
import com.example.sistemagestionbiblioteca.navigation.CustomTopBar
import com.example.sistemagestionbiblioteca.network.ApiService

import com.example.sistemagestionbiblioteca.features.shelves.ShelvesViewModel
import com.example.sistemagestionbiblioteca.features.shelves.ShelvesViewModelFactory

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.sistemagestionbiblioteca.R
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.shelves.Shelf

@Composable
fun Shelves(
    navController: NavController,
    currentUserId: Int,
    viewModel: ShelvesViewModel = viewModel(
        factory = ShelvesViewModelFactory(ApiService.getInstance())
    )
) {
    // Observamos listas de estanterías y mapas de libros
    val shelves = viewModel.shelves.collectAsState().value
    val booksMap = viewModel.booksByShelf.collectAsState().value

    Scaffold(
        topBar = { CustomTopBar(navController, "Estanterías") },
        bottomBar = { BottomBar(navController, currentUserId) },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(8.dp)
        ) {
            items(shelves) { shelf: Shelf ->
                // Título de la estantería
                Text(
                    text = shelf.ubicacion ?: shelf.ubicacion,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Fila horizontal de libros de esta estantería
                val books: List<Book> = booksMap[shelf.id].orEmpty()
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(books) { book ->
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            // Usamos carta específica de estanterías con eliminación
                            ShelvesBookCard(
                                book = book,
                                onDelete = { viewModel.deleteBook(shelf.id, book) }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ShelvesBookCard(
    book: Book,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .width(250.dp)
            .height(300.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.librocerrado),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = book.Título?:"",
                        color = Color(0xFF1D3A58),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = book.Autor?:"" ,
                        color = Color(0xFF1D3A58).copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(28.dp)
                    .background(Color.Red.copy(alpha = 0.6f), CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Borrar",
                    tint = Color.White
                )
            }
        }
    }
}