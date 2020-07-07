package com.baott.trackme.network

import com.baott.trackme.BuildConfig
import com.baott.trackme.constants.Constants
import com.baott.trackme.log.LOG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/*
 * Created by baotran on 10/4/18 
 */

class RestClient {
    companion object {
        // Development
        private const val URL_ROOT_DEVELOPMENT = "https://newsapi.org"
        private const val URL_API_DEVELOPMENT = "$URL_ROOT_DEVELOPMENT/v2/"

        // Production
        private const val URL_ROOT_PRODUCTION = "https://newsapi.org"
        private const val URL_API_PRODUCTION =  "$URL_ROOT_PRODUCTION/v2/"

        // Api service instances
        private var sInstance : IApiService

        init {
            sInstance = setUpRestClient(getBaseUrl()).create(IApiService::class.java)
            LOG.d("Init api service instance for the first time")
        }

        fun getInstance() : IApiService {
            return sInstance
        }

        /**
         * Set up rest client
         */
        private fun setUpRestClient(baseUrl: String): Retrofit {
            // Custom OkHttpClient
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .connectTimeout(Constants.Connection.TIME_OUT_CONNECTION, TimeUnit.SECONDS)
                    .readTimeout(Constants.Connection.TIME_OUT_READ, TimeUnit.SECONDS)
                    .writeTimeout(Constants.Connection.TIME_OUT_WRITE, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)

            okHttpClientBuilder.interceptors().add(Interceptor { chain ->
                val originalRequest = chain.request()

                // Add query
                val originalUrl = originalRequest.url()
                val customUrl = originalUrl.newBuilder()
                        .addQueryParameter("apiKey", "5b21f2f83ec54e1195fc6946b9af35c1")
//                        .addQueryParameter("api_key", MyApplication.getContext()?.getString(R.string.api_key_the_movie_db))
                        .build()

                // Add header
                val customRequest = originalRequest.newBuilder()
//                        .header("User-Agent", "")
//                        .header("Accept", "application/json")
//                        .header("Authorization", "")
//                        .header("X-API-Key", "")
//                        .header("Device", "")
                        .url(customUrl)
                        .method(originalRequest.method(), originalRequest.body())
                        .build()

                chain.proceed(customRequest)
            })

            // Retrofit client
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClientBuilder.build())
                    .build()
        }

        /**
         * Return api url
         */
        private fun getBaseUrl() : String {
            when (Constants.DEVELOPMENT_MODE) {
                Constants.DevelopmentMode.DEVELOPMENT -> {
                    return URL_API_DEVELOPMENT
                }

                Constants.DevelopmentMode.PRODUCTION -> {
                    return URL_API_PRODUCTION
                }
            }

            return URL_API_DEVELOPMENT
        }
    }
}