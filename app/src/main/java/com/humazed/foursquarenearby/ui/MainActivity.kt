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
import com.humazed.foursquarenearby.KEY_LATITUDE
import com.humazed.foursquarenearby.KEY_LIVE_LOCATION
import com.humazed.foursquarenearby.KEY_LONGITUDE
import com.humazed.foursquarenearby.R
import com.humazed.foursquarenearby.location.CurrentLocationListener
import com.humazed.foursquarenearby.persestance.VenueEntity
import com.humazed.foursquarenearby.viewmodel.VenuesViewModel
import humazed.github.com.kotlinandroidutils.*
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadVenues()

        observableViewModel()

        swipeLayout.onRefresh { loadVenues() }

        setupLiveLocationSwitch()

        getLocationUpdatesWithPermissionCheck()
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
                setupRecyclerView(venues)
            }
        )

        viewModel.hasError().observe(
            this,
            Observer { hasError ->
                venuesRecyclerView.visible = !hasError

                errorTv.visible = hasError
                if (hasError) errorTv.text = getString(R.string.loading_error)
            }
        )

        viewModel.isLoading().observe(
            this,
            Observer { isLoading ->
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

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getLocationUpdates() {
        disposables.add(
            Flowable.fromPublisher(
                LiveDataReactiveStreams.toPublisher(
                    this,
                    CurrentLocationListener.getInstance(applicationContext)
                )
            )
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe { location ->
                    if (location != null) {
                        val latitude = location.latitude.toFloat()
                        val longitude = location.longitude.toFloat()

                        if (liveLocationSwitch.isChecked) viewModel.loadVenues(latitude, longitude)

                        defaultSharedPrefs.edit {
                            putFloat(KEY_LATITUDE, latitude)
                            putFloat(KEY_LONGITUDE, longitude)
                        }

                        d { "Location Changed " + location.latitude + " : " + location.longitude }
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
