package com.humazed.foursquarenearby.api

import android.content.Context
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.humazed.foursquarenearby.model.explore.ExploreResponse
import com.humazed.foursquarenearby.model.photos.PhotosResponse
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


@Keep
interface VenuesService {
    @GET("venues/explore")
    fun getVenues(
        @Query("ll") longLat: String,
        @Query("radius") radius: Double,
        @Query("limit") limit: Int
    ): Single<ExploreResponse>

    @GET("venues/{venueId}/photos")
    fun getVenuePhotos(
        @Path("venueId") id: String
    ): Single<PhotosResponse>
}


val Context.api: VenuesService
    get() {
        val api: VenuesService by lazy {
            val cacheSize = 10 * 1024 * 1024
            val cache = Cache(cacheDir, cacheSize.toLong())

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor())
                .cache(cache)
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.foursquare.com/v2/")
                .client(okHttpClient)
                .build()

            return@lazy retrofit.create(VenuesService::class.java)
        }
        return api
    }

val Fragment.api get() = requireContext().api

