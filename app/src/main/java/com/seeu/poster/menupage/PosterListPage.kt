package com.seeu.poster.menupage

import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import com.seeu.poster.R
import com.seeu.poster.homepage.Main
import com.seeu.poster.homepage.Poster
import com.seeu.poster.tools.dateFormat
import java.util.*

/**
 * @author Gotcha
 * @date 2018/2/25
 */
class PosterListPage(private val main: Main, private val posterListView: View) {
    private lateinit var listView: ListView
    private lateinit var pageTitle: TextView
    private lateinit var listViewAdapter: PosterListViewAdapter
    private lateinit var historyPageTitle: String
    private lateinit var favouritePageTitle: String

    init {
        getValuesFromResource()
        initialPage()
    }

    fun loadHistoryView() {
        pageTitle.text = historyPageTitle
        LoadHistoryListThread().start()
    }

    fun loadFavouriteView() {
        pageTitle.text = favouritePageTitle
        LoadFavouriteListThread().start()
    }

    private fun initialPage() {
        with(posterListView) {
            listView = findViewById(R.id.history_posters)
            pageTitle = findViewById(R.id.page_title_string)

            val btBack = findViewById<ImageButton>(R.id.back)
            btBack.setOnClickListener {
                main.setContentView(main.mainView)
                main.onOtherPage = false
                listView.adapter = null
            }
        }
    }

    private fun showHistory() {
        val posters = ArrayList<Poster>()

        posters.add(Poster("https://images3.alphacoders.com/913/913202.jpg", "“礼”遇南科", "艺术中心", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-30 19:00"), true))
        posters.add(Poster("https://images7.alphacoders.com/913/913095.png", "致：我们或将失去的珊瑚礁", "DFCx潜爱交流会", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00")))
        posters.add(Poster("https://images8.alphacoders.com/912/912951.jpg", "计算机科学与工程系推介会", "计算机系", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00")))
        posters.add(Poster("https://images.alphacoders.com/912/912663.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00"), true))
        posters.add(Poster("https://images5.alphacoders.com/912/912437.jpg", "Optogenetics", "生物医学工程系", dateFormat.parse("02-26 10:00"), dateFormat.parse("02-26 11:30")))
        posters.add(Poster("https://images4.alphacoders.com/912/912559.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00"), true))

        listViewAdapter = PosterListViewAdapter(main, R.layout.poster_listview_item, posters)
        listView.adapter = listViewAdapter
    }

    private fun showFavourite() {
        val posters = ArrayList<Poster>()

        posters.add(Poster("https://images3.alphacoders.com/913/913202.jpg", "“礼”遇南科", "艺术中心", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-30 19:00"), true))
        posters.add(Poster("https://images.alphacoders.com/912/912663.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00"), true))
        posters.add(Poster("https://images4.alphacoders.com/912/912559.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00"), true))

        listViewAdapter = PosterListViewAdapter(main, R.layout.poster_listview_item, posters)
        listView.adapter = listViewAdapter
    }

    private fun getValuesFromResource() {
        historyPageTitle = main.getString(R.string.history)
        favouritePageTitle = main.getString(R.string.favourite)
    }

    inner class LoadHistoryListThread : Thread() {
        override fun run() {
            super.run()
            main.runOnUiThread {
                showHistory()
            }
        }
    }

    inner class LoadFavouriteListThread: Thread() {
        override fun run() {
            super.run()
            main.runOnUiThread {
                showFavourite()
            }
        }
    }
}