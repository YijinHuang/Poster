package com.seeu.poster.homepage

import android.Manifest
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.seeu.poster.R
import com.seeu.poster.mWidgets.ViewGroupWithMenu
import com.seeu.poster.menupage.PosterListPage
import com.seeu.poster.menupage.SettingPage
import com.seeu.poster.tools.StatusBarCompat
import com.seeu.poster.tools.dateFormat
import java.util.*

class Main : AppCompatActivity() {
    lateinit var mainView: View
    lateinit var posterListView: View
    lateinit var settingView: View

    private lateinit var posterListPage: PosterListPage
    private lateinit var settingPage: SettingPage

    private lateinit var viewGroup: ViewGroupWithMenu
    private lateinit var viewPager: PostersViewPager

    //widgets in homepage
    private lateinit var title: TextView
    private lateinit var holder: TextView
    private lateinit var currentPosition: TextView
    private lateinit var postersNumber: TextView
    private lateinit var posterType: TextView

    private lateinit var posterViewPagerAdapter: PosterViewPagerAdapter

    private var currentPosterType = 0
    private val postersTypes = PosterType.values()

    var onOtherPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        posterListView = LayoutInflater.from(this).inflate(R.layout.poster_list, null)
        settingView = LayoutInflater.from(this).inflate(R.layout.settingpage, null)
        setContentView(mainView)
        loadHomepage()
    }

    private fun loadHomepage() {
        StatusBarCompat.compat(this, MColor.MGARY.rgb)
        initialMenu()
        initialHomepage()
        LoadPostersThread().start()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun initialHomepage() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, 1)

        viewGroup = findViewById(R.id.viewGroup)
        viewPager = findViewById(R.id.poster_views)

        title = findViewById(R.id.poster_title)
        holder = findViewById(R.id.poster_holder)
        currentPosition = findViewById(R.id.current_position)
        postersNumber = findViewById(R.id.posters_number)
        posterType = findViewById(R.id.poster_type)

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                refreshInfo(position)
            }
        })

        posterType.text = postersTypes[currentPosterType].content
        val changePosterButton = findViewById<ImageButton>(R.id.change_poster_type)
        changePosterButton.setOnClickListener {
            currentPosterType = (currentPosterType+1) % postersTypes.size
            posterType.text = postersTypes[currentPosterType].content
            getPoster()
            showPoster()
        }

        val menuButton = findViewById<ImageButton>(R.id.menu)
        menuButton.setOnClickListener {
            viewGroup.toggleMenu()
        }

        viewGroup.setOnScrollChangeListener { _, _, _, _, _ ->
            if (viewGroup.scrollX == 0) {
                viewPager.isEnableSwiping = true
                StatusBarCompat.compat(this, MColor.MGARY.rgb)
            } else {
                viewPager.isEnableSwiping = false
                StatusBarCompat.compat(this, MColor.MWHITE.rgb)
            }
        }
    }

    private fun initialMenu() {
        val entryToAccount = findViewById<LinearLayout>(R.id.menu_accout_entry)
        val entryToFavourite = findViewById<LinearLayout>(R.id.menu_favourite_entry)
        val entryToHistory = findViewById<LinearLayout>(R.id.menu_history_entry)
        val entryToUpload = findViewById<LinearLayout>(R.id.menu_upload_entry)
        val entryToSetting = findViewById<LinearLayout>(R.id.menu_setting_entry)
        val entryToAbout = findViewById<LinearLayout>(R.id.menu_about_entry)

        posterListPage = PosterListPage(this, posterListView)
        settingPage = SettingPage(this, settingView)


        entryToHistory.setOnClickListener {
            setContentView(posterListView)
            posterListPage.loadHistoryView()
            onOtherPage = true
        }

        entryToFavourite.setOnClickListener {
            setContentView(posterListView)
            posterListPage.loadFavouriteView()
            onOtherPage = true
        }

        entryToSetting.setOnClickListener {
            setContentView(settingView)
            onOtherPage = true
        }
    }

    private fun showPoster() {
        viewPager.adapter = posterViewPagerAdapter
        refreshInfo(0)
        postersNumber.text = posterViewPagerAdapter.count.toString()
    }

    private fun refreshInfo(position: Int) {
        val item = posterViewPagerAdapter.getPosterItem(position)
        title.text = item.title
        holder.text = item.holder
        currentPosition.text = (position+1).toString()
    }

    private fun getPoster(){
        val posters = ArrayList<Poster>()

        when (currentPosterType) {
            0 -> {
                posters.add(Poster("https://images3.alphacoders.com/913/913202.jpg", "“礼”遇南科", "艺术中心", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-30 19:00")))
                posters.add(Poster("https://images7.alphacoders.com/913/913095.png", "致：我们或将失去的珊瑚礁", "DFCx潜爱交流会", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00")))
                posters.add(Poster("https://images8.alphacoders.com/912/912951.jpg", "计算机科学与工程系推介会", "计算机系", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00")))
                posters.add(Poster("https://images.alphacoders.com/912/912663.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00")))
            }
            1 -> {
                posters.add(Poster("https://images5.alphacoders.com/912/912437.jpg", "Optogenetics", "生物医学工程系", dateFormat.parse("02-26 10:00"), dateFormat.parse("02-26 11:30")))
                posters.add(Poster("https://images4.alphacoders.com/912/912559.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00")))
            }
        }
        posterViewPagerAdapter = PosterViewPagerAdapter(this, posters)
    }

    override fun onBackPressed() {
        if (onOtherPage) {
            setContentView(mainView)
        } else {
            super.onBackPressed()
        }
    }

    inner class LoadPostersThread: Thread() {
        override fun run() {
            super.run()
            runOnUiThread {
                getPoster()
                showPoster()
            }
        }
    }

    enum class PosterType(val content: String) {
        ACTIVITY("活动"), LECTURE("讲座")
    }

    enum class MColor(val rgb: Int) {
        MGARY(Color.rgb(114, 114, 114)), MWHITE(Color.rgb(250, 250, 250))
    }
}
