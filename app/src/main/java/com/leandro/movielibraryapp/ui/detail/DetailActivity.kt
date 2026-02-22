package com.leandro.movielibraryapp.ui.detail
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.leandro.movielibraryapp.data.model.MovieDetails
import com.leandro.movielibraryapp.di.AppContainer
import com.leandro.movielibraryapp.util.Resource

class DetailActivity : ComponentActivity() {

    private val movieId: Int by lazy { intent.getIntExtra("MOVIE_ID", -1) }

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(AppContainer.movieRepository, movieId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val movieDetailsState by viewModel.movieDetails.collectAsState()
            DetailScreen(movieDetailsState, onBack = { finish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(movieState: Resource<MovieDetails>, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (movieState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is Resource.Error -> {
                    Text(
                        text = movieState.message ?: "Erro desconhecido",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    movieState.data?.let { movie ->
                        MovieDetailContent(movie)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieDetailContent(movie: MovieDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
            contentDescription = "Backdrop do filme",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = "Pôster do filme",
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(movie.title, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                movie.releaseDate?.let { Text("Lançamento: $it", fontSize = 14.sp, color = Color.LightGray) }
                Spacer(modifier = Modifier.height(4.dp))
                movie.voteAverage?.let { Text(String.format("Nota: %.1f", it), fontSize = 14.sp, color = Color.LightGray) }
                Spacer(modifier = Modifier.height(4.dp))
                movie.runtime?.let { Text("$it min", fontSize = 14.sp, color = Color.LightGray) }
            }
        }

        Text(
            text = "Sinopse",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview ?: "Sinopse não disponível.",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.LightGray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
