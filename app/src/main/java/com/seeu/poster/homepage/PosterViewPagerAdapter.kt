package com.seeu.poster.homepage

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.seeu.poster.R
import com.seeu.poster.tools.GlideApp
import com.seeu.poster.tools.GlideRoundTransform

/**
 * @author Gotcha
 * @date 2018/2/16
 */
class PosterViewPagerAdapter(private val context: Context, private val posters: List<Poster>) : PagerAdapter() {
    //    private val viewCache: LinkedList<View> = LinkedList()
    //    private val builder = GenericDraweeHierarchyBuilder(context.resources)

    override fun getCount(): Int {
        return posters.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val poster = getPosterItem(position)
        val view = LayoutInflater.from(context).inflate(R.layout.poster_viewpager_item, null)
        val img = view.findViewById<ImageView>(R.id.viewpager_poster)
        GlideApp.with(context)
                .load(poster.imagePath)
                .transform(MultiTransformation(FitCenter() ,GlideRoundTransform(8)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(img)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun getPosterItem(position: Int): Poster {
        return posters[position]
    }

//    private data class ViewHolder(val poster: ImageView)
}