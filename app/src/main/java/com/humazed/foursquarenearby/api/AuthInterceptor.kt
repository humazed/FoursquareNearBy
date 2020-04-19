package com.humazed.foursquarenearby.api

import com.humazed.foursquarenearby.CLIENT_ID
import com.humazed.foursquarenearby.CLIENT_SECRET
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().newBuilder()
            .addQueryParameter("client_id",
                CLIENT_ID
            )
            .addQueryParameter("client_secret",
                CLIENT_SECRET
            )
            //https://developer.foursquare.com/docs/places-api/versioning/
            .addQueryParameter("v","20200419")
            .build()

        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(url)
                .build()
        )
    }

}