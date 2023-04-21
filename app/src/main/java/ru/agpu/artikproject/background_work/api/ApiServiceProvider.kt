package ru.agpu.artikproject.background_work.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    fun <T> provideService(url: String, clazz: Class<T>): T {

        val httpClient = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        return retrofit.create(clazz)
    }
}