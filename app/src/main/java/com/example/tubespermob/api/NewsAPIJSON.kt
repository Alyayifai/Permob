package com.example.tubespermob.api

data class NewsAPIJSON(
    val news: List<New>,
    val page: Int,
    val status: String
)