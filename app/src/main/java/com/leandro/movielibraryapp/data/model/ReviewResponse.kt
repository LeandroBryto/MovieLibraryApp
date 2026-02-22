package com.leandro.movielibraryapp.data.model

data class ReviewResponse(
    val results: List<Review>
)

data class Review(
    val author: String,
    val content: String
)