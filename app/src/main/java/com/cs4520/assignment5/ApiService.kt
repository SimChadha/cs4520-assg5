package com.cs4520.assignment5

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
    .baseUrl(Api.BASE_URL)
    .build()

interface ApiService {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(): Response<ProductList>
}

object ProductApi {
    // Expose singleton retrofit object to run queries
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}