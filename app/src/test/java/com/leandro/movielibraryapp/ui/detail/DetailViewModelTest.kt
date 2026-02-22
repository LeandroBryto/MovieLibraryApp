package com.leandro.movielibraryapp.ui.detail

import com.leandro.movielibraryapp.data.repository.MovieRepository
import com.leandro.movielibraryapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val repository = mockk<MovieRepository>()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `ao iniciar viewModel deve carregar detalhes com sucesso`() {
        coEvery { repository.getMovieDetails(any()) } returns mockk(relaxed = true)
        coEvery { repository.getSimilarMovies(any()) } returns mockk(relaxed = true)
        coEvery { repository.getMovieReviews(any()) } returns mockk(relaxed = true)

        val viewModel = DetailViewModel(repository, 123)

        assertTrue(viewModel.movieDetails.value is Resource.Success)
    }
}