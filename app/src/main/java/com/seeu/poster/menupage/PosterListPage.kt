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


        posters.add(Poster("http://pic117.nipic.com/file/20161208/10782555_090217062030_2.jpg", "中国梦，中国大学生之梦", "南科大讲堂", dateFormat.parse("04-28 19:00"), dateFormat.parse("04-28 21:00"), 125, true, intro = "郑强先后主持国家科技部支撑项目、国家重点基础研究发展规划（973）子课题、国家高技术研究发展计划（863）重点课题、国家自然科学基金重点项目、国家自然科学基金等国家级研究项目30余项。在Macromolecules, Polymer, Carbon, Macromol. Chem.&Phys., J. Polym. Sci. Part B: Polym. Phys., Chinese J. Polym. Sci., J. Mater. Res.,《高等学校化学学报》，《高分子学报》等重要学术刊物上发表SCI收录论文420余篇，SCI他引3800余次。获国家授权专利38项。获省部级科技奖励一等奖3项、二等奖1项。应用研究成果已产生经济效益逾 30 亿元。\n" +
                "他在浙江乃至全国出名，并不仅仅是因为他的学术成果。近十年间，他在全国20多个省市的高校、企业、部队、机关、中学、乃至小学和幼儿园进行了200多场的演讲。他的演讲与他所研究的高分子领域关系不大，但却涉及人才、科研、教育等与高校和人才息息相关的话题，赢得100多次热烈鼓掌常见于每场演讲。中央电视台《东方时空》栏目、中央电视台《开讲啦》、《青年中国说》栏目，中央教育电视台《教育人生》栏目、《人民日报》、《中国青年报》等予以专题报道，深受广大青年学生欢迎，产生了广泛的社会反响，被誉为“中国教育郑能量”。"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531679970140&di=98e17f0314f9dd6b5329cc75ce8c8d9e&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F29%2F90%2F38m58PICgYy_1024.jpg", "“礼”遇南科", "艺术中心", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-30 19:00"), 50, intro = "“礼”遇南科“礼”遇南科“礼”遇南科“礼”遇南科“礼”遇南科"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680004607&di=82c61bd6e30a7504b628b9ee95d24e5b&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F013e44594e1cd1a8012193a3ee99f4.jpg%401280w_1l_2o_100sh.jpg", "致：我们或将失去的珊瑚礁", "DFCx潜爱交流会", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00"), 25, true, intro = "我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680027106&di=e671f24ffff72dd1bcf8f0051a9205e2&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F19%2F36%2F68%2F52Z58PICC2a_1024.jpg", "计算机科学与工程系推介会", "计算机系", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00"), 23, intro = "计算机科学与工程系推介会计算机科学与工程系推介会计算机科学与工程系推介会"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680077934&di=09522507d122522f6d01b12696326e2a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc767ee46c9db00baa1cd112a2d.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00"), 0, true, intro = "高教纵横高教纵横高教纵横高教纵横高教纵横高教纵横"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680404683&di=e801d07d1a2a313df850690b5e8eee5b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc70835e6c5da00baa1cd112ae5.jpg", "Optogenetics", "生物医学工程系", dateFormat.parse("02-26 10:00"), dateFormat.parse("02-26 11:30"), 4, intro = "OptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogenetics"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680155804&di=e398e9986fe59aedd5c899297a3e89be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8718367adab44aed517b0ed8b81c8701a18bfbd1.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00"), 152, intro = "基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟"))

        listViewAdapter = PosterListViewAdapter(main, R.layout.poster_listview_item, posters)
        listView.adapter = listViewAdapter
    }

    private fun showFavourite() {
        val posters = ArrayList<Poster>()

        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680077934&di=09522507d122522f6d01b12696326e2a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc767ee46c9db00baa1cd112a2d.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00"), 0, true, intro = "高教纵横高教纵横高教纵横高教纵横高教纵横高教纵横"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680404683&di=e801d07d1a2a313df850690b5e8eee5b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc70835e6c5da00baa1cd112ae5.jpg", "Optogenetics", "生物医学工程系", dateFormat.parse("02-26 10:00"), dateFormat.parse("02-26 11:30"), 4, intro = "OptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogenetics"))
        posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680155804&di=e398e9986fe59aedd5c899297a3e89be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8718367adab44aed517b0ed8b81c8701a18bfbd1.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00"), 152, intro = "基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟"))

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

    inner class LoadFavouriteListThread : Thread() {
        override fun run() {
            super.run()
            main.runOnUiThread {
                showFavourite()
            }
        }
    }
}