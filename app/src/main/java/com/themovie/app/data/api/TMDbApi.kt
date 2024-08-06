package com.themovie.app.data.api

import com.themovie.app.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key") apiKey: String): MovieResponse
}