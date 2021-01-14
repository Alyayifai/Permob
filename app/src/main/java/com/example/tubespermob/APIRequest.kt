package com.example.tubespermob

import com.example.tubespermob.api.NewsAPIJSON
import com.example.tubespermob.api.Register
import com.example.tubespermob.api.SignIn
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface APIRequest {

    @GET("/v1/latest-news?language=en&apiKey=q75GoG72TvpZbn6uQ5CMENpD5NiQWQrK2LPGWryTKjy2-2KT")
    suspend fun getNews(): NewsAPIJSON

    @FormUrlEncoded
    @POST("/api/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<SignIn>

    @FormUrlEncoded
    @POST("/api/register")
    fun createUser(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<Register>

}