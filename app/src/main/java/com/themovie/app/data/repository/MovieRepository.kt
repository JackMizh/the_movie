package com.themovie.app.data.repository

import com.themovie.app.data.api.RetrofitInstance
import com.themovie.app.data.model.MovieResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class MovieRepository {
    private val api = RetrofitInstance.api

    suspend fun getPopularMovies(page: Int): Result<List<MovieResult>> {
        return  withContext(Dispatchers.IO) {
            try {
                val response = api.getPopularMovies(page = page)
                Result.Success(response.results)
            } catch (e: IOException) {
                Result.Error("Network Error")
            } catch (e: HttpException) {
                Result.Error("Server Error")
            }
        }
    }
}