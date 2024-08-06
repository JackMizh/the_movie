package com.themovie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.themovie.app.ui.theme.TheMovieTheme
import com.themovie.app.ui.view.FavoriteMovieListScreen
import com.themovie.app.ui.view.MovieDetailScreen
import com.themovie.app.ui.view.MovieListScreen
import com.themovie.app.ui.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheMovieTheme {
                val navController = rememberNavController()
                val movieViewModel = MovieViewModel(applicationContext)
                NavHost(
                    navController = navController,
                    startDestination = "movie_list"
                ) {
                    composable("movie_list") { MovieListScreen(navController, movieViewModel) }
                    composable("movie_detail/{movieId}") { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                        movieId?.let { MovieDetailScreen(it, movieViewModel) }
                    }
                    composable("favorite_movies") { FavoriteMovieListScreen(navController, movieViewModel) }
                }
            }
        }
    }
}