package com.seeu.poster.mWidgets

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Scroller

/**
 * @author Gotcha
 * @date 2018/2/24
 */
class ViewGroupWithMenu(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr) {
    private val screenWidth: Int
    private val screenHeight: Int
    private val halfOfScreenHeight: Int
    private val homepageContent: Int
    private val menuPadding: Int
    private val menuWidth: Int
    private val enableSwipeWidth: Int

    private lateinit var menu: ViewGroup

    private lateinit var homepage: ViewGroup
    private var lastX: Int = 0
    private var lastY: Int = 0
    private var beginX: Int = 0
    private var currentX: Int = 0
    private var currentY: Int = 0

    private var scroller: Scroller
    private var isOpen = false

    private var scale: Float = 0f

    init {
        val metrics = DisplayMetrics()
        val manager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        manager.defaultDisplay.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        halfOfScreenHeight = screenHeight / 2
        menuPadding = convertToDP(context, 150F).toInt()
        homepageContent = screenWidth
        enableSwipeWidth = (screenWidth * 0.9).toInt()
        menuWidth = screenWidth - menuPadding
        scroller = Scroller(context)
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        homepage = getChildAt(0) as ViewGroup
        menu = getChildAt(1) as ViewGroup
        menu.layoutParams.width = menuWidth
        measureChild(menu, widthMeasureSpec, heightMeasureSpec)
        measureChild(homepage, widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(homepageContent + menuWidth, screenHeight)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        homepage.layout(0, 0, screenWidth, screenHeight)
        menu.layout(screenWidth, 0, screenWidth + menuWidth, screenHeight)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x.toInt()
                lastY = event.x.toInt()
                beginX = lastX
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = event.x.toInt()
                currentY = event.x.toInt()
                val deviationX = currentX - lastX

                if (deviationX < 0) {
                    if (beginX > enableSwipeWidth) {
                        if (scrollX - deviationX >= menuWidth)
                            scrollTo(menuWidth, 0)
                        else
                            scrollBy(-deviationX, 0)
                    }
                } else {
                    if (scrollX - deviationX <= 0)
                        scrollTo(0, 0)
                    else
                        scrollBy(-deviationX, 0)
                }
                scaleHomepage()

                lastX = currentX
                lastY = currentY
            }
            MotionEvent.ACTION_UP -> {
                if (scrollX > menuWidth / 2) {
                    openMenu(menuWidth - scrollX)
                } else {
                    closeMenu(-scrollX)
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
        scale = Math.abs(scrollX.toFloat()) / menuWidth.toFloat()
        scaleHomepage()
    }

//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = true

    fun toggleMenu() {
        if (isOpen) {
            closeMenu(-scrollX)
        } else {
            openMenu(menuWidth)
        }
    }

    private fun openMenu(deviation: Int) {
        scroller.startScroll(scrollX, 0, deviation, 0, 300)
        isOpen = true
        invalidate()
    }

    private fun closeMenu(deviation: Int) {
        scroller.startScroll(scrollX, 0, deviation, 0, 300)
        isOpen = false
        invalidate()
    }

    private fun scaleHomepage() {
        homepage.scaleX = 1 - 0.05625f*scale
        homepage.pivotX = menuWidth.toFloat()
        homepage.pivotY = halfOfScreenHeight.toFloat()
        homepage.scaleY = 1 - 0.1f*scale
    }

    private fun convertToDP(context: Context, num: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, num, context.resources.displayMetrics)
}