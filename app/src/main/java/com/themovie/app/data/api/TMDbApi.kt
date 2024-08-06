package com.themovie.app.data.api

import com.themovie.app.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TMDbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") authorization: String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMWUyNzNmNzA4ZGNiNWRhM2M0ZGEyN2RkMGFlNjllOSIsIm5iZiI6MTcyMjkzNzEzMS4xNTQxNDUsInN1YiI6IjY0ODgyMTJjZTM3NWMwMDBhY2M2ZTNlOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.pbo9GHYJ0nI7FmYECQSDPp-178xoQCM4J3wqS8Ysxi8",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MovieResponse
}