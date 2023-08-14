package com.example.planradar.infrastructure.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiExecutor {

    const val PLANRADAR_APIS_ENDPOINT = "https://openweathermap.org/"

    private fun getRetrofit(endpointURL: String): Retrofit {

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

        val okHttpClient = clientBuilder.build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(endpointURL)
            .build()
    }

    private class HeadersInterceptor: Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {

            val newRequest = chain.request().newBuilder()
                .header("appid", "f5cb0b965ea1564c50c6f1b74534d823")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .build()

            return chain.proceed(newRequest)
        }
    }

    fun createApi(endpointURL: String): PlanRadarApi {
        val retrofit = getRetrofit(endpointURL)
        return retrofit.create(PlanRadarApi::class.java)
    }
}