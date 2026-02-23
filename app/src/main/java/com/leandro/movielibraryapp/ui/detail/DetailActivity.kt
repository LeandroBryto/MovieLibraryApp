package com.leandro.movielibraryapp.ui.detail
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leandro.movielibraryapp.data.model.Movie
import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.data.model.MovieResponse
import com.leandro.movielibraryapp.data.model.Review
import com.leandro.movielibraryapp.data.model.ReviewResponse
import com.leandro.movielibraryapp.di.AppContainer
import com.leandro.movielibraryapp.util.Resource
import java.util.Locale

class DetailActivity : ComponentActivity() {

    private val movieId: Int by lazy { intent.getIntExtra("MOVIE_ID", -1) }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(AppContainer.movieRepository, movieId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val movieDetailsState by viewModel.movieDetails.collectAsState()
            val similarMoviesState by viewModel.similarMovies.collectAsState()
            val movieReviewsState by viewModel.movieReviews.collectAsState()
            DetailScreen(movieDetailsState, similarMoviesState, movieReviewsState, onBack = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieState: Resource<MovieDetails>,
    similarState: Resource<MovieResponse>,
    reviewsState: Resource<ReviewResponse>,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (movieState) {
                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> Text(movieState.message ?: "Erro", color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is Resource.Success -> {
                    movieState.data?.let {
                        MovieDetailContent(movie = it, similarState = similarState, reviewsState = reviewsState, onBack = onBack)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: MovieDetails, similarState: Resource<MovieResponse>, reviewsState: Resource<ReviewResponse>, onBack: () -> Unit) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Box(modifier = Modifier.fillMaxWidth().height(210.dp)) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.4f
            )

            IconButton(onClick = onBack, modifier = Modifier.padding(8.dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color.White)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f).padding(bottom = 80.dp)) {
                    Text(movie.title ?: "", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(
                        text = formatRuntime(movie.runtime),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )
                    Text(
                        text = "⭐ Nota: ${String.format(Locale.US, "%.1f", movie.voteAverage)} / 10 Média de Votos",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp,

                    )
                }

                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text("Sinopse", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp), color = Color.White)
        Text(movie.overview ?: "", modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Comentários", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp), color = Color.White)
        when(reviewsState) {
            is Resource.Success -> {
                val reviews = reviewsState.data?.results ?: emptyList()
                Column(modifier = Modifier.padding(16.dp)) {
                    if (reviews.isEmpty()) {
                        Text("Sem comentários.", color = Color.Gray)
                    } else {
                        reviews.take(3).forEach { review ->
                            ReviewItem(review = review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            else -> { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
        }

        Text("Mais como este", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp), color = Color.White)
        when(similarState) {
            is Resource.Success -> {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(similarState.data?.results ?: emptyList()) { item ->
                        SimilarMovieItem(movie = item)
                    }
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(review.author, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = review.content,
            color = Color.LightGray,
            fontSize = 13.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SimilarMovieItem(movie: Movie) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(end = 12.dp)
            .clickable {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", movie.id)
                }
                context.startActivity(intent)
            }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.title ?: "",
            color = Color.LightGray,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun formatRuntime(runtime: Int?): String {
    if (runtime == null || runtime == 0) return "0 minuto(s)"
    val h = runtime / 60
    val m = runtime % 60
    return "${h} hora(s) ${m} minuto(s)"
}
