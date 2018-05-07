package com.seeu.poster.homepage

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager

/**
 * @author Gotcha
 * @date 2018/2/25
 */

class PostersViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    var isEnableSwiping = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableSwiping) super.onTouchEvent(ev) else false

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableSwiping) super.onInterceptTouchEvent(ev) else false
    }
}