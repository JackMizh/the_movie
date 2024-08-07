@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.themovie.app.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.themovie.app.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieDetailScreen(movieId: Int, viewModel: MovieViewModel) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

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
            floatingActionButton = {
                if (movieDetails != null) {
                    FloatingActionButton(
                        onClick = {
                            movieDetails?.let { viewModel.toggleFavorite(it) }
                        },
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
                        )
                    }
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                when {
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
                    movieDetails != null -> {
                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            Box(){
                                GlideImage(
                                    model = "https://image.tmdb.org/t/p/w500${movieDetails!!.backdrop_path}",
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp)
                                        .onGloballyPositioned {
                                            sizeImage = it.size
                                        }
                                )
                                Box(modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = sizeImage.height.toFloat() / 3,
                                            endY = sizeImage.height.toFloat()
                                        )
                                    ))
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Spacer(modifier = Modifier.height(350.dp))
                                Text(
                                    text = movieDetails?.title ?: "",
                                    style = TextStyle(
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                )
                                Text(
                                    text = "⭐ ${movieDetails!!.vote_average} (${movieDetails!!.vote_count})",
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                )
                                FlowRow(
                                    modifier = Modifier.padding(horizontal = 15.dp),
                                ) {
                                    if(movieDetails!!.genres.isNotEmpty()){
                                        movieDetails!!.genres.forEach { item ->
                                            if(item.id == movieDetails!!.genres[movieDetails!!.genres.size - 1].id){
                                                Text(
                                                    text = item.name,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 13.sp,
                                                )
                                            } else {
                                                Text(
                                                    text = "${item.name}, ",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 13.sp,
                                                )
                                            }
                                        }
                                    }
                                }
                                Text(
                                    text = movieDetails?.overview ?: "",
                                    color = Color.LightGray,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 20.dp, start = 15.dp, end = 15.dp, top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}