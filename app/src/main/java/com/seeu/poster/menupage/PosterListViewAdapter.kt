package com.seeu.poster.menupage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.seeu.poster.R
import com.seeu.poster.homepage.Poster
import com.seeu.poster.tools.GlideApp
import com.seeu.poster.tools.GlideRoundTransform
import com.seeu.poster.tools.dateFormat

/**
 * @author Gotcha
 * @date 2018/2/25
 */
class PosterListViewAdapter(context: Context?,val resource: Int, objects: MutableList<Poster>?) : ArrayAdapter<Poster>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val poster = getItem(position)
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null)
            viewHolder = ViewHolder(view.findViewById(R.id.poster_in_listView), view.findViewById(R.id.title_in_listView), view.findViewById(R.id.date_in_listView), view.findViewById(R.id.favourite_star))
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        GlideApp.with(context)
                .load(poster.imagePath)
                .transform(MultiTransformation(CenterCrop() ,GlideRoundTransform(4)))
                .into(viewHolder.posterImage)
        viewHolder.posterTitle.text = poster.title
        val dateString = "${dateFormat.format(poster.beginDate)} 至 ${dateFormat.format(poster.endDate)}"
        viewHolder.posterDate.text = dateString
        if (poster.isFavourite) {
            viewHolder.favouriteStar.setImageResource(R.drawable.symbols_star_light)
        }

        return view
    }

    private data class ViewHolder(val posterImage: ImageView, val posterTitle: TextView, val posterDate: TextView, val favouriteStar: ImageView)
}