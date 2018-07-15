package com.seeu.poster.menupage

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.seeu.poster.R
import com.seeu.poster.homepage.Main
import com.suke.widget.SwitchButton
import com.bumptech.glide.Glide
import android.os.Looper
import android.widget.Toast
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import java.io.File
import java.math.BigDecimal


/**
 * @author Gotcha
 * @date 2018/3/10
 */
class SettingPage(private val main: Main, private val settingView: View) {
//    private val btPopMessage: SwitchButton
//    private val btAutoAlarm: SwitchButton
    private val btUseMobileData: SwitchButton
//    var allowPopMessage = true
//    var allowAutoAlarm = false
    var allowUseMobileData = true

    private val textClearCache: TextView
    private val textCheckUpgrade: TextView

    private val sp = main.getSharedPreferences("preference", Context.MODE_PRIVATE)
    val editor = sp.edit()

    init {
        //Initial page title
        val pageTitle = settingView.findViewById<TextView>(R.id.page_title_string)
        pageTitle.text = main.getText(R.string.setting)

        val btBack = settingView.findViewById<ImageButton>(R.id.back)
        btBack.setOnClickListener {
            main.setContentView(main.mainView)
            main.onOtherPage = false
        }

        //Initial clickable widgets in the setting page
//        btPopMessage = settingView.findViewById(R.id.pop_message_button)
//        btAutoAlarm = settingView.findViewById(R.id.auto_alarm_button)
        btUseMobileData = settingView.findViewById(R.id.use_mobile_data_button)

        textClearCache = settingView.findViewById(R.id.clear_cache)
        textCheckUpgrade = settingView.findViewById(R.id.check_upgrade)

        initialButton()
    }

    private fun initialButton() {
//        allowPopMessage = sp.getBoolean("allowPopMessage", true)
//        btPopMessage.isChecked = allowPopMessage
//        if (allowPopMessage) {
//            allowAutoAlarm = sp.getBoolean("allowAutoAlarm", false)
//            btAutoAlarm.isChecked = allowAutoAlarm
//        } else {
//            allowAutoAlarm = false
//            btAutoAlarm.isEnabled = false
//        }
        allowUseMobileData = sp.getBoolean("allowUseMobileDate", false)
        btUseMobileData.isChecked = allowUseMobileData

//        btPopMessage.setOnCheckedChangeListener{
//            view, isChecked -> editor.putBoolean("allowPopMessage", isChecked)
//            if (!isChecked) {
//                btAutoAlarm.isChecked = false
//                btAutoAlarm.isEnabled = false
//                editor.putBoolean("allowAutoAlarm", false)
//            } else {
//                btAutoAlarm.isEnabled = true
//            }
//        }
//        btAutoAlarm.setOnCheckedChangeListener{
//            view, isChecked -> editor.putBoolean("allowAutoAlarm", isChecked)
//        }
        btUseMobileData.setOnCheckedChangeListener{
            view, isChecked -> editor.putBoolean("allowUseMobileDate", isChecked)
        }

        textClearCache.setOnClickListener {
            val cacheSize = getCacheSize(main)
            clearImageDiskCache(main)
            Toast.makeText(main.applicationContext, "已清除" + cacheSize + "缓存", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearImageDiskCache(context: Context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    Glide.get(context).clearDiskCache()
                    // BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                }).start()
            } else {
                Glide.get(context).clearDiskCache()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCacheSize(context: Context): String {
        try {
            return getFormatSize(getFolderSize(File(context.cacheDir.toString() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    @Throws(Exception::class)
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList!!) {
                size += if (aFileList.isDirectory) {
                    getFolderSize(aFileList)
                } else {
                    aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }


    private fun getFormatSize(size: Long): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte.toDouble()))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte.toDouble()))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte.toDouble()))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}