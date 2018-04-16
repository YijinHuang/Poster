package com.seeu.poster.homepage

import android.content.Context
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import com.seeu.poster.R
import com.seeu.poster.mWidgets.RoundImageView

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

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val poster = getPosterItem(position)
//        val view = SimpleDraweeView(context)
//        val hierarchy = builder.build()
//        val roundingParams = hierarchy.roundingParams
//        roundingParams!!.setCornersRadius(20f)
//        hierarchy.roundingParams = roundingParams
//        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
//        view.hierarchy = hierarchy
//        val roundingParams = view.hierarchy.roundingParams
//        roundingParams!!.setCornersRadius(20f)
//        hierarchy.roundingParams = roundingParams
        val view = LayoutInflater.from(context).inflate(R.layout.poster_viewpager_item, null)
        val img = view.findViewById<SimpleDraweeView>(R.id.viewpager_poster)
        img.setImageURI(Uri.parse(poster.imagePath))
        container?.addView(view)
        return view
    }

//    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
//        val poster = getPosterItem(position)
//        val view: View
//        val viewHolder: ViewHolder
//
//        if (viewCache.size == 0) {
//            view = LayoutInflater.from(context).inflate(R.layout.poster_viewpager_item, null)
//            viewHolder = ViewHolder(view.findViewById(R.id.viewpager_poster))
//        } else {
//            view = viewCache.removeFirst()
//            viewHolder = view.tag as ViewHolder
//        }
//
//        val bitmap = BitmapFactory.decodeFile(poster.imagePath)
//        viewHolder.poster.setImageBitmap(bitmap)
//
//        container?.addView(view)
//        return view
//    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as View)
    }

    fun getPosterItem(position: Int): Poster {
        return posters[position]
    }

//    private data class ViewHolder(val poster: ImageView)
}