package com.humazed.foursquarenearby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.humazed.foursquarenearby.SIZE
import com.humazed.foursquarenearby.api.api
import com.humazed.foursquarenearby.model.explore.Venue
import com.humazed.foursquarenearby.persestance.AppDatabase
import com.humazed.foursquarenearby.persestance.VenueEntity
import com.humazed.foursquarenearby.persestance.VenuesRepository
import humazed.github.com.kotlinandroidutils.d
import humazed.github.com.kotlinandroidutils.er
import humazed.github.com.kotlinandroidutils.isConnected
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class VenuesViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext

    private val disposables = CompositeDisposable()

    private val venuesRepository =
        VenuesRepository.getInstance(AppDatabase.getInstance(applicationContext).venuesDoa())


    private val loadError = MutableLiveData(false)
    private val loading = MutableLiveData(false)

    fun getVenues(): LiveData<List<VenueEntity>> = venuesRepository.getAllVenues()
    fun isLoading(): LiveData<Boolean> = loading
    fun hasError(): LiveData<Boolean> = loadError

    fun loadVenues(latitude: Float, longitude: Float) {
        d { "latitude = [${latitude}], longitude = [${longitude}]" }
        if (!applicationContext.isConnected()) {
            loading.value = false
            return
        }

        loading.value = true

        applicationContext.api.getVenues("$latitude,$longitude", 1000.0, 1)
            .map { exploreResponse ->
                exploreResponse.response?.groups?.get(0)?.items?.mapNotNull { it.venue }
            }
            .flattenAsObservable { it }
            .flatMapSingle { venue ->
                Single.zip(
                    Single.just(venue),
                    getPhoto(venue),
                    BiFunction<Venue, String, Pair<Venue, String>> { first, second -> first to second }
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribeBy(
                onSuccess = { value ->
                    loadError.value = false

                    venuesRepository.saveVenues(value)

                    loading.value = false
                },
                onError = { e ->
                    er { e }
                    loadError.value = true
                    loading.value = false
                }
            )
            .apply { disposables.add(this) }
    }

    // this doesn't always work because of the rate limiting of the api.
    private fun getPhoto(venue: Venue): Single<String> {
        return applicationContext.api.getVenuePhotos(venue.id)
            .map { photoResponse ->
                if (photoResponse.response?.photos?.items?.isNotEmpty() == true) {
                    return@map "${photoResponse.response.photos.items[0].prefix}$SIZE${photoResponse.response.photos.items[0].suffix}"
                } else {
                    return@map ""
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}