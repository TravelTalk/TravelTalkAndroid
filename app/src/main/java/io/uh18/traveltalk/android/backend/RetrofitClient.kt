package io.uh18.traveltalk.android.backend

import retrofit2.Retrofit


class RetrofitClient {

    private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://infra.uh:5000/")
            .build()

    var service = retrofit.create<LocationApi>(LocationApi::class.java)

}