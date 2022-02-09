package com.example.camping.data.retrofit

import com.example.camping.util.data.SERVICE
import com.example.camping.util.inheritance.BaseClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CustomClient : BaseClient() {

    fun getClient(customInterceptor: CustomInterceptor): CampingService {

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(baseInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(customInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVICE.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampingService::class.java)
    }
}

object BasicClient : BaseClient() {

    fun getClient(): CampingService {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(SERVICE.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CampingService::class.java)
    }
}