package com.themovie.app.data.repository

import com.themovie.app.data.api.RetrofitInstance
import com.themovie.app.data.model.Result

class MovieRepository {
    private val api = RetrofitInstance.api

    suspend fun getPopularMovies(page: Int): List<Result> {
        return api.getPopularMovies(page = page).results
    }
}