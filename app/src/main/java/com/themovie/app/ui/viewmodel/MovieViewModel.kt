package com.themovie.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themovie.app.data.model.Result
import com.themovie.app.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val _movies = MutableStateFlow<List<Result>>(emptyList())
    val movies: StateFlow<List<Result>> = _movies

    fun getPopularMovies(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.getPopularMovies(page)
            _movies.value = movies
        }
    }
}