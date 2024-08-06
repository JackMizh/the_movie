package com.themovie.app.data.model

import com.themovie.app.data.db.FavoriteMovieEntity

data class FavoriteMovies(
    val id: Int,
    val poster_path: String,
    val vote_average: Double,
)

fun FavoriteMovieEntity.toDomain(): FavoriteMovies {
    return FavoriteMovies(
        id = id,
        vote_average = vote_average,
        poster_path = poster_path,
    )
}