package com.seeu.poster.tools

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author Gotcha
 * @date 2018/7/3
 */

fun isWifi(mContext: Context): Boolean {
    val connectivityManager = mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetInfo = connectivityManager.activeNetworkInfo
    return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
}
