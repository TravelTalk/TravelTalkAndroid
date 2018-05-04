package io.uh18.traveltalk.android.backend.mock

import io.uh18.traveltalk.android.SERVICE_URL
import io.uh18.traveltalk.android.backend.LocationApi
import io.uh18.traveltalk.android.backend.TravelTalkClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

class TravelTalkClientMock : TravelTalkClient {

    private val behavior = NetworkBehavior.create()

    private lateinit var retrofit: Retrofit

    override fun createLocationService(): LocationApi {
        initBuilder()

        val mockRetrofit = MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior).build()

        val delegate = mockRetrofit.create(LocationApi::class.java)

        return LocationApiMock(delegate)
    }

    private fun initBuilder() {
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SERVICE_URL)
                .build()
    }
}