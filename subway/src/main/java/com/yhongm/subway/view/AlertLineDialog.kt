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

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.*
import com.yhongm.subway.xml.SVG

import com.yhongm.subway.LineColorAdapter
import com.yhongm.subway.R
import com.yhongm.subway.fragment.MainSubwayFragment


class AlertLineDialog constructor(context: Context) : Dialog(context, 0) {

    var cAdapter: LineColorAdapter? = null
    var mListView: ListView? = null

    var mListener: OnChangeListener? = null

    var mResultSvgs: ArrayList<SVG>? = null

    init {
        cAdapter = LineColorAdapter(context)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert_line)
        mListView = findViewById(R.id.listView)
        findViewById<Button>(R.id.close).setOnClickListener({
            dismiss()
        })


    }

    override fun dismiss() {
        super.dismiss()
        mResultSvgs = cAdapter!!.getResults()
        if (mListener != null) {
            mListener!!.change(svgs = mResultSvgs!!)
        }
    }

    fun setSvgs(svgs: ArrayList<SVG>) {
        cAdapter!!.setSvgs(svgs)

        if (mListView != null) {
            mListView!!.adapter = cAdapter
        }
    }

    fun setOnStateChangeListener(onChangeListener: OnChangeListener) {
        this.mListener = onChangeListener
    }

    interface OnChangeListener {
        fun change(svgs: ArrayList<SVG>)
    }


}




