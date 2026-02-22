package com.leandro.movielibraryapp.data.repository

import com.leandro.movielibraryapp.data.api.TMDBService
import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.data.model.MovieResponse
import com.leandro.movielibraryapp.data.model.ReviewResponse


class MovieRepository(private val tmdbService: TMDBService) {

    suspend fun getNowPlaying(): MovieResponse {
        return tmdbService.getNowPlaying()
    }

    suspend fun getPopular(): MovieResponse {
        return tmdbService.getPopular()
    }

    suspend fun getUpcoming(): MovieResponse {
        return tmdbService.getUpcoming()
    }

    suspend fun getTopRated(): MovieResponse {
        return tmdbService.getTopRated()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return tmdbService.getMovieDetails(movieId)
    }

    suspend fun getSimilarMovies(movieId: Int): MovieResponse {
        return tmdbService.getSimilarMovies(movieId)
    }

    suspend fun getMovieReviews(movieId: Int): ReviewResponse {
        return tmdbService.getMovieReviews(movieId)
    }
}