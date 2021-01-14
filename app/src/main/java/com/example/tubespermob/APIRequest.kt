package com.example.tubespermob

import com.example.tubespermob.api.NewsAPIJSON
import retrofit2.http.GET

interface APIRequest {

    @GET("/v1/latest-news?language=en&apiKey=q75GoG72TvpZbn6uQ5CMENpD5NiQWQrK2LPGWryTKjy2-2KT")
    suspend fun getNews(): NewsAPIJSON

}