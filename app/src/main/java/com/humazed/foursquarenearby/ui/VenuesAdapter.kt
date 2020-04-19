package com.humazed.foursquarenearby.ui

import com.humazed.foursquarenearby.R
import com.humazed.foursquarenearby.model.explore.Venue
import humazed.github.com.kotlinandroidutils.BaseAdapter
import humazed.github.com.kotlinandroidutils.KBaseViewHolder
import humazed.github.com.kotlinandroidutils.load
import kotlinx.android.synthetic.main.row_venue.*

class VenuesAdapter(
    venues: List<Pair<Venue, String>>
) :
    BaseAdapter<Pair<Venue, String>>(R.layout.row_venue, venues) {

    override fun convert(holder: KBaseViewHolder, item: Pair<Venue, String>) {
        holder.apply {
            val (venue, imageUrl) = item

            nameTv.text = venue.name
            categoryTv.text = venue.categories?.get(0)?.shortName ?: ""
            distanceTv.text = venue.location?.distance?.toString() ?: ""

            image.load(imageUrl)
        }
    }
}
