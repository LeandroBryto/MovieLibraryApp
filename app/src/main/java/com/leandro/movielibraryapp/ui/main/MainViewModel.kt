package com.leandro.movielibraryapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leandro.movielibraryapp.data.model.Movie
import com.leandro.movielibraryapp.data.repository.MovieRepository
import com.leandro.movielibraryapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _nowPlayingMovies = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val nowPlayingMovies: StateFlow<Resource<List<Movie>>> = _nowPlayingMovies

    private val _popularMovies = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val popularMovies: StateFlow<Resource<List<Movie>>> = _popularMovies

    private val _upcomingMovies = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val upcomingMovies: StateFlow<Resource<List<Movie>>> = _upcomingMovies

    private val _topRatedMovies = MutableStateFlow<Resource<List<Movie>>>(Resource.Loading())
    val topRatedMovies: StateFlow<Resource<List<Movie>>> = _topRatedMovies

    init {
        fetchAllMovies()
    }

    private fun fetchAllMovies() {
        viewModelScope.launch {
            try {
                _nowPlayingMovies.value = Resource.Success(repository.getNowPlaying().results)
            } catch (e: Exception) {
                _nowPlayingMovies.value = Resource.Error(e.message ?: "Erro desconhecido")
            }
        }
        viewModelScope.launch {
            try {
                _popularMovies.value = Resource.Success(repository.getPopular().results)
            } catch (e: Exception) {
                _popularMovies.value = Resource.Error(e.message ?: "Erro desconhecido")
            }
        }
        viewModelScope.launch {
            try {
                _upcomingMovies.value = Resource.Success(repository.getUpcoming().results)
            } catch (e: Exception) {
                _upcomingMovies.value = Resource.Error(e.message ?: "Erro desconhecido")
            }
        }
        viewModelScope.launch {
            try {
                _topRatedMovies.value = Resource.Success(repository.getTopRated().results)
            } catch (e: Exception) {
                _topRatedMovies.value = Resource.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}