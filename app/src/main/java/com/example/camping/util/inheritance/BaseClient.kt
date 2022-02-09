package com.example.camping.util.inheritance

import com.example.camping.util.data.SERVICE
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

open class BaseClient {
    protected val baseInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url
            .newBuilder()
            .addEncodedQueryParameter("ServiceKey", SERVICE.API_KEY)
            .addQueryParameter("MobileOS", SERVICE.APP_OS)
            .addQueryParameter("MobileApp", SERVICE.APP_NAME)
            .addQueryParameter("_type", SERVICE.TYPE)
            .build()

        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }

    protected val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    protected var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(baseInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}