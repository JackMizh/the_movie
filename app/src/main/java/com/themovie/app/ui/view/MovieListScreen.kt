@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.themovie.app.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.themovie.app.data.model.Result
import com.themovie.app.ui.viewmodel.MovieViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieListScreen(viewModel: MovieViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val movies by viewModel.movies.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getPopularMovies(page = 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Popular Movies") })
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 60.dp)
        ) {
            items(movies) { movie ->
                MovieItem(movie)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Result) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = "https://image.tmdb.org/t/p/w500" + movie.poster_path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
                Text(text = movie.overview, maxLines = 3, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MovieItem(
        movie = Result(
            adult = false,
            backdrop_path = "/2RVcJbWFmICRDsVxRI8F5xRmRsK.jpg",
            genre_ids = listOf(27, 878, 53),
            id = 762441,
            original_language = "en",
            original_title = "A Quiet Place: Day One",
            overview = "As New York City is invaded by alien creatures who hunt by sound, a woman named Sam fights to survive with her cat.",
            popularity = 4907.314,
            poster_path = "/yrpPYKijwdMHyTGIOd1iK1h0Xno.jpg",
            release_date = "2024-06-26",
            title = "A Quiet Place: Day One",
            video = false,
            vote_average = 7.026,
            vote_count = 1011
        )
    )
}

@Preview(showBackground = true)
@Composable
fun MovieListScreenPreview() {
    MovieListScreen()
}
