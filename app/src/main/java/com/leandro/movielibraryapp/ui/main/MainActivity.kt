package com.leandro.movielibraryapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.leandro.movielibraryapp.data.model.Movie
import com.leandro.movielibraryapp.databinding.ActivityMainBinding
import com.leandro.movielibraryapp.di.AppContainer
import com.leandro.movielibraryapp.ui.adapter.MovieAdapter
import com.leandro.movielibraryapp.ui.detail.DetailActivity
import com.leandro.movielibraryapp.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var viewModel: MainViewModel

    private val nowPlayingAdapter = MovieAdapter { movie -> onMovieClick(movie) }
    private val popularAdapter = MovieAdapter { movie -> onMovieClick(movie) }
    private val upcomingAdapter = MovieAdapter { movie -> onMovieClick(movie) }
    private val topRatedAdapter = MovieAdapter { movie -> onMovieClick(movie) }

    // Variável para controlar o tempo da Splash (Requisito 6)
    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Instala a Splash antes do super
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 2. REQUISITO 6: Segura a Splash por 2 segundos
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashScreen = false
        }, 2000)

        setupViewModel()
        setupRecyclerViews()
        observeMovies()
    }

    private fun setupViewModel() {
        val factory = MainViewModelFactory(AppContainer.movieRepository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        binding.rvNowPlaying.adapter = nowPlayingAdapter
        binding.rvPopular.adapter = popularAdapter
        binding.rvUpcoming.adapter = upcomingAdapter
        binding.rvTopRated.adapter = topRatedAdapter
    }

    private fun observeMovies() {
        lifecycleScope.launch {
            viewModel.nowPlayingMovies.collectLatest {
                if (it is Resource.Success) nowPlayingAdapter.submitList(it.data)
            }
        }

        lifecycleScope.launch {
            viewModel.popularMovies.collectLatest {
                if (it is Resource.Success) popularAdapter.submitList(it.data)
            }
        }

        lifecycleScope.launch {
            viewModel.upcomingMovies.collectLatest {
                if (it is Resource.Success) upcomingAdapter.submitList(it.data)
            }
        }

        lifecycleScope.launch {
            viewModel.topRatedMovies.collectLatest {
                if (it is Resource.Success) topRatedAdapter.submitList(it.data)
            }
        }
    }

    private fun onMovieClick(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("MOVIE_ID", movie.id)
        }
        startActivity(intent)
    }
}
