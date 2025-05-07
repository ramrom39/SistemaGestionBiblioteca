package com.example.sistemagestionbiblioteca.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

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
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.sistemagestionbiblioteca.R
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.shelves.Shelf
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun Shelves(
    navController: NavController,
    currentUserId: Int,
    viewModel: ShelvesViewModel = viewModel(
        factory = ShelvesViewModelFactory(ApiService.getInstance())
    )
) {
    val isRefreshing by viewModel.isLoading.collectAsState()
    val shelves by viewModel.shelves.collectAsState()
    val booksMap by viewModel.booksByShelf.collectAsState()
    val context = LocalContext.current
    val swipeState = rememberSwipeRefreshState(isRefreshing)

    Scaffold(
        topBar = { CustomTopBar(navController, "Estanterías") },
        bottomBar = { BottomBar(navController, currentUserId) },
        containerColor = Color.White
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeState,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            if (isRefreshing) {
                // Placeholder de carga con 3 secciones y 3 tarjetas cada una
                LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
                    items(3) { _ ->
                        // Cabecera fantasma
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(Color.Gray.copy(alpha = 0.3f))
                        )
                        Spacer(Modifier.height(8.dp))
                        // Fila de tarjetas fantasma
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(3) { _ ->
                                Box(
                                    Modifier
                                        .width(220.dp)
                                        .height(260.dp)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            } else {
                val nonEmptyShelves = shelves.filter { booksMap[it.id]?.isNotEmpty() == true }
                LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
                    items(nonEmptyShelves) { shelf: Shelf ->
                        val books = booksMap[shelf.id].orEmpty()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = shelf.ubicacion,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF1D3A58),
                                fontSize = 16.sp
                            )
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(
                                    text = "${books.size} libros",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Divider(
                            color = Color(0xFF1D3A58),
                            thickness = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth(0.72f)
                                .padding(bottom = 8.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(books) { book ->
                                ShelvesBookCard(
                                    book = book,
                                    onDelete = {
                                        viewModel.deleteBook(shelf.id, book)
                                        Toast.makeText(
                                            context,
                                            "Libro '${book.Título}' eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
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