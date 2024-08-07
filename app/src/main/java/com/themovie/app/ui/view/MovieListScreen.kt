@file:OptIn(ExperimentalMaterial3Api::class)

package com.themovie.app.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.themovie.app.ui.composable.MovieItem
import com.themovie.app.ui.viewmodel.MovieViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieListScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val listState = rememberLazyGridState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { it.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                if(index == movies.size - 1) {
                    viewModel.getPopularMovies()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Popular Movies", color = Color.White) },
                actions = {
                    IconButton(onClick = { navController.navigate("favorite_movies") }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite Movies", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.refreshPopularMovies()
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    movies.isEmpty() -> {
                        Text(
                            text = errorMessage ?: "Data Empty",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unknown Error",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            state = listState,
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.padding(top = 60.dp, start = 10.dp, end = 10.dp),
                        ) {
                            items(movies) { movie ->
                                MovieItem(
                                    poster_path = movie.poster_path,
                                    vote_average = movie.vote_average
                                ) {
                                    navController.navigate("movie_detail/${movie.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
