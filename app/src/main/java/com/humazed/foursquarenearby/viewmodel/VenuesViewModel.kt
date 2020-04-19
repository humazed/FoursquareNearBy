package com.humazed.foursquarenearby.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.humazed.foursquarenearby.SIZE
import com.humazed.foursquarenearby.api.api
import com.humazed.foursquarenearby.model.explore.Venue
import humazed.github.com.kotlinandroidutils.d
import humazed.github.com.kotlinandroidutils.er
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class VenuesViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationContext = application.applicationContext

    private val disposables = CompositeDisposable()

    private val venues = MutableLiveData<List<Pair<Venue, String>>>()
    private val loadError = MutableLiveData<Boolean>(false)
    private val loading = MutableLiveData<Boolean>(false)

    fun getVenues(): LiveData<List<Pair<Venue, String>>> = venues
    fun isLoading(): LiveData<Boolean> = loading
    fun hasError(): LiveData<Boolean> = loadError

    fun loadVenues(latitude: Float, longitude: Float) {
        d { "latitude = [${latitude}], longitude = [${longitude}]" }
        loading.value = true
        disposables.add(
            applicationContext.api.getVenues("$latitude,$longitude", 1000.0, 2)
                .map { exploreResponse -> exploreResponse.response?.groups?.get(0)?.items?.mapNotNull { it.venue } }
                .flattenAsObservable { it }
                .flatMap { venue ->
                    Observable.zip(
                        Observable.just(venue),
                        getPhoto(venue).toObservable(),
                        BiFunction<Venue, String, Pair<Venue, String>> { first, second -> first to second }
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribeWith(object :
                    DisposableSingleObserver<List<Pair<Venue, String>>>() {
                    override fun onSuccess(value: List<Pair<Venue, String>>) {
                        loadError.value = false

                        venues.value = value

                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        er { e }
                        loadError.value = true
                        loading.value = false
                    }
                })
        )
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