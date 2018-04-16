package com.seeu.poster.menupage

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.seeu.poster.R
import com.seeu.poster.homepage.Main
import com.suke.widget.SwitchButton

/**
 * @author Gotcha
 * @date 2018/3/10
 */
class SettingPage(private val main: Main, private val settingView: View) {
    private val btPopMessage: SwitchButton
    private val btAutoAlarm: SwitchButton
    private val btUseMobileData: SwitchButton

    private val textClearCache: TextView
    private val textCheckUpgrade: TextView

    val allowPopMessage: Boolean = true
    val allowAutoAlarm: Boolean = false
    val allowUseMobileDate: Boolean = false

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
        btPopMessage = settingView.findViewById(R.id.pop_message_button)
        btAutoAlarm = settingView.findViewById(R.id.auto_alarm_button)
        btUseMobileData = settingView.findViewById(R.id.use_mobile_data_button)
        btPopMessage.isChecked = allowPopMessage
        btAutoAlarm.isChecked = allowAutoAlarm
        btUseMobileData.isChecked = allowUseMobileDate

        textClearCache = settingView.findViewById(R.id.clear_cache)
        textCheckUpgrade = settingView.findViewById(R.id.check_upgrade)
    }
}