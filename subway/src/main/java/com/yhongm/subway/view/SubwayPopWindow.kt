/**
 *                   Copyright [yhongm]
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at

 *         http://www.apache.org/licenses/LICENSE-2.0

 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */


package com.yhongm.subway.view

import android.content.Context
import android.os.Build
import android.support.v7.widget.AppCompatImageButton
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.yhongm.subway.R

class SubwayPopWindow constructor(context: Context) : PopupWindow(context) {
    private var popView: View? = null
    private var tvFirst: TextView? = null
    private var tvEnd: TextView? = null
    private var tvTitle: TextView? = null

    init {
        popView = LayoutInflater.from(context).inflate(R.layout.popwindow_subway, null)
        contentView = popView

        width = db2px(context, 260f)
        height = db2px(context, 200f)
        isFocusable = false
        if (Build.VERSION.SDK_INT > 21) {
            elevation = 5f
        }
        var btn = popView!!.findViewById<AppCompatImageButton>(R.id.pop_ib_close)

        tvFirst = popView!!.findViewById<TextView>(R.id.pop_tv_first)

        tvEnd = popView!!.findViewById<TextView>(R.id.pop_tv_end)
        tvTitle = popView!!.findViewById(R.id.pop_tv_title)
        btn.setOnClickListener {
            dismiss()
        }

    }

    fun setContent(first: String, end: String) {
        tvFirst!!.text = first
        tvEnd!!.text = end
    }

    fun db2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        val px = dp * scale + 0.5
        return px.toInt()
    }

    fun show(view: View, x: Int, y: Int) {
        showAtLocation(view!!, Gravity.CENTER, 0, 0)
    }

    fun setTitle(name: String) = tvTitle!!.setText(name)

}