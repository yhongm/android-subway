package com.yhongm.subway.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView

class CustomScrollView(context: Context, attrs: AttributeSet) : ScrollView(context, attrs) {

    private var mGestureDetector: GestureDetector? = null


    init {
        mGestureDetector = GestureDetector(YScrollDetector())
        setFadingEdgeLength(0)

    }

    //通过手势判断，来判断是否拦截触摸事件。
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var result: Boolean = false
        try {
            result = super.onInterceptTouchEvent(ev) && mGestureDetector!!.onTouchEvent(ev)
        } catch (e: Exception) {
        }

        return result
    }

    // Return false if we're scrolling in the x direction

    inner class YScrollDetector : GestureDetector.SimpleOnGestureListener() {

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            return Math.abs(distanceY) > Math.abs(distanceX)
        }
    }
}