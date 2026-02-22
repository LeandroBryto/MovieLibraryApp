package com.leandro.movielibraryapp.di

import com.leandro.movielibraryapp.data.api.TMDBService
import com.leandro.movielibraryapp.data.repository.MovieRepository
import com.leandro.movielibraryapp.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AppContainer {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tmdbService: TMDBService = retrofit.create(TMDBService::class.java)

    val movieRepository = MovieRepository(tmdbService)
}