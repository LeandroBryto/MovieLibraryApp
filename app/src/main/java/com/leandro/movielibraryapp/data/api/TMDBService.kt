package com.leandro.movielibraryapp.data.api

import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.data.model.MovieResponse
import com.leandro.movielibraryapp.data.model.ReviewResponse
import com.leandro.movielibraryapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieDetails

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "pt-BR"
    ): MovieResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = "en-US"
    ): ReviewResponse
}