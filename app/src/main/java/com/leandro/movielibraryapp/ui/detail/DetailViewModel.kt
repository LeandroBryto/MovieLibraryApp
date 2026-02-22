package com.leandro.movielibraryapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.data.model.MovieResponse
import com.leandro.movielibraryapp.data.model.Review
import com.leandro.movielibraryapp.data.model.ReviewResponse
import com.leandro.movielibraryapp.data.repository.MovieRepository
import com.leandro.movielibraryapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository, private val movieId: Int) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Resource<MovieDetails>>(Resource.Loading())
    val movieDetails: StateFlow<Resource<MovieDetails>> = _movieDetails

    private val _similarMovies = MutableStateFlow<Resource<MovieResponse>>(Resource.Loading())
    val similarMovies: StateFlow<Resource<MovieResponse>> = _similarMovies

    private val _movieReviews = MutableStateFlow<Resource<ReviewResponse>>(Resource.Loading())
    val movieReviews: StateFlow<Resource<ReviewResponse>> = _movieReviews

    init {
        fetchAllData(movieId)
    }

    private fun fetchAllData(id: Int) {
        viewModelScope.launch {
            try {
                _movieDetails.value = Resource.Success(repository.getMovieDetails(id))
            } catch (e: Exception) {
                _movieDetails.value = Resource.Error(e.message ?: "Erro nos detalhes")
            }
        }

        viewModelScope.launch {
            try {
                _similarMovies.value = Resource.Success(repository.getSimilarMovies(id))
            } catch (e: Exception) {
                _similarMovies.value = Resource.Error(e.message ?: "Erro nos similares")
            }
        }

        viewModelScope.launch {
            _movieReviews.value = Resource.Loading()
            try {
                val response = repository.getMovieReviews(id)

                if (response.results.isEmpty()) {
                    val mockReview = Review(
                        author = "Usuário VIP",
                        content = "Este filme é excelente! A fotografia e a atuação estão impecáveis. Recomendo a todos que gostam de uma boa história bem contada. (Nota: Review gerada pois a API não possui comentários para este título)."
                    )
                    _movieReviews.value = Resource.Success(ReviewResponse(listOf(mockReview)))
                } else {
                    _movieReviews.value = Resource.Success(response)
                }

            } catch (e: Exception) {
                _movieReviews.value = Resource.Error(e.message ?: "Erro ao carregar avaliações.")
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