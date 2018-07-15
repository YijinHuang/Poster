package com.seeu.poster.homepage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.chrisbanes.photoview.PhotoView
import com.seeu.poster.R
import com.seeu.poster.R.string.holder
import com.seeu.poster.R.string.intro
import com.seeu.poster.detailpage.DetailPage
import com.seeu.poster.tools.GlideApp
import com.seeu.poster.tools.GlideRoundTransform
import com.seeu.poster.tools.StatusBarCompat
import java.io.File
import java.io.FileOutputStream


/**
 * @author Gotcha
 * @date 2018/2/16
 */
class PosterViewPagerAdapter(private val main: Main, private val posters: List<Poster>) : PagerAdapter() {
    private val detailPage = DetailPage(main)

    override fun getCount(): Int {
        return posters.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val poster = getPosterItem(position)
        val view = LayoutInflater.from(main).inflate(R.layout.poster_viewpager_item, null)
        val img = view.findViewById<ImageView>(R.id.viewpager_poster)
        img.setOnClickListener {
            if (main.viewPager.isEnableSwiping)
                main.lastViewOfDetailPage = 0
                detailPage.toDetail(poster)
        }
        GlideApp.with(main)
                .load(poster.imagePath)
                .transform(MultiTransformation(FitCenter(), GlideRoundTransform(8)))
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