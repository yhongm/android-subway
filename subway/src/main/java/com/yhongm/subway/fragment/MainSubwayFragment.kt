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

package com.yhongm.subway.fragment

import android.support.v4.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.yhongm.subway.DataObject
import com.yhongm.subway.view.AlertLineDialog
import com.yhongm.subway.R
import com.yhongm.subway.svg.RenderSvgPathView
import com.yhongm.subway.view.SubwayPopWindow
import com.yhongm.subway.xml.SVG
import com.yhongm.subway.xml.Station

class MainSubwayFragment : Fragment(), AlertLineDialog.OnChangeListener {


    var mSvgs: ArrayList<SVG>? = null
    var alertLineDialog: AlertLineDialog? = null
    var spv: RenderSvgPathView? = null

    var testListStations: ArrayList<Station>? = null
    var resultList: ArrayList<String>? = null
    var hsv: HorizontalScrollView? = null

    var vpopwindow: SubwayPopWindow? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.fragment_mainsubway, container, false)
        spv = view.findViewById(R.id.spv)
        hsv = view.findViewById<HorizontalScrollView>(R.id.main_hsv_h)
        vpopwindow = SubwayPopWindow(activity!!.applicationContext)
        spv!!.setPvgs(mSvgs!!)
        spv!!.setStrokeColor(Color.YELLOW)


        spv!!.start()
        spv!!.setListener(object : RenderSvgPathView.RenderSvgLister {
            override fun firstContent(x: Int, y: Int) {

                hsv!!.scrollTo(x, y)

            }

            override fun clickStationName(name: String, view: View, x: Int, y: Int) {


                val stations = DataObject.parseStations()

                val list1 = stations!!.filter {
                    it.name.equals(name)
                }
                if (list1.size > 0) {
                    var firstend = list1[0].firstend

                    val firstAndEnd = firstend.split("||||||")
                    var first = ""
                    var end = ""
                    if (firstAndEnd.size > 1) {
                        first = firstAndEnd[0].replaceFirst("::::::", ":").replace("::::::", "-->")
                        end = firstAndEnd[1].replaceFirst("::::::", ":").replace("::::::", "-->")
                    } else if (firstAndEnd.size == 1) {
                        first = firstAndEnd[0].replaceFirst("::::::", ":").replace("::::::", "-->")
                        end = ""
                    }
                    if (vpopwindow!!.isShowing) {
                        vpopwindow!!.dismiss()
                    }
                    vpopwindow!!.setContent(first, end)
                    vpopwindow!!.setTitle(name)
                    vpopwindow!!.show(view, x, y)
                }

            }

        })
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertLineDialog = AlertLineDialog(activity!!)
        testListStations = ArrayList()
        resultList = ArrayList()

        alertLineDialog!!.setOnStateChangeListener(this)
        mSvgs = DataObject.parseSvg()


    }

    override fun change(svgs: ArrayList<SVG>) {
        spv!!.setPvgs(svgs)
    }

    fun setIsShowStationName(b: Boolean) {
        spv!!.setIsShowStationName(b)
    }

    fun setIsShowStationLocation(b: Boolean) {
        spv!!.setIsShowStationLocation(b)
    }

    fun setScale(d: Double) {
        spv!!.setScale(d)
    }

    fun showAlertLineDialog() {
        alertLineDialog!!.show()
        alertLineDialog!!.setSvgs(mSvgs!!)
    }

}