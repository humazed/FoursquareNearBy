package com.humazed.foursquarenearby.ui

import com.humazed.foursquarenearby.R
import com.humazed.foursquarenearby.persestance.VenueEntity
import humazed.github.com.kotlinandroidutils.BaseAdapter
import humazed.github.com.kotlinandroidutils.KBaseViewHolder
import humazed.github.com.kotlinandroidutils.load
import kotlinx.android.synthetic.main.row_venue.*

class VenuesAdapter(
    venues: List<VenueEntity>
) :
    BaseAdapter<VenueEntity>(R.layout.row_venue, venues) {

    override fun convert(holder: KBaseViewHolder, item: VenueEntity?) {
        holder.apply {
            item?.apply {
                nameTv.text = name
                categoryTv.text = category
                distanceTv.text = mContext.getString(R.string.meters, distance)

                image.load(imageUrl)
            }

        }
    }
}
