package com.themovie.app.data.api

import com.themovie.app.BuildConfig
import com.themovie.app.data.model.MovieDetailsResponse
import com.themovie.app.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.AUTH_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = BuildConfig.AUTH_KEY,
        @Query("language") language: String = "en-US"
    ): MovieDetailsResponse
}