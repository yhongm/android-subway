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


package com.yhongm.subway

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.yhongm.colorpicker.ColorPickerView
import com.yhongm.colorpicker.Utils
import com.yhongm.colorpicker.builder.ColorPickerDialogBuilder

import com.yhongm.subway.xml.SVG

class SearchAdapter constructor(context: Context, results: ArrayList<HashMap<String, String>>) : BaseAdapter() {
    var mContext: Context? = null
    var mResults: ArrayList<HashMap<String, String>>? = null

    init {
        mContext = context
        mResults = results
    }

    fun setDatas(results: ArrayList<HashMap<String, String>>) {
        mResults = results
        notifyDataSetInvalidated()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        if (convertView == null) {


            view = View.inflate(mContext, R.layout.listview_search_item, null)

        } else {
            view = convertView
        }
        val cardview = view!!.get<CardView>(R.id.cardview)
        val tvLines = view!!.get<TextView>(R.id.item_tv_lines)
        val tvTotal = view.get<TextView>(R.id.item_tv_total)
        val result = mResults!!.get(position)
        cardview.setCardBackgroundColor(Color.parseColor("#0ff0f0"))
        cardview.radius = 10f
        cardview.useCompatPadding = true
        cardview.cardElevation = 0.5f


        tvLines.text = result.map { it.key }[0]
        tvTotal.text = "共换乘${result.map { it.value }[0]}站"
        return view
    }

    override fun getItem(position: Int): Any {
        return mResults!!.get(position)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        return mResults!!.size
    }
}

class LineColorAdapter constructor(context: Context) : BaseAdapter() {
    var mContext: Context? = null
    var mSvgs: ArrayList<SVG>? = null

    init {
        mContext = context

    }

    fun setSvgs(svgs: ArrayList<SVG>) {
        this.mSvgs = svgs
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        if (convertView == null) {


            view = View.inflate(mContext, R.layout.line_item, null)

        } else {
            view = convertView
        }
        var textView = view!!.get<TextView>(R.id.ll_name)
        var ck = view!!.get<CheckBox>(R.id.ll_ck)
        var cColor = view!!.get<View>(R.id.ll_color)
        val svg = mSvgs!!.get(position)
        textView.setText(svg.name)
        cColor.setOnClickListener({
            ColorPickerDialogBuilder
                    .with(mContext)
                    .setTitle("选择线路颜色")
                    .initialColor(Color.parseColor(svg!!.color))
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
//                    .setOnColorSelectedListener { selectedColor -> }
                    .setPositiveButton("确定") { dialog, selectedColor, allColors ->
                        cColor.setBackgroundColor(selectedColor)
                        svg.color = Utils.getHexString(selectedColor, false)

                        mSvgs!![position] = svg
                    }
                    .setNegativeButton("取消") { _, _ -> }
                    .build()
                    .show()
        })
        cColor.setBackgroundColor(Color.parseColor(svg.color))
        ck.isChecked = svg.isSelect

        ck.setOnClickListener({
            svg.isSelect = ck.isChecked
            mSvgs!![position] = svg


        })




        return view
    }


    override fun getItem(position: Int): Any {
        return mSvgs!!.get(position)

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getCount(): Int {
        if (mSvgs != null) {
            return mSvgs!!.size
        }
        return 0
    }

    fun getResults(): ArrayList<SVG>? {
        return mSvgs
    }

}

fun <T : View> View.get(viewId: Int): T {
    var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    tag = viewHolder
    var childView: View? = viewHolder.get(viewId)
    if (null == childView) {
        childView = findViewById(viewId)
        viewHolder.put(viewId, childView)
    }
    return childView as T
}