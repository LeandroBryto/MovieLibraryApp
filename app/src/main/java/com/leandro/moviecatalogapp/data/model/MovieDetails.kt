package com.leandro.moviecatalogapp.data.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val vote_average: Double
)
