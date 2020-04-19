package com.humazed.foursquarenearby.ui

import android.Manifest
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.humazed.foursquarenearby.KEY_LATITUDE
import com.humazed.foursquarenearby.KEY_LIVE_LOCATION
import com.humazed.foursquarenearby.KEY_LONGITUDE
import com.humazed.foursquarenearby.R
import com.humazed.foursquarenearby.location.CurrentLocationListener
import com.humazed.foursquarenearby.persestance.VenueEntity
import com.humazed.foursquarenearby.viewmodel.VenuesViewModel
import humazed.github.com.kotlinandroidutils.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.support.v4.onRefresh
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.util.concurrent.TimeUnit

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    private val viewModel: VenuesViewModel by viewModels()

    private val disposables = CompositeDisposable()

    private var adapter: BaseQuickAdapter<VenueEntity, KBaseViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!liveLocationSwitch.isChecked) loadVenues()

        observableViewModel()

        swipeLayout.onRefresh { loadVenues() }

        setupLiveLocationSwitch()

        getLocationUpdatesWithPermissionCheck()

        setupNetworkErrorView()
    }


    private fun loadVenues() {
        if (defaultSharedPrefs.contains(KEY_LATITUDE)) {
            viewModel.loadVenues(
                defaultSharedPrefs.getFloat(KEY_LATITUDE, 0f),
                defaultSharedPrefs.getFloat(KEY_LONGITUDE, 0f)
            )
        }
    }

    private fun observableViewModel() {
        viewModel.getVenues().observe(
            this,
            Observer { venues ->
                d { "getVenues" }
                adapter = setupRecyclerView(venues)
            }
        )

        viewModel.hasError().observe(
            this,
            Observer { hasError ->
                d { "hasError" }
                venuesRecyclerView.visible = !hasError

                errorTv.visible = hasError
                if (hasError) errorTv.text = getString(R.string.loading_error)
            }
        )

        viewModel.isLoading().observe(
            this,
            Observer { isLoading ->
                d { "isLoading" }
                swipeLayout.isRefreshing = isLoading
                errorTv.visible = !isLoading
                venuesRecyclerView.visible = !isLoading
            }
        )
    }

    private fun setupRecyclerView(venues: List<VenueEntity>): BaseQuickAdapter<VenueEntity, KBaseViewHolder> {
        val adapter = VenuesAdapter(venues)
        venuesRecyclerView.adapter = adapter


        layoutInflater.inflate(R.layout.content_list_empty, swipeLayout as ViewGroup, false)
            ?.apply {
                adapter.emptyView = this
            }

        return adapter
    }

    private fun setupLiveLocationSwitch() {
        liveLocationSwitch.isChecked = defaultSharedPrefs.getBoolean(KEY_LIVE_LOCATION, true)

        liveLocationSwitch.onCheckedChange { _, isChecked ->
            if (isChecked) loadVenues()
            defaultSharedPrefs.edit {
                putBoolean(KEY_LIVE_LOCATION, isChecked)
            }
        }
    }

    private fun setupNetworkErrorView() {
        ReactiveNetwork.observeNetworkConnectivity(applicationContext)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { connectivity ->
                d { connectivity.toString() }
                viewModel.getVenues().observe(this,
                    Observer { venues ->
                        if (connectivity.available()) {
                            networkErrorView.hide()
                            venuesRecyclerView.show()

                            if (venues.isNullOrEmpty()) loadVenues()

                        } else if (venues.isNullOrEmpty()) {
                            networkErrorView.show()
                            errorTv.hide()
                            swipeLayout.isRefreshing = false
                            adapter?.emptyView?.hide()
                        }
                    }
                )
            }
            .apply { disposables.add(this) }

        networkErrorView.setRetryListener { loadVenues() }
    }


    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getLocationUpdates() {
        disposables.add(
            Flowable.fromPublisher(
                LiveDataReactiveStreams.toPublisher(
                    this,
                    CurrentLocationListener.getInstance(applicationContext)
                )
            )
                .throttleFirst(10, TimeUnit.SECONDS)
                .subscribe { location ->
                    if (location != null) {
                        val latitude = location.latitude.toFloat()
                        val longitude = location.longitude.toFloat()

                        //don't reload id the latLng didn't change
                        val oldLatitude = defaultSharedPrefs.getFloat(KEY_LATITUDE, 0f)
                        val oldLongitude = defaultSharedPrefs.getFloat(KEY_LONGITUDE, 0f)
                        if (liveLocationSwitch.isChecked &&
                            (oldLatitude != latitude || oldLongitude != longitude)
                        ) {
                            viewModel.loadVenues(latitude, longitude)
                        }

                        defaultSharedPrefs.edit {
                            putFloat(KEY_LATITUDE, latitude)
                            putFloat(KEY_LONGITUDE, longitude)
                        }
                    }

                }
        )
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        AlertDialog.Builder(this)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                getLocationUpdatesWithPermissionCheck()
                dialog.dismiss()
            }
            .setCancelable(false)
            .setMessage(R.string.location_permeation_denied)
            .show()
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationNeverAskAgain() {
        AlertDialog.Builder(this)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .setMessage(R.string.location_permeation_never_ask_again)
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
