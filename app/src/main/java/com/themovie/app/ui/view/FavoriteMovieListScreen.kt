package com.themovie.app.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.themovie.app.ui.composable.MovieItem
import com.themovie.app.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteMovieListScreen(navController: NavController, viewModel: MovieViewModel) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.loadFavoriteMovies()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Favorite Movies") })
        }
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.loadFavoriteMovies()
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    favoriteMovies.isEmpty() -> {
                        Text(
                            text = errorMessage ?: "Data Empty",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unknown Error",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.padding(top = 60.dp)
                        ) {
                            items(favoriteMovies) { favoriteMovies ->
                                MovieItem(
                                    id = favoriteMovies.id,
                                    poster_path = favoriteMovies.poster_path,
                                    vote_average = favoriteMovies.vote_average
                                ) {
                                    navController.navigate("movie_detail/${favoriteMovies.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}