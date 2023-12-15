package com.muthuram.faceliveness.helper

import android.content.Context
import com.muthuram.faceliveness.manager.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    fun getInstance(context: Context): Retrofit {

        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader("Content-Type", "application/json")
                // If token has been saved, add it to the request
                val preferenceManager = PreferenceManager(context)
                builder.addHeader("Authorization", "Bearer ${preferenceManager.getAccessToken()}")
                return@addInterceptor chain.proceed(builder.build())
            }
            .connectTimeout(40, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://ecs-dsapi-staging.digivalitsolutions.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

}