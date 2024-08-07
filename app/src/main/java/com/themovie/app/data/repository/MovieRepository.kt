package com.themovie.app.data.repository

import android.content.Context
import com.themovie.app.data.api.RetrofitInstance
import com.themovie.app.data.db.FavoriteMovieDao
import com.themovie.app.data.db.MovieDatabase
import com.themovie.app.data.model.FavoriteMovies
import com.themovie.app.data.model.MovieDetailsResponse
import com.themovie.app.data.model.MovieResult
import com.themovie.app.data.model.toDomain
import com.themovie.app.data.model.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class MovieRepository(context: Context) {
    private val favoriteMovieDao: FavoriteMovieDao
    private val api = RetrofitInstance.api

    init {
        val database = MovieDatabase.getDatabase(context)
        favoriteMovieDao = database.favoriteMovieDao()
    }

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

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getMovieDetails(movieId = movieId)
                Result.Success(response)
            } catch (e: IOException) {
                Result.Error("Network Error")
            } catch (e: HttpException) {
                Result.Error("Server Error")
            }
        }
    }

    fun getFavoriteMovies(): Flow<List<FavoriteMovies>> {
        return favoriteMovieDao.getFavoriteMovies().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun addToFavorites(movie: MovieDetailsResponse) {
        favoriteMovieDao.insert(movie.toEntity())
    }

    suspend fun removeFromFavorites(movie: MovieDetailsResponse) {
        favoriteMovieDao.delete(movie.toEntity())
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteMovieDao.getMovieById(movieId) != null
    }
}