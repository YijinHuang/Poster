package com.seeu.poster.homepage

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager

/**
 * @author Gotcha
 * @date 2018/2/25
 */

class PostersViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private val screenWidth: Int
    private val enableSwipeWidth: Int
    private var beginX: Int = 0
    var isEnableSwiping = true

    init {
        val metrics = DisplayMetrics()
        val manager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        manager.defaultDisplay.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        enableSwipeWidth = (screenWidth * 0.9).toInt()
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev?.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                beginX = ev.x.toInt()
            }
        }

        return if (isEnableSwiping && beginX < enableSwipeWidth) super.onTouchEvent(ev) else false

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnableSwiping && beginX < enableSwipeWidth) super.onInterceptTouchEvent(ev) else false
    }
}