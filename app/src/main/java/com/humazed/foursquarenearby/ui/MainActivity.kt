package com.humazed.foursquarenearby.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.humazed.foursquarenearby.R
import com.humazed.foursquarenearby.model.explore.Venue
import com.humazed.foursquarenearby.viewmodel.VenuesViewModel
import humazed.github.com.kotlinandroidutils.KBaseViewHolder
import humazed.github.com.kotlinandroidutils.visible
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.support.v4.onRefresh

class MainActivity : AppCompatActivity() {
    private val viewModel: VenuesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.loadVenues()

        observableViewModel()

        swipeLayout.onRefresh { viewModel.loadVenues() }
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
                vernuesRecyclerView.visible = !hasError

                errorTv.visible = hasError
                if (hasError) errorTv.text = getString(R.string.loading_error)
            }
        )

        viewModel.isLoading().observe(
            this,
            Observer { isLoading ->
                swipeLayout.isRefreshing = isLoading
                errorTv.visible = !isLoading
                vernuesRecyclerView.visible = !isLoading
            }
        )
    }

    private fun setupRecyclerView(venues: List<Pair<Venue, String>>): BaseQuickAdapter<Pair<Venue, String>, KBaseViewHolder> {
        val adapter = VenuesAdapter(venues)
        vernuesRecyclerView.adapter = adapter


        layoutInflater.inflate(R.layout.content_list_empty, swipeLayout as ViewGroup, false)
            ?.apply {
                adapter.emptyView = this
            }

        return adapter
    }

}
