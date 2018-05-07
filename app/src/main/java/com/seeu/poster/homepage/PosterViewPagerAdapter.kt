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
    private val window = main.window

    private val detailImg = main.detailView.findViewById<ImageView>(R.id.detail_image)
    private val intro = main.detailView.findViewById<TextView>(R.id.detail_intro)
    private val title = main.detailView.findViewById<TextView>(R.id.detail_title)
    private val holder = main.detailView.findViewById<TextView>(R.id.detail_holder)
    private val star = main.detailView.findViewById<ImageView>(R.id.detail_star)
    private val favNum = main.detailView.findViewById<TextView>(R.id.detail_fav_num)
    private val photoView = main.photoView.findViewById<PhotoView>(R.id.photo_view)

    private lateinit var img: ImageView

    init {
        photoView.setOnClickListener {
            main.onBackPressed()
        }
        photoView.setOnLongClickListener {
            try {
                val dir = File(Environment.getExternalStorageDirectory().path + "/Poster/images")
                val file = File(dir.absolutePath + "/" + photoView.contentDescription + ".png")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                if (!file.exists()) {
                    file.createNewFile()
                }
                val out = FileOutputStream(file)
                img.isDrawingCacheEnabled = true
                img.drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, out)
                img.isDrawingCacheEnabled = false
                out.flush()
                out.close()

                Toast.makeText(main.applicationContext, "已保存图片至" + file.absolutePath, Toast.LENGTH_SHORT).show()
                main.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.absolutePath)))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            false
        }
    }

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
                toDetail(poster)
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

    private fun toDetail(poster: Poster) {
        main.setContentView(main.detailView)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            StatusBarCompat.compat(main, Main.MColor.MBLACK.rgb)
        }
        title.text = poster.title
        favNum.text = poster.favouriteNum.toString()
        holder.text = poster.holder
        intro.text = poster.intro
        if (poster.isFavourite) {
            star.setImageResource(R.drawable.symbols_star_light)
        } else {
            star.setImageResource(R.drawable.symbols_star_dark)
        }
        GlideApp.with(main)
                .load(poster.imagePath)
                .placeholder(ColorDrawable(Color.LTGRAY))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(detailImg)
        main.onDetailPage = true

        detailImg.setOnClickListener {
            main.onPhotoViewPage = true
            img = detailImg
            toPhotoView(poster)
        }
    }

    private fun toPhotoView(poster: Poster) {
        main.setContentView(main.photoView)
        window.addFlags(main.fullScreenCode)
        photoView.contentDescription = poster.title
        GlideApp.with(main)
                .load(poster.imagePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(photoView)
    }
//    private data class ViewHolder(val poster: ImageView)
}