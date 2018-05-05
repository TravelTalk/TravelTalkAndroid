package io.uh18.traveltalk.android.backend.mock

import io.uh18.traveltalk.android.SERVICE_URL
import io.uh18.traveltalk.android.backend.ChatApi
import io.uh18.traveltalk.android.backend.LocationApi
import io.uh18.traveltalk.android.backend.TravelTalkClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

class TravelTalkClientMock : TravelTalkClient {

    private val behavior = NetworkBehavior.create()

    private lateinit var mock: MockRetrofit

    override fun createChatService(): ChatApi {
        initBuilder()

        val delegate = mock.create(ChatApi::class.java)

        return ChatApiMock(delegate)
    }

    
    override fun createLocationService(): LocationApi {
        initBuilder()

        val delegate = mock.create(LocationApi::class.java)

        return LocationApiMock(delegate)
    }

    private fun initBuilder() {

        behavior.setFailurePercent(0)
        behavior.setErrorPercent(0)

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SERVICE_URL)
                .build()

        mock = MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior).build()

    }
}