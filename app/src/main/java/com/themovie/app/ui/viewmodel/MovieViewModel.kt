package com.themovie.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themovie.app.data.model.FavoriteMovies
import com.themovie.app.data.model.MovieDetailsResponse
import com.themovie.app.data.model.MovieResult
import com.themovie.app.data.repository.MovieRepository
import com.themovie.app.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(context: Context) : ViewModel() {
    private val repository = MovieRepository(context)

    private val _movies = MutableStateFlow<List<MovieResult>>(emptyList())
    val movies: StateFlow<List<MovieResult>> = _movies

    private val _movieDetails = MutableStateFlow<MovieDetailsResponse?>(null)
    val movieDetails: StateFlow<MovieDetailsResponse?> = _movieDetails

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _favoriteMovies = MutableStateFlow<List<FavoriteMovies>>(emptyList())
    val favoriteMovies: StateFlow<List<FavoriteMovies>> = _favoriteMovies

    private var currentPage = 1

    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getPopularMovies(currentPage)) {
                is Result.Success -> {
                    _movies.value += result.data
                    _errorMessage.value = if (result.data.isEmpty()) "No Movies Available" else null
                    currentPage++
                }
                is Result.Error -> {
                    _errorMessage.value = result.message
                }
            }
            _isLoading.value = false
        }
    }

    fun refreshPopularMovies() {
        viewModelScope.launch {
            currentPage = 1
            getPopularMovies()
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.getMovieDetails(movieId)) {
                is Result.Success -> {
                    _movieDetails.value = result.data
                    _errorMessage.value = null
                }
                is Result.Error -> {
                    _errorMessage.value = result.message
                }
            }
            _isFavorite.value = repository.isFavorite(movieId)
            _isLoading.value = false
        }
    }

    fun loadFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovies().collect { movies ->
                _favoriteMovies.value = movies
            }
        }
    }

    fun toggleFavorite(movie: MovieDetailsResponse) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                repository.removeFromFavorites(movie)
            } else {
                repository.addToFavorites(movie)
            }
            _isFavorite.value = !_isFavorite.value
        }
    }
}