package io.uh18.traveltalk.android.backend

import io.uh18.traveltalk.android.SERVICE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {

        private val httpClient = OkHttpClient.Builder()

        private val logging = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)


        private var builder: Retrofit.Builder = Retrofit.Builder()

        private lateinit var retrofit: Retrofit

        fun createLocationService(): LocationApi {
            initBuilder()
            return retrofit.create(LocationApi::class.java)
        }

        private fun initBuilder() {
            if (!httpClient.interceptors().contains(logging)) {
                httpClient.addInterceptor(logging)
                builder.client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl(SERVICE_URL)
                        .build()

                retrofit = builder.build()
            }
        }

    }
}