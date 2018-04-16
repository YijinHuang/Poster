package com.seeu.poster.menupage

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.seeu.poster.R
import com.seeu.poster.homepage.Poster
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

        viewHolder.posterImage.setImageURI(Uri.parse(poster.imagePath))
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