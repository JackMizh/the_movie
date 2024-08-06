@file:OptIn(ExperimentalMaterial3Api::class)

package com.themovie.app.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.themovie.app.ui.viewmodel.MovieViewModel

@Composable
fun MovieDetailScreen(movieId: Int, viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            viewModel.getMovieDetails(movieId)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Movie Details") })
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unknown Error",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    movieDetails != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = movieDetails?.title ?: "", style = MaterialTheme.typography.titleLarge)
                            Text(text = "Release Date: ${movieDetails?.release_date}")
                            Text(text = "Rating: ${movieDetails?.vote_average}")
                            Text(text = movieDetails?.overview ?: "")
                        }
                    }
                }
            }
        }
    }
}