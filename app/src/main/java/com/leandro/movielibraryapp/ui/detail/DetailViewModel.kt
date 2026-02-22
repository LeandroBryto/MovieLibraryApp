package com.leandro.movielibraryapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.data.repository.MovieRepository
import com.leandro.movielibraryapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository, private val movieId: Int) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Resource<MovieDetails>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<MovieDetails>> = _movieDetails

    init {
        fetchMovieDetails(movieId)
    }

    private fun fetchMovieDetails(id: Int) {
        viewModelScope.launch {
            _movieDetails.value = Resource.Loading()
            try {
                val details = repository.getMovieDetails(id)
                _movieDetails.value = Resource.Success(details)
            } catch (e: Exception) {
                _movieDetails.value = Resource.Error(e.message ?: "Erro ao carregar detalhes do filme.")
            }
        }
    }
}

class DetailViewModelFactory(private val repository: MovieRepository, private val movieId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository, movieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}