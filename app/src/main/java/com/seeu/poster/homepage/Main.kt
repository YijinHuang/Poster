package com.seeu.poster.homepage

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.seeu.poster.R
import com.seeu.poster.mWidgets.ViewGroupWithMenu
import com.seeu.poster.menupage.PosterListPage
import com.seeu.poster.menupage.SettingPage
import com.seeu.poster.tools.StatusBarCompat
import com.seeu.poster.tools.dateFormat
import com.seeu.poster.tools.isWifi
import java.util.*


class Main : AppCompatActivity() {
    lateinit var mainView: View
    lateinit var posterListView: View
    lateinit var settingView: View
    lateinit var detailView: View
    lateinit var photoView: View
    lateinit var aboutView: View

    lateinit var posterListPage: PosterListPage
    lateinit var settingPage: SettingPage

    private lateinit var viewGroup: ViewGroupWithMenu
    lateinit var viewPager: PostersViewPager

    //widgets in homepage
    private lateinit var title: TextView
    private lateinit var holder: TextView
    private lateinit var currentPosition: TextView
    private lateinit var postersNumber: TextView
    private lateinit var posterType: TextView

    private lateinit var posterViewPagerAdapter: PosterViewPagerAdapter

    private var currentPosterType = 0
    private val postersTypes = PosterType.values()

    val fullScreenCode = WindowManager.LayoutParams.FLAG_FULLSCREEN

    var onOtherPage = false
    var onDetailPage = false
    var onPhotoViewPage = false
    var lastViewOfDetailPage = 0

    var isWifi = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_main, null)
        posterListView = LayoutInflater.from(this).inflate(R.layout.poster_list, null)
        settingView = LayoutInflater.from(this).inflate(R.layout.setting_page, null)
        detailView = LayoutInflater.from(this).inflate(R.layout.detail_page, null)
        photoView = LayoutInflater.from(this).inflate(R.layout.photoview_page, null)
        aboutView = LayoutInflater.from(this).inflate(R.layout.about_page, null)
        photoView.setBackgroundColor(MColor.MBLACK.rgb)
        setContentView(mainView)
        loadHomepage()
        envInfoDetect()
    }

    private fun loadHomepage() {
        StatusBarCompat.compat(this, MColor.MGARY.rgb)
        initialMenu()
        initialHomepage()
        LoadPostersThread().start()
    }

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

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                refreshInfo(position)
            }
        })

        posterType.text = postersTypes[currentPosterType].content
        val changePosterButton = findViewById<ImageButton>(R.id.change_poster_type)
        changePosterButton.setOnClickListener {
            currentPosterType = (currentPosterType + 1) % postersTypes.size
            posterType.text = postersTypes[currentPosterType].content
            getPoster()
            showPoster()
        }

        val menuButton = findViewById<ImageButton>(R.id.menu)
        menuButton.setOnClickListener {
            viewGroup.toggleMenu()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
    }

    private fun initialMenu() {
//        val entryToAccount = findViewById<LinearLayout>(R.id.menu_accout_entry)
        val entryToFavourite = findViewById<LinearLayout>(R.id.menu_favourite_entry)
        val entryToHistory = findViewById<LinearLayout>(R.id.menu_history_entry)
//        val entryToUpload = findViewById<LinearLayout>(R.id.menu_upload_entry)
        val entryToSetting = findViewById<LinearLayout>(R.id.menu_setting_entry)
        val entryToAbout = findViewById<LinearLayout>(R.id.menu_about_entry)

        posterListPage = PosterListPage(this, posterListView)
        settingPage = SettingPage(this, settingView)


        entryToHistory.setOnClickListener {
            setContentView(posterListView)
            posterListPage.loadHistoryView()
            lastViewOfDetailPage = 1
            onOtherPage = true
        }

        entryToFavourite.setOnClickListener {
            setContentView(posterListView)
            posterListPage.loadFavouriteView()
            lastViewOfDetailPage = 1
            onOtherPage = true
        }

        entryToSetting.setOnClickListener {
            setContentView(settingView)
            onOtherPage = true
        }

        entryToAbout.setOnClickListener {
            setContentView(aboutView)
            val pageTitle = aboutView.findViewById<TextView>(R.id.page_title_string)
            pageTitle.text = this.getText(R.string.about)
            onOtherPage = true
        }
    }

    private fun envInfoDetect() {
        isWifi = isWifi(this)
        if (!isWifi) {
            Toast.makeText(this.applicationContext, "当前在非WIFI环境", Toast.LENGTH_SHORT).show()
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
        currentPosition.text = (position + 1).toString()
    }

    private fun getPoster() {
        val posters = ArrayList<Poster>()

        when (currentPosterType) {
            0 -> {
                posters.add(Poster("http://pic117.nipic.com/file/20161208/10782555_090217062030_2.jpg", "中国梦，中国大学生之梦", "南科大讲堂", dateFormat.parse("04-28 19:00"), dateFormat.parse("04-28 21:00"), 125, true, intro = "郑强先后主持国家科技部支撑项目、国家重点基础研究发展规划（973）子课题、国家高技术研究发展计划（863）重点课题、国家自然科学基金重点项目、国家自然科学基金等国家级研究项目30余项。在Macromolecules, Polymer, Carbon, Macromol. Chem.&Phys., J. Polym. Sci. Part B: Polym. Phys., Chinese J. Polym. Sci., J. Mater. Res.,《高等学校化学学报》，《高分子学报》等重要学术刊物上发表SCI收录论文420余篇，SCI他引3800余次。获国家授权专利38项。获省部级科技奖励一等奖3项、二等奖1项。应用研究成果已产生经济效益逾 30 亿元。\n" +
                        "他在浙江乃至全国出名，并不仅仅是因为他的学术成果。近十年间，他在全国20多个省市的高校、企业、部队、机关、中学、乃至小学和幼儿园进行了200多场的演讲。他的演讲与他所研究的高分子领域关系不大，但却涉及人才、科研、教育等与高校和人才息息相关的话题，赢得100多次热烈鼓掌常见于每场演讲。中央电视台《东方时空》栏目、中央电视台《开讲啦》、《青年中国说》栏目，中央教育电视台《教育人生》栏目、《人民日报》、《中国青年报》等予以专题报道，深受广大青年学生欢迎，产生了广泛的社会反响，被誉为“中国教育郑能量”。"))
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531679970140&di=98e17f0314f9dd6b5329cc75ce8c8d9e&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F15%2F29%2F90%2F38m58PICgYy_1024.jpg", "“礼”遇南科", "艺术中心", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-30 19:00"), 50, intro = "“礼”遇南科“礼”遇南科“礼”遇南科“礼”遇南科“礼”遇南科"))
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680004607&di=82c61bd6e30a7504b628b9ee95d24e5b&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F013e44594e1cd1a8012193a3ee99f4.jpg%401280w_1l_2o_100sh.jpg", "致：我们或将失去的珊瑚礁", "DFCx潜爱交流会", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00"), 25, true, intro = "我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁我们或将失去的珊瑚礁"))
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680027106&di=e671f24ffff72dd1bcf8f0051a9205e2&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F19%2F36%2F68%2F52Z58PICC2a_1024.jpg", "计算机科学与工程系推介会", "计算机系", dateFormat.parse("03-01 19:00"), dateFormat.parse("03-01 21:00"), 23, intro = "计算机科学与工程系推介会计算机科学与工程系推介会计算机科学与工程系推介会"))
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680077934&di=09522507d122522f6d01b12696326e2a&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc767ee46c9db00baa1cd112a2d.jpg", "高教纵横", "高等教育研究中心", dateFormat.parse("01-01 00:00"), dateFormat.parse("01-01 00:00"), 0, true, intro = "高教纵横高教纵横高教纵横高教纵横高教纵横高教纵横"))
            }
            1 -> {
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680404683&di=e801d07d1a2a313df850690b5e8eee5b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F6609c93d70cf3bc70835e6c5da00baa1cd112ae5.jpg", "Optogenetics", "生物医学工程系", dateFormat.parse("02-26 10:00"), dateFormat.parse("02-26 11:30"), 4, intro = "OptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogeneticsOptogenetics"))
                posters.add(Poster("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531680155804&di=e398e9986fe59aedd5c899297a3e89be&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F8718367adab44aed517b0ed8b81c8701a18bfbd1.jpg", "基于虚拟区域方法的颗粒悬浮流直接数值模拟", "力学与航空航天系", dateFormat.parse("01-18 16:00"), dateFormat.parse("01-18 17:00"), 152, intro = "基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟基于虚拟区域方法的颗粒悬浮流直接数值模拟"))
            }
        }
        posterViewPagerAdapter = PosterViewPagerAdapter(this, posters)
    }

    override fun onBackPressed() {
        when {
            onPhotoViewPage -> {
                onPhotoViewPage = false
                window.clearFlags(fullScreenCode)
                setContentView(detailView)
            }
            onDetailPage -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                } else {
                    StatusBarCompat.compat(this, MColor.MGARY.rgb)
                }
                onDetailPage = false
                if (lastViewOfDetailPage == 1) {
                    setContentView(posterListView)
                } else {
                    setContentView(mainView)
                }
            }
            onOtherPage -> {
                onOtherPage = false
                setContentView(mainView)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onStop() {
        settingPage.editor.commit()
        super.onStop()
    }

    inner class LoadPostersThread : Thread() {
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
        MGARY(Color.rgb(114, 114, 114)), MWHITE(Color.rgb(250, 250, 250)), MBLACK(Color.rgb(0, 0, 0))
    }
}
